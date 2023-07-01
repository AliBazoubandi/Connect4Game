package com.example.connect4;

public class ComputerPlayer {

    Connect4Game game = new Connect4Game();
    public int bestMove(Player[][] gameBoard) {
        int columns = gameBoard[0].length;
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (int column = 0; column < columns; column++) {
            if (Connect4Game.isSlotAvailable(gameBoard, column)) {
                // Make a hypothetical move for the computer player
                Player[][] hypotheticalBoard = game.makeHypotheticalMove(gameBoard, column, Player.COMPUTER);

                // Calculate the score for this move using the Minimax algorithm with Alpha-Beta pruning
                int score = miniMax(hypotheticalBoard, 0, false, alpha, beta);

                // Update the best move and score if a better move is found
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = column;
                }

                // Update the alpha value
                alpha = Math.max(alpha, bestScore);

                // Perform alpha-beta pruning
                if (alpha >= beta) {
                    break;
                }
            }
        }

        return bestMove;
    }

    private int miniMax(Player[][] gameBoard, int depth, boolean isMaximizingPlayer, int alpha, int beta) {
        if (game.isGameOver(gameBoard) || depth == Connect4Game.getMaxDepth()) {
            // Evaluate the game board and return the score
            return evaluateBoard(gameBoard);
        }

        int bestScore;
        int columns = gameBoard[0].length;
        if (isMaximizingPlayer) {
            bestScore = Integer.MIN_VALUE;

            for (int column = 0; column < columns; column++) {
                if (Connect4Game.isSlotAvailable(gameBoard, column)) {
                    // Make a hypothetical move for the computer player
                    Player[][] hypotheticalBoard = game.makeHypotheticalMove(gameBoard, column, Player.COMPUTER);

                    // Recursively call the miniMax function for the opponent's turn
                    int score = miniMax(hypotheticalBoard, depth + 1, false, alpha, beta);

                    // Update the best score if a higher score is found
                    bestScore = Math.max(bestScore, score);

                    // Update the alpha value
                    alpha = Math.max(alpha, bestScore);

                    // Perform alpha-beta pruning
                    if (alpha >= beta) {
                        break;
                    }
                }
            }

        } else {
            bestScore = Integer.MAX_VALUE;

            for (int column = 0; column < columns; column++) {
                if (Connect4Game.isSlotAvailable(gameBoard, column)) {
                    // Make a hypothetical move for the human player
                    Player[][] hypotheticalBoard = game.makeHypotheticalMove(gameBoard, column, Player.PLAYER);

                    // Recursively call the miniMax function for the computer player's turn
                    int score = miniMax(hypotheticalBoard, depth + 1, true, alpha, beta);

                    // Update the best score if a lower score is found
                    bestScore = Math.min(bestScore, score);

                    // Update the beta value
                    beta = Math.min(beta, bestScore);

                    // Perform alpha-beta pruning
                    if (alpha >= beta) {
                        break;
                    }
                }
            }

        }
        return bestScore;
    }

    private int evaluateBoard(Player[][] gameBoard) {
        int score = 0;
        int rows = gameBoard.length;
        int columns = gameBoard[0].length;

        // Evaluate rows
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns - 3; column++) {
                int playerCount = 0;
                int computerCount = 0;

                for (int k = 0; k < 4; k++) {
                    if (gameBoard[row][column + k] == Player.PLAYER) {
                        playerCount++;
                    } else if (gameBoard[row][column + k] == Player.COMPUTER) {
                        computerCount++;
                    }
                }

                score += calculateScore(playerCount, computerCount);
            }
        }

        // Evaluate columns
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows - 3; row++) {
                int playerCount = 0;
                int computerCount = 0;

                for (int k = 0; k < 4; k++) {
                    if (gameBoard[row + k][column] == Player.PLAYER) {
                        playerCount++;
                    } else if (gameBoard[row + k][column] == Player.COMPUTER) {
                        computerCount++;
                    }
                }

                score += calculateScore(playerCount, computerCount);
            }
        }

        // Evaluate diagonals (positive slope)
        for (int row = 0; row < rows - 3; row++) {
            for (int column = 0; column < columns - 3; column++) {
                int playerCount = 0;
                int computerCount = 0;

                for (int k = 0; k < 4; k++) {
                    if (gameBoard[row + k][column + k] == Player.PLAYER) {
                        playerCount++;
                    } else if (gameBoard[row + k][column + k] == Player.COMPUTER) {
                        computerCount++;
                    }
                }

                score += calculateScore(playerCount, computerCount);
            }
        }

        // Evaluate diagonals (negative slope)
        for (int row = 3; row < rows; row++) {
            for (int column = 0; column < columns - 3; column++) {
                int playerCount = 0;
                int computerCount = 0;

                for (int k = 0; k < 4; k++) {
                    if (gameBoard[row - k][column + k] == Player.PLAYER) {
                        playerCount++;
                    } else if (gameBoard[row - k][column + k] == Player.COMPUTER) {
                        computerCount++;
                    }
                }

                score += calculateScore(playerCount, computerCount);
            }
        }

        return score;
    }

    private int calculateScore(int playerCount, int computerCount) {
        int score = 0;

        if (playerCount == 4) {
            // Human player wins
            score -= 100;
        } else if (computerCount == 4) {
            // Computer player wins
            score += 100;
        } else if (playerCount == 3 && computerCount == 0) {
            // Potential winning move for human player
            score -= 10;
        } else if (playerCount == 2 && computerCount == 0) {
            // Potential winning move for human player
            score -= 5;
        } else if (playerCount == 1 && computerCount == 0) {
            // Single piece for human player
            score -= 1;
        } else if (computerCount == 3 && playerCount == 0) {
            // Potential winning move for computer player
            score += 10;
        } else if (computerCount == 2 && playerCount == 0) {
            // Potential winning move for computer player
            score += 5;
        } else if (computerCount == 1 && playerCount == 0) {
            // Single piece for computer player
            score += 1;
        }

        return score;
    }
}
