package com.project.social_network.model;

import java.util.Arrays;
import java.util.UUID;

public class TicTacToeGame {
    private String gameId;
    private String[] board;
    private String currentTurn;
    private String playerX;
    private String playerO;
    private String winner;
    private boolean gameOver;

    public TicTacToeGame() {
        this.gameId = UUID.randomUUID().toString();
        this.board = new String[9];
        Arrays.fill(this.board, null);
        this.currentTurn = "X";
        this.winner = null;
        this.gameOver = false;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String[] getBoard() {
        return board;
    }

    public void setBoard(String[] board) {
        this.board = board;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(String currentTurn) {
        this.currentTurn = currentTurn;
    }

    public String getPlayerX() {
        return playerX;
    }

    public void setPlayerX(String playerX) {
        this.playerX = playerX;
    }

    public String getPlayerO() {
        return playerO;
    }

    public void setPlayerO(String playerO) {
        this.playerO = playerO;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}