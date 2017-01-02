import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Mohammed on 2/12/2016.
 */
public class Solver extends JComponent{

    public int[][] grid = new int[9][9];

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g.setColor(Color.white);
        g.fillRect(0,0,500,500);
        g.setColor(Color.black);
        int s = 40;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                g.drawRect(10+s*i,10+s*j,s,s);
                g.setFont(new Font("Times New Roman",Font.PLAIN,s));
                g.drawString(String.valueOf(grid[j][i]),10+s*i,10+s*(j+1));
            }
        }
        g2.setStroke(new BasicStroke(3));

        for (int x=0;x<3;x++){
            for (int y= 0;y<3;y++){
                g2.drawRect(10+x*s*3,10+y*3*s,s*3,s*3);
            }
        }
    }

    public Solver(){

        try {
            Scanner reader = new Scanner(new File("C:\\Users\\Mohammed\\Dropbox\\Computer science\\Java\\SodukuSolver\\src\\board.txt"));
            for (int i = 0; i < 9; i++) {
                String line = reader.nextLine();
                for (int j = 0; j < 9; j++) {
                    grid[i][j] = Integer.valueOf(line.charAt(j))-48;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JFrame window = new JFrame("Solver");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(500,500);
        window.add(this);
        window.setVisible(true);
        repaint();
        recurse(grid,0,0);

    }


    /**
     * Recursive function for solving board
     * @param board 2D array representing board
     * @param x x-coordinate
     * @param y y-coordinate
     * @return true if board was solved, false if not
     */
    boolean recurse(int[][] board, int x, int y) {
        grid=board;
        repaint();

        //checking if board is complete
        boolean full = true;
        for (int row = 0; row < 9; row++){
            if (sum(board[row],9) != 45) {
                full = false;
                break;
            }
        }
        //If the board is full and valid then copy it to the global version and return true
        if (full) {
            for (int yy = 0; yy < 9; yy++) {
                for (int xx = 0; xx < 9; xx++) {
                    grid[yy][xx] = board[yy][xx];
                }
            }
            repaint();
            return true;
        }

        //if the current spot already has a number in it then find the next empty spot and recurse
        if ((board[y][x]) != 0) {
            int[] newCoords = nextSpot(x,y);
            return recurse(board, newCoords[0],newCoords[1]);
        }

        //Get all options for the current spot and try each of them
        int[] possibilities = getOptions(board,x, y);
        for (int i = 0; i < 10; i++){
            if (possibilities[i] == 0) continue;


            if (recurse(createNewBoard(board, possibilities[i], x,y), x,y)) { //If it works then stop checking
                return true;
            }
        }
        return false;
    }

    /**
     * Makes a copy of an array
     * @param master array to copy
     * @return copy of array provided
     */
    int[][] copyArray(int[][] master) {
        int[][] newBoard = new int[9][9];

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                newBoard[y][x] = master[y][x];
            }
        }
        return newBoard;
    }

    /**
     * Creates a copy of the board and assigns a new value to a particular locaiton
     * @param currentBoard board to copy
     * @param newValue value to place inside board
     * @param x x-coordinate to place value
     * @param y y-coordinate to place value
     * @return new board
     */
    int[][] createNewBoard(int[][] currentBoard, int newValue, int x,int y) {
        int[][] newBoard = copyArray(currentBoard);
        newBoard[y][x] = newValue;
        return newBoard;
    }

    /**
     * Finds the next spot to place a number
     * @param x current x-coord
     * @param y current y-coord
     * @return array of x,y coord of new spot
     */
    int[] nextSpot(int x, int y) {

        if (x == 8) {
            x = 0;
            y++;
        } else {
            x++;
        }
        int[] ret = { x,y };
        if (y > 8) {
            ret[0] = -1;
            ret[1] = -1;
        }
        return ret;
    }

    /**
     * Gets the other numbers in the current square
     * @param x x-coord of current spot
     * @param y y-coord of current spot
     * @return Array of number present in current square
     */
    int[] getSquareNumbers(int x, int y) {
        int[] returnArray= new int[9];
        int counter = 0;
        y = y/3;
        x = x/3;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                returnArray[counter] = (grid[y*3 + row][x*3 + col]);
                counter++;
            }

        }
        return returnArray;
    }

    /**
     * Determines which numbers are valid for the current location
     * @param board Current board
     * @param x x-coord
     * @param y y-coord
     * @return array of length 9, each non-zero element is a valid option
     */
    int[] getOptions(int[][] board, int x, int y) {
        int[] possibilites = {0,1,2,3,4,5,6,7,8,9};

        //Checking column
        for (int row = 0; row < 9; row++) {
            possibilites[board[row][x]] = 0;
        }

        //Checking row
        for (int col = 0; col < 9; col++) {
            possibilites[board[y][col]] = 0;
        }

        //checking current square
        int[] square = getSquareNumbers(x, y);
        for (int i = 0; i < 9; i++){
            possibilites[square[i]] = 0;
        }

        return possibilites;
    }

    /**
     * Calculates the sum of an array
     * @param arr int array to sum
     * @param length length of array
     * @return integer sum of array
     */
    int sum(int[] arr, int length) {
        int s = 0;
        for (int i = 0; i < length; i++) {
            s += arr[i];
        }
        return s;
    }
}
