package com.example.connect4;

import java.util.Arrays;

public class Connect4Game {


    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final int MAX_DEPTH = 6;
    private static Player winner = null;
    private Player[][] gameBoard;
    private Player currentPlayer;
    private boolean gameOver;
    private boolean isMaximizingPlayer;

    public Connect4Game() {
        this.currentPlayer = Player.PLAYER;
        this.gameBoard = new Player[ROWS][COLUMNS];
        this.gameOver = false;
        this.isMaximizingPlayer = true;
        initializeGameBoard();
    }

    public void reset() {
        this.currentPlayer = Player.PLAYER;
        this.gameBoard = new Player[ROWS][COLUMNS];
        this.gameOver = false;
        this.isMaximizingPlayer = true;
        initializeGameBoard();
    }

    public void initializeGameBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                gameBoard[row][column] = null;
            }
        }
    }

    public int getAvailableRow(int column) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (gameBoard[row][column] == null) {
                return row;
            }
        }
        return -1; // Column is full, no available row
    }

    public int makeMove(int column) {
        if (currentPlayer == Player.PLAYER && !isGameOver(gameBoard) && Connect4Game.isSlotAvailable(gameBoard, column)) {
            int row = getAvailableRow(column);
            if (row != -1) {
                gameBoard[row][column] = Player.PLAYER;
                switchPlayer();
                return row;
            }
            return -1;
        } else if (currentPlayer == Player.COMPUTER && !isGameOver(gameBoard) && Connect4Game.isSlotAvailable(gameBoard, column)) {
            int row = getAvailableRow(column);
            if (row != -1) {
                gameBoard[row][column] = Player.COMPUTER;
                switchPlayer();
                return row;
            }
            return -1;
        }
        return -1;
    }

    public Player[][] makeHypotheticalMove(Player[][] gameBoard, int column, Player player) {
        // Create a deep copy of the game board
        Player[][] hypotheticalBoard = new Player[ROWS][COLUMNS];
        for (int row = 0; row < ROWS; row++) {
            if (COLUMNS >= 0) System.arraycopy(gameBoard[row], 0, hypotheticalBoard[row], 0, COLUMNS);
        }

        // Make the hypothetical move for the specified player
        for (int row = ROWS - 1; row >= 0; row--) {
            if (hypotheticalBoard[row][column] == null) {
                hypotheticalBoard[row][column] = player;
                break;
            }
        }

        return hypotheticalBoard;
    }

    public boolean isGameBoardFull(Player[][] gameBoard) {
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                if (gameBoard[row][column] == null) {
                    return false;
                }
            }
        }
        gameOver = true;
        return true;
    }

    public static boolean checkWin(Player[][] gameBoard, Player player) {
        // Check for a horizontal win
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS - 3; column++) {
                if (gameBoard[row][column] == player &&
                        gameBoard[row][column + 1] == player &&
                        gameBoard[row][column + 2] == player &&
                        gameBoard[row][column + 3] == player) {
                    winner = player;
                    return true;
                }
            }
        }

        // Check for a vertical win
        for (int row = 0; row < ROWS - 3; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                if (gameBoard[row][column] == player &&
                        gameBoard[row + 1][column] == player &&
                        gameBoard[row + 2][column] == player &&
                        gameBoard[row + 3][column] == player) {
                    winner = player;
                    return true;
                }
            }
        }

        // Check for a diagonal win (positive slope)
        for (int row = 0; row < ROWS - 3; row++) {
            for (int column = 0; column < COLUMNS - 3; column++) {
                if (gameBoard[row][column] == player &&
                        gameBoard[row + 1][column + 1] == player &&
                        gameBoard[row + 2][column + 2] == player &&
                        gameBoard[row + 3][column + 3] == player) {
                    winner = player;
                    return true;
                }
            }
        }

        // Check for a diagonal win (negative slope)
        for (int row = 3; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS - 3; column++) {
                if (gameBoard[row][column] == player &&
                        gameBoard[row - 1][column + 1] == player &&
                        gameBoard[row - 2][column + 2] == player &&
                        gameBoard[row - 3][column + 3] == player) {
                    winner = player;
                    return true;
                }
            }
        }

        return false;
    }
    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameOver(Player[][] gameBoard) {
        return (checkWin(gameBoard, Player.PLAYER) || checkWin(gameBoard, Player.COMPUTER) || isGameBoardFull(gameBoard));
    }

    public boolean isSlotAvailable(int row, int column) {
        return gameBoard[row][column] == null;
    }

    public static boolean isSlotAvailable(Player[][] gameBoard, int column) {
        if (column < 0 || column >= COLUMNS) {
            return false;
        }

        for (int row = 0; row < ROWS; row++) {
            if (gameBoard[row][column] == null) {
                return true;
            }
        }

        return false;
    }

    public void switchPlayer() {
        currentPlayer = currentPlayer == Player.PLAYER ? Player.COMPUTER : Player.PLAYER;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static Player getWinner() {
        return winner;
    }

    public static int getROWS() {
        return ROWS;
    }

    public static int getCOLUMNS() {
        return COLUMNS;
    }

    public static int getMaxDepth() {
        return MAX_DEPTH;
    }

    public Player[][] getGameBoard() {
        return gameBoard;
    }
}
