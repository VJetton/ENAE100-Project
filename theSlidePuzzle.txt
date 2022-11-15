package slide_puzzle;

import java.util.*;

public class SlidePuzzle {
    
    private int n;
    private int inversions;
    private int layer;
    private int[][] puzzleBoard;
    private int[][] goalBoard;
    private ArrayList<Integer> puzzleElements;
    private int[][] neighbor1;
    private int[][] neighbor2;
    private int[][] neighbor3;
    private int[][] neighbor4;
    private ArrayList<int[][]> neighbors = new ArrayList<int[][]>();
    private SlidePuzzle parent;
    
    public SlidePuzzle() {}
    //initalizes n by n puzzle board, n by n neighbors, and n by n board for goal board referencing
    public SlidePuzzle(int q) {
        n = q;
        puzzleBoard = new int[n][n];
        goalBoard = new int[n][n];
        neighbor1 = new int[n][n];
        neighbor2 = new int[n][n];
        neighbor3 = new int[n][n];
        neighbor4 = new int[n][n];
        layer = 0;
        generatePuzzle();
        generateGoalBoard();
    }
    

    public SlidePuzzle(int[][] q, int r, SlidePuzzle z) {
	    n = q.length;
        parent = z;
        layer = r;
        puzzleBoard = new int[n][n];
        puzzleBoard = q;
        goalBoard = new int[n][n];
        neighbor1 = new int[n][n];
        neighbor2 = new int[n][n];
        neighbor3 = new int[n][n];
        neighbor4 = new int[n][n];
        generateGoalBoard();
        generateElements();
    }
    //creates a list in ascending order of the elements that will be present in the slide puzzle as well as the goal elements
    private void generateElements() {
        puzzleElements = new ArrayList<Integer>();
        for (int x = 0; x < n*n; x++) {
            puzzleElements.add(x);
        }
    }
    
