package com.project.social_network.service;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.project.social_network.model.TicTacToeGame;
import com.project.social_network.service.interfaces.TicTacToeService;

@Service
public class TicTacToeServiceImpl implements TicTacToeService {
    private static final Logger logger = LoggerFactory.getLogger(TicTacToeServiceImpl.class);

    private final Map<String, TicTacToeGame> games = new ConcurrentHashMap<>();
    private final Map<String, ReentrantLock> gameLocks = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> gameSessionRegistry = new ConcurrentHashMap<>();
    private final Map<String, Map<String, String>> gameToSessionToRoleMap = new ConcurrentHashMap<>();

    @Override
    public TicTacToeGame createGame(String gameId) {
        TicTacToeGame game = new TicTacToeGame();
        if (gameId != null && !gameId.isEmpty()) {
            game.setGameId(gameId);
        }
        games.put(game.getGameId(), game);
        gameLocks.put(game.getGameId(), new ReentrantLock());
        gameSessionRegistry.put(game.getGameId(), ConcurrentHashMap.newKeySet());
        gameToSessionToRoleMap.put(game.getGameId(), new ConcurrentHashMap<>());
        logger.info("Created new game with ID: {}", game.getGameId());
        return game;
    }

    @Override
    public TicTacToeGame joinGame(String gameId, String sessionId) {
        // Get or create game lock
        ReentrantLock lock = gameLocks.computeIfAbsent(gameId, k -> new ReentrantLock());

        lock.lock();
        try {
            logger.info("Session {} trying to join game {}", sessionId, gameId);
            TicTacToeGame game = games.get(gameId);

            if (game == null) {
                logger.info("Game {} does not exist, creating new game", gameId);
                game = createGame(gameId);
            }

            // Get the session registry for this game
            Set<String> sessionRegistry = gameSessionRegistry.computeIfAbsent(gameId,
                    k -> ConcurrentHashMap.newKeySet());

            // Get role mapping for this game
            Map<String, String> sessionToRoleMap = gameToSessionToRoleMap.computeIfAbsent(gameId,
                    k -> new ConcurrentHashMap<>());

            // Check if this session already has a role assigned
            String existingRole = sessionToRoleMap.get(sessionId);

            if (existingRole != null) {
                logger.info("Session {} already has role {} in game {}", sessionId, existingRole, gameId);

                // Ensure role consistency
                if ("X".equals(existingRole) && !sessionId.equals(game.getPlayerX())) {
                    logger.info("Restoring session {} as player X", sessionId);
                    game.setPlayerX(sessionId);
                } else if ("O".equals(existingRole) && !sessionId.equals(game.getPlayerO())) {
                    logger.info("Restoring session {} as player O", sessionId);
                    game.setPlayerO(sessionId);
                }

                // Add session to registry
                sessionRegistry.add(sessionId);
                return game;
            }

            // Add session to registry
            sessionRegistry.add(sessionId);

            // Check if this session is already assigned a role in the game itself
            if (sessionId.equals(game.getPlayerX())) {
                logger.info("Session {} reconnected as player X", sessionId);
                sessionToRoleMap.put(sessionId, "X");
                return game;
            } else if (sessionId.equals(game.getPlayerO())) {
                logger.info("Session {} reconnected as player O", sessionId);
                sessionToRoleMap.put(sessionId, "O");
                return game;
            }

            // Assign player to X or O only if the role is not already taken
            if (game.getPlayerX() == null) {
                game.setPlayerX(sessionId);
                sessionToRoleMap.put(sessionId, "X");
                logger.info("Session {} assigned as player X", sessionId);
            } else if (game.getPlayerO() == null && !sessionId.equals(game.getPlayerX())) {
                game.setPlayerO(sessionId);
                sessionToRoleMap.put(sessionId, "O");
                logger.info("Session {} assigned as player O", sessionId);
            } else {
                // Both roles are taken, this player is a spectator
                sessionToRoleMap.put(sessionId, "SPECTATOR");
                logger.info("Session {} assigned as SPECTATOR", sessionId);
            }

            // Log the current state
            logger.info("Game {} current state - playerX: {}, playerO: {}, roles: {}",
                    gameId, game.getPlayerX(), game.getPlayerO(), sessionToRoleMap);

            return game;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public TicTacToeGame makeMove(String gameId, int position, String sessionId) {
        ReentrantLock lock = gameLocks.get(gameId);
        if (lock == null) {
            logger.warn("Attempt to make move on non-existent game: {}", gameId);
            return null;
        }

        lock.lock();
        try {
            TicTacToeGame game = games.get(gameId);

            if (game == null) {
                logger.warn("Game not found: {}", gameId);
                return null;
            }

            // Check if the game is over
            if (game.isGameOver()) {
                logger.info("Attempted move on completed game: {}", gameId);
                return game;
            }

            // Check if it's the player's turn
            boolean isPlayerX = sessionId.equals(game.getPlayerX());
            boolean isPlayerO = sessionId.equals(game.getPlayerO());

            logger.info("Move attempt - isPlayerX: {}, isPlayerO: {}, currentTurn: {}",
                    isPlayerX, isPlayerO, game.getCurrentTurn());

            if ((game.getCurrentTurn().equals("X") && !isPlayerX) ||
                    (game.getCurrentTurn().equals("O") && !isPlayerO)) {
                logger.warn("Invalid move attempt: not player's turn");
                return game;
            }

            // Check if the position is valid and not already taken
            if (position < 0 || position >= 9 || game.getBoard()[position] != null) {
                logger.warn("Invalid move position: {}", position);
                return game;
            }

            // Make the move
            game.getBoard()[position] = game.getCurrentTurn();
            logger.info("Move made at position {} by player {}", position, game.getCurrentTurn());

            // Check for a winner
            checkWinner(game);

            // Switch turns if the game is not over
            if (!game.isGameOver()) {
                game.setCurrentTurn(game.getCurrentTurn().equals("X") ? "O" : "X");
                logger.info("Turn switched to {}", game.getCurrentTurn());
            }

            return game;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public TicTacToeGame resetGame(String gameId) {
        ReentrantLock lock = gameLocks.get(gameId);
        if (lock == null) {
            logger.warn("Attempt to reset non-existent game: {}", gameId);
            return null;
        }

        lock.lock();
        try {
            TicTacToeGame game = games.get(gameId);

            if (game == null) {
                logger.warn("Game not found for reset: {}", gameId);
                return null;
            }

            // Reset the board
            Arrays.fill(game.getBoard(), null);
            game.setCurrentTurn("X");
            game.setWinner(null);
            game.setGameOver(false);

            logger.info("Game {} has been reset", gameId);
            return game;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public TicTacToeGame leaveGame(String gameId, String sessionId) {
        ReentrantLock lock = gameLocks.get(gameId);
        if (lock == null) {
            logger.warn("Attempt to leave non-existent game: {}", gameId);
            return null;
        }

        lock.lock();
        try {
            TicTacToeGame game = games.get(gameId);

            if (game == null) {
                logger.warn("Game not found for leave: {}", gameId);
                return null;
            }

            // Get the role mapping
            Map<String, String> sessionToRoleMap = gameToSessionToRoleMap.get(gameId);
            if (sessionToRoleMap != null) {
                String role = sessionToRoleMap.remove(sessionId);
                logger.info("Session {} with role {} leaving game {}", sessionId, role, gameId);
            }

            // Remove session from registry
            Set<String> sessionRegistry = gameSessionRegistry.get(gameId);
            if (sessionRegistry != null) {
                sessionRegistry.remove(sessionId);
                logger.info("Removed session {} from game registry", sessionId);
            }

            // If a player leaves, reset their position
            if (sessionId.equals(game.getPlayerX())) {
                game.setPlayerX(null);
                logger.info("Player X position reset in game {}", gameId);
            } else if (sessionId.equals(game.getPlayerO())) {
                game.setPlayerO(null);
                logger.info("Player O position reset in game {}", gameId);
            }

            // If both players have left and no sessions are connected, remove the game
            if (game.getPlayerX() == null && game.getPlayerO() == null &&
                    (sessionRegistry == null || sessionRegistry.isEmpty())) {
                games.remove(gameId);
                gameLocks.remove(gameId);
                gameSessionRegistry.remove(gameId);
                gameToSessionToRoleMap.remove(gameId);
                logger.info("Removed game {} as all players have left", gameId);
                return null;
            }

            logger.info("Game {} state after leave - playerX: {}, playerO: {}",
                    gameId, game.getPlayerX(), game.getPlayerO());
            return game;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public TicTacToeGame getGame(String gameId) {
        return games.get(gameId);
    }

    private void checkWinner(TicTacToeGame game) {
        String[] board = game.getBoard();

        // Check rows
        for (int i = 0; i < 9; i += 3) {
            if (board[i] != null && board[i].equals(board[i + 1]) && board[i].equals(board[i + 2])) {
                setWinner(game, board[i]);
                logger.info("Winner found in row {}: {}", i / 3, board[i]);
                return;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (board[i] != null && board[i].equals(board[i + 3]) && board[i].equals(board[i + 6])) {
                setWinner(game, board[i]);
                logger.info("Winner found in column {}: {}", i, board[i]);
                return;
            }
        }

        // Check diagonals
        if (board[0] != null && board[0].equals(board[4]) && board[0].equals(board[8])) {
            setWinner(game, board[0]);
            logger.info("Winner found in diagonal \\: {}", board[0]);
            return;
        }

        if (board[2] != null && board[2].equals(board[4]) && board[2].equals(board[6])) {
            setWinner(game, board[2]);
            logger.info("Winner found in diagonal /: {}", board[2]);
            return;
        }

        // Check for a draw
        boolean isBoardFull = true;
        for (String cell : board) {
            if (cell == null) {
                isBoardFull = false;
                break;
            }
        }

        if (isBoardFull) {
            game.setGameOver(true);
            game.setWinner("DRAW");
            logger.info("Game ended in a draw");
        }
    }

    private void setWinner(TicTacToeGame game, String winner) {
        game.setWinner(winner);
        game.setGameOver(true);
        logger.info("Game over, winner: {}", winner);
    }
}