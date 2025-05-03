package com.project.social_network.service.interfaces;

import com.project.social_network.model.TicTacToeGame;

public interface TicTacToeService {
    TicTacToeGame createGame(String gameId);

    TicTacToeGame joinGame(String gameId, String sessionId);

    TicTacToeGame makeMove(String gameId, int position, String sessionId);

    TicTacToeGame resetGame(String gameId);

    TicTacToeGame leaveGame(String gameId, String sessionId);

    TicTacToeGame getGame(String gameId);
}