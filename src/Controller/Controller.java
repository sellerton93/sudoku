package Controller;

import Model.Game;
import View.SudokuCell;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;


public class Controller {
    public GridPane mainBoard;
    public AnchorPane anchorPane;
    public Button solveButton;
    public Button newGameButton;
    public Button clearButton;

    private SudokuCell[][] cells;
    private String selectedValue;
    private Game game;
    @FXML
    private ChoiceBox choiceBox;


    public void initialize() {

        choiceBox.setItems(FXCollections.observableArrayList("Blank","Easy","Medium","Hard","DEMO - Candidate line", "DEMO - Given value"));
        choiceBox.setValue("Blank");
        game = new Game();
        cells = new SudokuCell[9][9];
        selectedValue = "0";


        anchorPane.setOnKeyPressed(keyEvent -> {
            String str = keyEvent.getCode().toString();
            if (str.equals("ESCAPE")) {
                for (int y = 0; y < 9; y++) {
                    for (int x = 0; x < 9; x++) {
                        if (cells[x][y].isSelected()) {
                            cells[x][y].setText(selectedValue);
                            cells[x][y].setSelected(false);
                        }
                    }
                }
            }
            else if(str.equals("DELETE")){
                for (int y = 0; y < 9; y++) {
                    for (int x = 0; x < 9; x++) {
                        if (cells[x][y].isSelected()) {
                            cells[x][y].setText("");
                            cells[x][y].setSelected(false);
                        }
                    }
                }
            }
            else {
                str = str.substring(str.length() - 1);
                try {
                    int value = Integer.parseInt(str);

                    for (int y = 0; y < 9; y++) {
                        for (int x = 0; x < 9; x++) {
                            if (cells[x][y].isSelected()) {

                                game.setValue(x, y, value);
                                cells[x][y].setText(str);
                                cells[x][y].setSelected(false);

                            }
                        }
                    }

                } catch (NumberFormatException e) {
                    // Decide what to do here
                }
            }
        });

        newGamePressed();


    }

    public Controller(){

    }

    public void solveButtonPress(){


        clearPressed();
        int[][] solution = game.getSolution();
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if(game.getValue(x,y) ==0){

                    game.setValue(x,y,solution[x][y]);
                    cells[x][y].setText(Integer.toString(solution[x][y]));

                }
            }
        }
        update();
    }


    public void newGamePressed(){
        String difficulty = choiceBox.getValue().toString();
        game.newGame(difficulty);
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                SudokuCell cell = new SudokuCell(x, y);

                cell.setOnAction(actionEvent -> {

                            cell.isSelected();
                            selectedValue = cell.getText();
                        }
                );
                cells[y][x] = cell;
                mainBoard.add(cells[y][x], x, y);

            }
        }
        update();
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (!(cells[y][x].getText().equals(""))){
                    cells[y][x].getStyleClass().removeAll();
                    cells[y][x].getStyleClass().add("set-button");
                    cells[y][x].setDisable(true);
                }
                else{
                    cells[y][x].getStyleClass().removeAll();
                    cells[y][x].getStyleClass().add("toggle-button");
                    cells[y][x].setDisable(false);
                }
            }
        }
        update();
    }

    public void clearPressed(){

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if(!(cells[x][y].getStyleClass().toString().contains("set"))){
                    cells[x][y].setText("");
                    game.setValue(x,y,0);
                }

            }
        }

        update();

    }

    public void update(){
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                int value = game.getValue(x,y);
                if(value==0){
                    cells[x][y].setText("");

                }else {
                    String str = Integer.toString(value);
                    cells[x][y].setText(str);

                }
            }
        }
    }

}

