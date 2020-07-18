package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Game {

    private int[][] solution;
    private int[][] board;
    private int valid;
    private int[][] clues;

    private int numSolutionsFound=0;


    public Game(){
        newGame("Easy");
    }

    public void newGame(String difficulty){


        board = new int[9][9];
        clues = new int[9][9];
        solution = new int[9][9];


        //Randomize a list of integers 1-9 and place it on the first row of the board
        ArrayList<Integer> list = new ArrayList<>();
        for(int i =1;i<10;i++){
            list.add(i);
        }

        Collections.shuffle(list);
        for(int i=0;i<9;i++){
            board[0][i]= list.get(i);
        }

        //shift the list by 3, and place in the next row of the board, then repeat for the 3rd row. This gives 3 unique & valid 3x3 blocks
        Collections.rotate(list,3);
        for(int i=0;i<9;i++){
            board[1][i]=list.get(i);
        }
        Collections.rotate(list,3);
        for(int i=0;i<9;i++){
            board[2][i]=list.get(i);
        }

        solve(board);


        //rotate the board to increase randomness and help hide any potential pattern that may emerge.
        int rotations = new Random().nextInt(3);
        while(rotations>0){
            rotateBoard();
            rotations--;
        }

        //keep a copy of this solution for later.
        solution = Arrays.stream(board).map(a -> Arrays.copyOf(a,a.length)).toArray(int[][]::new);

        switch (difficulty){
            case "Blank":
                clearBoard();
                break;
            case "Easy":
                setEasy();
                break;
            case "Medium":
                setMedium();
                break;
            case "Hard":
                setHard();
                break;
            case "DEMO - Candidate line":
                demoCandidateLine();
                break;
            case "DEMO - Given value":
                demoGiven();
                break;

        }

    }


    public ArrayList findNext(int[][] game){

        ArrayList<Integer> result = new ArrayList();
        for(int x=0;x<9;x++){
            for(int y=0;y<9;y++){
                if (game[x][y] == 0) {
                    result.add(x);
                    result.add(y);
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * Check if the current value being placed into the board is a valid input
     * @param game the game board we're currently solving
     * @param value value attempting to be input
     * @param posx x coordinate
     * @param posy y coordinate
     * @return true if valid input, else false
     */
    public boolean isValid(int[][] game, int value, int posx, int posy){

        if(value <1 || value > 9){
            return false;
        }

        //Check the row
        for(int x=0;x<9;x++){
            if(game[x][posy] == value && x != posx){
                return false;
            }
        }

        //Check Column
        for (int y=0;y<9;y++){
            if(game[posx][y] == value  && y != posy){
                return false;
            }
        }

        //Check 3x3 block
        int blockposx = posx / 3;
        int blockposy = posy / 3;

        for (int x = blockposx*3; x < blockposx*3+3; x++){
            for(int y = blockposy*3; y< blockposy*3+3; y++){
                if(game[x][y] == value  &&  game[x][y] != game[posx][posy]){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Find a solution to the current board. Will only find a solution to a valid board - otherwise program will hang
     * @return
     */
    public boolean solve(int[][] board) {

        ArrayList<Integer> position = findNext(board);
        if (position.isEmpty() ) {

            return true;
        }

        else{

            int x = position.get(0);
            int y = position.get(1);

            for(int i =1; i<10;i++){
                if(isValid(board, i, x, y)) {
                    board[x][y] = i;
                    if (solve(board)) {
                        return true;
                    }else{
                        board[x][y]=0;
                    }
                }
            }
        }

        return false;
    }


    /*
    public boolean isValidGame(int[][] board){

        ArrayList<Integer> position = findNext(board);
        if (position.isEmpty() ) {
            if(numSolutionsFound < 0){
                numSolutionsFound++;
                valid = Arrays.hashCode(board);
                return false;
            }
            if(numSolutionsFound>0){
                if(Arrays.hashCode(board) ==valid){
                    return true;
                }
            }
            return true;
        }

        else{
            int x = position.get(0);
            int y = position.get(1);
            for(int i =1; i<10;i++){
                if(isValid(board, i, x, y)) {
                    board[x][y] = i;
                    if (solve(board)) {
                        return true;
                    }else{
                        board[x][y]=0;
                    }
                }
            }
        }

        return false;
    }*/



    public void setValue(int posx, int posy, int value){
        board[posx][posy] = value;
    }

    public int getValue(int posx, int posy){
        return board[posx][posy];
    }

    public int[][] getBoard(){
        return board;
    }

    public int[][] getSolution(){
        solution = Arrays.stream(board).map(a -> Arrays.copyOf(a,a.length)).toArray(int[][]::new);
        solve(solution);
        return solution;
    }


    public void clearBoard(){
        for(int x=0;x<9;x++){
            for(int y=0;y<9;y++){
                board[x][y] =0;
                solution[x][y]=0;
            }
        }
    }

    public void rotateBoard() {

        for (int x = 0; x < 9; x++) {
            for (int y = x + 1; y < 9; y++) {
                int temp = board[x][y];
                board[x][y] = board[y][x];
                board[y][x] = temp;
            }
        }

        for (int x = 0; x < 9 / 2; x++) {
            for (int y = 0; y < 9; y++) {
                int temp = board[x][y];
                board[x][y] = board[9 - (x + 1)][y];
                board[9 - (x + 1)][y] = temp;

            }
        }

    }

    public ArrayList findPairingOnX(ArrayList<Integer> position){

        ArrayList<Integer> pairPosition = new ArrayList<>();

        int x = position.get(0);
        int y = position.get(1);
        int valueToMatch = board[x][y];

        int yGroup = y/3;

        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(j/3 == yGroup){
                    pairPosition.add(0,i);
                    pairPosition.add(1,j);
                    if(board[i][j] ==valueToMatch && x != i && y != j){
                        return pairPosition;
                    }
                }
            }
        }

        return position;
    }

    public ArrayList findPairingOnY(ArrayList<Integer> position){

        ArrayList<Integer> pairPosition = new ArrayList<>();

        int x = position.get(0);
        int y = position.get(1);
        int valueToMatch = board[x][y];

        int xGroup = x/3;

        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(i/3 == xGroup){
                    pairPosition.add(0,i);
                    pairPosition.add(1,j);
                    if(board[i][j] ==valueToMatch && x != i && y != j){
                        return pairPosition;
                    }
                }
            }
        }

        return position;
    }


    /**
     * A candidate line is created with a matching pair of numbers and one missing number within a 3x9 block, it hints the 3 possible positions for the hidden number
     * @param numX number of candidate lines on X axis
     * @param numY number of candidate lines on Y axis
     */
    public void setCandidateLines(int numX, int numY){

        ArrayList<Integer> position = new ArrayList<>();
        ArrayList<Integer> pairPosition = new ArrayList<>();

        while(numX >0){
            int x = new Random().nextInt(9);
            int y = new Random().nextInt(9);

            position.add(0, x);
            position.add(1, y);

            if (clues[x][y] == 0 && board[x][y] !=0) {
                pairPosition = findPairingOnX(position);
                if(pairPosition != position){
                    clues[pairPosition.get(0)][pairPosition.get(1)] = board[pairPosition.get(0)][pairPosition.get(1)];
                    clues[x][y] = board[x][y];
                    board[pairPosition.get(0)][pairPosition.get(1)] =0;
                    board[x][y]=0;
                }
            }

            numX--;
        }

        while(numY >0){
            int x = new Random().nextInt(9);
            int y = new Random().nextInt(9);

            position.add(0, x);
            position.add(1, y);

            if (clues[x][y] == 0 && board[x][y] !=0) {
                pairPosition = findPairingOnY(position);
                if(pairPosition != position){
                    clues[pairPosition.get(0)][pairPosition.get(1)] = board[pairPosition.get(0)][pairPosition.get(1)];
                    clues[x][y] = board[x][y];
                    board[pairPosition.get(0)][pairPosition.get(1)] =0;
                    board[x][y]=0;
                }
            }

            numY--;
        }

    }

    /**
     * This method finds  a random value on the board, and then attempts to find a candidate line on both X and Y axis.
     * If during the loop it cannot find enough numbers to complete the setup for the given, it resets the values it had changed during that iteration.
     * @param num number of given clues wanted on the board, the max possible is 9 because 5 matching numbers are required to set up a given
     *
     */
    public void setGivens(int num){

        int numClues = 0;
        ArrayList<Integer> position = new ArrayList<>();
        ArrayList<Integer> xPair1;
        ArrayList<Integer> xPair2;
        ArrayList<Integer> yPair1;
        ArrayList<Integer> yPair2;

        while (numClues < num) {
            int x = new Random().nextInt(9);
            int y = new Random().nextInt(9);

            position.add(0, x);
            position.add(1, y);

            if (clues[x][y] == 0 && board[x][y] !=0) {

                xPair1 = findPairingOnX(position);
                if(xPair1 == position){
                    continue;
                }
                clues[xPair1.get(0)][xPair1.get(1)] = board[xPair1.get(0)][xPair1.get(1)];
                board[xPair1.get(0)][xPair1.get(1)] = 0;

                xPair2 = findPairingOnX(position);
                if(xPair2 == position){
                    board[xPair1.get(0)][xPair1.get(1)] = clues[xPair1.get(0)][xPair1.get(1)];
                    clues[xPair1.get(0)][xPair1.get(1)] =0;
                    continue;
                }
                clues[xPair2.get(0)][xPair2.get(1)] = board[xPair2.get(0)][xPair2.get(1)];
                board[xPair2.get(0)][xPair2.get(1)] = 0;


                yPair1 = findPairingOnY(position);
                if(yPair1 == position){

                    board[xPair2.get(0)][xPair2.get(1)] = clues[xPair2.get(0)][xPair2.get(1)];
                    board[xPair1.get(0)][xPair1.get(1)] = clues[xPair1.get(0)][xPair1.get(1)];

                    clues[xPair2.get(0)][xPair2.get(1)] =0;
                    clues[xPair1.get(0)][xPair1.get(1)] =0;
                    continue;
                }
                clues[yPair1.get(0)][yPair1.get(1)] = board[yPair1.get(0)][yPair1.get(1)];
                board[yPair1.get(0)][yPair1.get(1)] = 0;


                yPair2 = findPairingOnY(position);
                if(yPair2 == position){

                    board[yPair1.get(0)][yPair1.get(1)] = clues[yPair1.get(0)][yPair1.get(1)];
                    board[xPair2.get(0)][xPair2.get(1)] = clues[xPair2.get(0)][xPair2.get(1)];
                    board[xPair1.get(0)][xPair1.get(1)] = clues[xPair1.get(0)][xPair1.get(1)];

                    clues[yPair1.get(0)][yPair1.get(1)] =0;
                    clues[xPair2.get(0)][xPair2.get(1)] =0;
                    clues[xPair1.get(0)][xPair1.get(1)] =0;

                    continue;
                }
                clues[yPair2.get(0)][yPair2.get(1)] = board[yPair2.get(0)][yPair2.get(1)];
                board[yPair2.get(0)][yPair2.get(1)] = 0;


                numClues++;

            }
        }
    }

    public void setEasy() {
        setGivens(8);
        setCandidateLines(4,4);
        board = clues.clone();

    }

    public void setMedium(){
        setGivens(4);
        setCandidateLines(5,5);
        board = clues.clone();

    }

    public void setHard(){

        setGivens(9);
        setCandidateLines(9,9);

        int i = new Random().nextInt(9) +1;

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if(board[x][y] == i){
                    clues[x][y] = board[x][y];
                    board[x][y]=0;
                }
            }
        }


    }


    public void demoGiven(){
        setGivens(1);
        board = clues.clone();
    }


    public void demoCandidateLine(){
        setCandidateLines(1,0);
        board  = clues.clone();
    }


}
