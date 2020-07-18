package View;

import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import static javafx.scene.paint.Color.BLACK;

public class SudokuCell extends ToggleButton {

    private int x, y; //position on the board

    public SudokuCell(int x, int y){
        this.y=y;
        this.x=x;

        setPrefSize(50,50);
        setTextAlignment(TextAlignment.CENTER);
        setText("0");
        setDisabled(false);
        setFont(Font.font("Default", FontWeight.BOLD,24));


    }
    public void setValue(int value, boolean userInput){
        setTextFill(userInput ? BLACK : Color.RED);
        if(value > 0 && value < 10){
            setText(value +"");
            ;
        }else{
            setText(value+"");
        }
    }

    public int getX(){
        return x;
    }

    public int getY() {
        return y;
    }
}
