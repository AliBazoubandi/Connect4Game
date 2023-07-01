package com.example.connect4;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Connect4GUIController implements Initializable {
    @FXML
    private GridPane boardPane;

    @FXML
    private Button resetButton;

    @FXML
    private Label currentPlayerLabel;

    private Connect4Game game;

    private ComputerPlayer computerPlayer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game = new Connect4Game();
        computerPlayer = new ComputerPlayer();
        currentPlayerLabel.setText("Current Player: " + game.getCurrentPlayer());
        createBoard();
    }

    private void createBoard() {
        for (int row = 0; row < Connect4Game.getROWS(); row++) {
            for (int column = 0; column < Connect4Game.getCOLUMNS(); column++) {
                Button slot = new Button();
                int finalColumn = column;
                slot.setOnAction(e -> makeMove(finalColumn));
                boardPane.add(slot, column, row);
            }
        }
    }

    @FXML
    private void resetGame() {
        game.reset();
        currentPlayerLabel.setText("Current Player: " + game.getCurrentPlayer());
        enableSlots();
        clearBoardColors();
    }

    private void clearBoardColors() {
        boardPane.getChildren().forEach(node -> node.setStyle(null));
    }
    @FXML
    private void makeMove(int column) {
        if (game.getCurrentPlayer().equals(Player.PLAYER)) {
            int row = game.makeMove(column);
            if (row != -1) {
                Button slot = (Button) boardPane.getChildren().get(row * 7 + column);
                slot.setStyle("-fx-background-color: blue;");
                disableSlots();
                currentPlayerLabel.setText("Current Player: " + Player.COMPUTER);
                enableSlots();
                makeComputerMove();
            } else {
                handleGameOver();
            }
        }
    }

    private void makeComputerMove() {
        int column = computerPlayer.bestMove(game.getGameBoard());
        int row = game.makeMove(column);

        Button slot = (Button) boardPane.getChildren().get(row * 7 + column);
        slot.setStyle("-fx-background-color: red;");
        if (game.isGameOver()) {
            handleGameOver();
        } else {
            currentPlayerLabel.setText("Current Player: " + Player.PLAYER);
            enableSlots();
        }
    }

    private void handleGameOver() {
        if (Connect4Game.getWinner() == Player.PLAYER) {
            displayAlert("Congratulations! \uD83C\uDF89", "You win!");
        } else if (Connect4Game.getWinner() == Player.COMPUTER) {
            displayAlert("Better luck next time! \uD83D\uDE0A", "Computer wins!");
        } else {
            displayAlert("It's a draw! \uD83E\uDD1D", "The game ended in a draw.");
        }

        // Disable further moves
        for (Node node : boardPane.getChildren()) {
            if (node instanceof Button slot) {
                slot.setDisable(true);
            }
        }
        resetGame();
    }

    private void displayAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void disableSlots() {
        boardPane.getChildren().forEach(node -> node.setDisable(true));
    }

    private void enableSlots() {
        boardPane.getChildren().forEach(node -> node.setDisable(!game.isSlotAvailable(GridPane.getRowIndex(node), GridPane.getColumnIndex(node))));
    }
}