package slide_puzzle;

import java.util.*;

public class Solver {

    private ArrayList<int[][]> currentNeighbors = new ArrayList<int[][]>();
    private ArrayList<SlidePuzzle> parents = new ArrayList<SlidePuzzle>();
    private ArrayList<SlidePuzzle> currentParents = new ArrayList<SlidePuzzle>();
    private int puzzleSize;
    private int numMoves;

    public Solver() {}

    public void reInitialize() {
    	currentNeighbors.clear();
    	parents.clear();
    	currentParents.clear();
    	puzzleSize = 0;
    	numMoves = 0;
    }
    public boolean isGameSolved() {
    	for (int x = 0; x < parents.size(); x++) {
            if (parents.get(x).isSolved()) {
                return true;
            }
        }
    	for (int x = 0; x < currentParents.size(); x++) {
            if (currentParents.get(x).isSolved()) {
                return true;
            }
        }
        return false;
    }

    public boolean inParents(int[][] board) {
        for (int x = 0; x < parents.size(); x++) {
            if (parents.get(x).compareTo(board)) {
                return true;
            }
        }
        return false;
    }

    public void solve(SlidePuzzle q) {
    	currentParents.add(q);
    	puzzleSize = q.getSize();
    	if (currentParents.get(0).isSolvable()) {
    		while (!isGameSolved()) {

                //selects the lowest priority current parent
            	int currentPos = 0;
                for (int x = 0; x < currentParents.size(); x++) {
                    if (currentParents.get(x).getPriority() < currentParents.get(currentPos).getPriority()) {
                    	currentPos = x;
                    }
                }
                
                //removes the priorityNeighbor from the currentParents tree and adds it to the parents tree and generates its neighbors
                    parents.add(currentParents.get(currentPos));
                    currentNeighbors.addAll(currentParents.get(currentPos).getNeighbors());
                    currentParents.remove(currentPos);
                
                //generates the neighboring boards for the priority neighbor
                    
                //removes neighbor boards that are in the parent tree and adds the rest to the currentParents tree
                while (currentNeighbors.size() > 0) {
                    if (inParents(currentNeighbors.get(0))) {
                        currentNeighbors.remove(0);
                    }
                    else {
                        currentParents.add(new SlidePuzzle(currentNeighbors.get(0), parents.get(parents.size() - 1).getLayer() + 1, parents.get(parents.size() - 1)));
                        currentNeighbors.remove(0);
                        //System.out.println(currentParents.get(currentParents.size() - 1).getBoardString() + "Layer: " + currentParents.get(currentParents.size() - 1).getLayer() + "\n");
                        if (currentParents.get(currentParents.size() - 1).isSolved()) {
                        	parents.add(currentParents.get(currentParents.size() - 1));
                        	//numMoves = currentParents.get(currentParents.size() - 1).getLayer();
                        }
                    }
                }
            }
    	}
    	else {
    		System.out.println("Not Solvable.");
    	}
        
    }

    public String printTree() {
    	String out = "";
    	for (int z = 0; z < parents.size(); z++) {
    		for (int x = 0; x < puzzleSize; x++) {
        		for (int y = 0; y < puzzleSize; y++) {
        			out += parents.get(z).getBoard()[x][y] + " ";
        		}
        		out += "\n";
        	}
    		out += "\n";
    	}
    	return out;
    }
    
    public ArrayList<SlidePuzzle> getBestPath() {
        ArrayList<SlidePuzzle> bestPath = new ArrayList<SlidePuzzle>();
        numMoves = 0;
        SlidePuzzle currentPuzzle = new SlidePuzzle();
        for (int x = 0; x < parents.size(); x++) {
            if (parents.get(x).isSolved()) {
                currentPuzzle = parents.get(x);
                numMoves = currentPuzzle.getLayer();
                break;
            }
        }
        for (int x = 0; x < numMoves + 1; x++) {
            bestPath.add(currentPuzzle);
            if (currentPuzzle.getParent() != null) {
                currentPuzzle = currentPuzzle.getParent();
            }
        }
        return bestPath;
    }

    public String getBestPathString() {
        String out = "";
        ArrayList<SlidePuzzle> bestPath = getBestPath();
        for (int x = bestPath.size() - 1; x >= 0; x--) {
            out += bestPath.get(x).getBoardString() + "\n";
        }
        return out;
    }
    
    public int getNumMoves() {
    	for (int x = 0; x < parents.size(); x++) {
    		if (parents.get(x).isSolved()) {
    			numMoves = parents.get(x).getLayer();
    		}
    	}
    	return numMoves;
    }

}