    //generates goal board
    private void generateGoalBoard() {
    	int goalVal = 1;
    	for (int x = 0; x < n; x++) {
    		for (int y = 0; y < n; y++) {
    			goalBoard[x][y] = goalVal;
    			goalVal++;
    		}
    	}
    	goalBoard[n-1][n-1] = 0;
    }
    //randomly fills the puzzle board with the elements from puzzle elements array and generates goal board
    private void generatePuzzle() {
        generateElements();
    	int pos;
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                pos = (int) (Math.random()*puzzleElements.size());
                puzzleBoard[x][y] = puzzleElements.get(pos);
                puzzleElements.remove(pos);
            }
        }
        puzzleElements.clear();
    }
    
    //calculates number of inversions
    public int numInversions() {
        inversions = 0;
        for(int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if(puzzleBoard[x][y] != 0) {
                    puzzleElements.add(puzzleBoard[x][y]);
                }
            }
        }
        for (int x = 0; x < puzzleElements.size(); x++) {
            for (int y = x + 1; y < puzzleElements.size(); y++) {
                if(puzzleElements.get(x) > puzzleElements.get(y)) {
                    inversions++;
                }
            }
        }
        return inversions;
    }
    
    //uses number of inversions and board size to determine if the board is solvable
    public boolean isSolvable() {
        if(n % 2 == 1) {
            if (inversions % 2 == 0) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
        int sum = 0;
            for(int x = 0; x < n; x++) {
                for (int y = 0; y < n; y++) {
                    if (puzzleBoard[x][y] == 0) {
                        sum = inversions + x;
                    }
                }
            }
            if (sum % 2 == 1) {
                return false;
            }
            else {
                return true;
            }
        }
    }
    
    //calculates manhattan distance
    public int manhattanDistance() {
        int manhattan = 0;
            for (int x = 0; x < n; x++) {
                for (int y = 0; y < n; y++) {
                    if (puzzleBoard[x][y] != goalBoard[x][y]) {
                        for (int s = 0; s < n; s++) {
                            for (int t = 0; t < n; t++) {
                                if (goalBoard[s][t] == puzzleBoard[x][y]) {
                                    manhattan += Math.abs(x - s) + Math.abs(y - t);
                                }
                            }
                        }
                    }
                }
            }
        return manhattan;
    }
    
    //calculates hammingDistance
    private int hammingDistance(int[][] arr) {
        int hamming = 0;
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (arr[x][y] == goalBoard[x][y]) {
                	hamming++;
                }
            }
        }
        return hamming;
    }
    
    private void swap(int x1, int y1, int x2, int y2, int[][] arr) {
        int temp;
        temp = arr[x1][y1];
        arr[x1][y1] = arr[x2][y2];
        arr[x2][y2] = temp;
    }
    
    private void calculateNeighbors() {
        
        //puts copies of the current puzzleboard into potential neighboring boards
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                neighbor1[x][y] = puzzleBoard[x][y];
                neighbor2[x][y] = puzzleBoard[x][y];
                neighbor3[x][y] = puzzleBoard[x][y];
                neighbor4[x][y] = puzzleBoard[x][y];
            }
        }
        
        //traverses puzzleboard to find where the blanke elment is
        int blankXCoord = 0;
        int blankYCoord = 0;
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (puzzleBoard[x][y] == 0) {
                    blankXCoord = x;
                    blankYCoord = y;
                    break;
                }
            }
        }
        
        if (blankXCoord == 0 && blankYCoord == 0) {
            swap(0, 0, 0, 1, neighbor1);
            swap(0, 0, 1, 0, neighbor2);
            neighbors.add(neighbor1);
            neighbors.add(neighbor2);
        }
                
        else if (blankXCoord == n - 1 && blankYCoord == 0) {
            swap(n-1, 0, n-1, 1, neighbor1);
            swap(n-1, 0, n-2, 0, neighbor2);
            neighbors.add(neighbor1);
            neighbors.add(neighbor2);
        }
        
        else if (blankXCoord == 0 && blankYCoord == n - 1) {
            swap(0, n-1, 1, n-1, neighbor1);
            swap(0, n-1, 0, n-2, neighbor2);
            neighbors.add(neighbor1);
            neighbors.add(neighbor2);
        }
                
        else if (blankXCoord == n - 1 && blankYCoord == n - 1) {
            swap(n-1, n-1, n-1, n-2, neighbor1);
            swap(n-1, n-1, n-2, n-1, neighbor2);
            neighbors.add(neighbor1);
            neighbors.add(neighbor2);
        }
                
        //edge cases
        else if (blankXCoord == 0) {
            swap(0, blankYCoord, 0, blankYCoord - 1, neighbor1);
            swap(0, blankYCoord, 0, blankYCoord + 1, neighbor2);
            swap(0, blankYCoord, 1, blankYCoord, neighbor3);
            neighbors.add(neighbor1);
            neighbors.add(neighbor2);
            neighbors.add(neighbor3);
        }
    
        else if (blankXCoord == n - 1) {
            swap(n - 1, blankYCoord, n - 1, blankYCoord - 1, neighbor1);
            swap(n - 1, blankYCoord, n - 1, blankYCoord + 1, neighbor2);
            swap(n - 1, blankYCoord, n - 2, blankYCoord, neighbor3);
            neighbors.add(neighbor1);
            neighbors.add(neighbor2);
            neighbors.add(neighbor3);
        }
    
        else if (blankYCoord == 0) {
            swap(blankXCoord, 0, blankXCoord - 1, 0, neighbor1);
            swap(blankXCoord, 0, blankXCoord + 1, 0, neighbor2);
            swap(blankXCoord, 0, blankXCoord, 1, neighbor3);
            neighbors.add(neighbor1);
            neighbors.add(neighbor2);
            neighbors.add(neighbor3);
        }
        else if (blankYCoord == n - 1) {
            swap(blankXCoord, n - 1, blankXCoord - 1, n - 1, neighbor1);
            swap(blankXCoord, n - 1, blankXCoord + 1, n - 1, neighbor2);
            swap(blankXCoord, n - 1, blankXCoord, n - 2, neighbor3);
            neighbors.add(neighbor1);
            neighbors.add(neighbor2);
            neighbors.add(neighbor3);
        }
        
        //middle case
        else {
            swap(blankXCoord, blankYCoord, blankXCoord - 1, blankYCoord, neighbor1);
            swap(blankXCoord, blankYCoord, blankXCoord + 1, blankYCoord, neighbor2);
            swap(blankXCoord, blankYCoord, blankXCoord, blankYCoord - 1, neighbor3);
            swap(blankXCoord, blankYCoord, blankXCoord, blankYCoord + 1, neighbor4);
            neighbors.add(neighbor1);
            neighbors.add(neighbor2);
            neighbors.add(neighbor3);
            neighbors.add(neighbor4);
        }
    }
    
    //returns arraylist of neighboring boards for use in solver class    
    public ArrayList<int[][]> getNeighbors() {
        calculateNeighbors();
        return neighbors;
    }

    //determines if board is in solved state
    public boolean isSolved() {
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (puzzleBoard[x][y] != goalBoard[x][y]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    //determines if the elements of two boards are identical. true for yes, false for no
    public boolean compareTo(int[][] other) {
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (puzzleBoard[x][y] != other[x][y]) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getSize() {
        return n;
    }
    
    public int[][] getBoard() {
        return puzzleBoard;
    }
    
    public int getPriority() {
    	return layer + manhattanDistance();
    }
    
    public SlidePuzzle getParent() {
        return parent;
    }

    public int getLayer() {
        return layer;
    }

    public String getBoardString() {
        String out = "";
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                out += puzzleBoard[x][y] + " ";
            }
            out += "\n";
        }
        return out;
    }
}