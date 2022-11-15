package slide_puzzle;

public class Main {
	public static void main(String[] args) {
		//int[][] array = {{1, 2, 3},{5, 7, 6},{4, 0, 8}};
		//SlidePuzzle one = new SlidePuzzle(array, 0, null);
		SlidePuzzle one = new SlidePuzzle(3);
		System.out.println(one.getBoardString());
		System.out.println("Inversions: " + one.numInversions());
		System.out.println("Solvable: " + one.isSolvable());
		if (one.isSolvable()) {
			Solver two = new Solver();
			two.solve(one);
			System.out.println(two.getBestPathString());
			System.out.println("Moves taken to solve: " + two.getNumMoves());
			two.reInitialize();
		}
		
		/*int totalMoves = 0;
		SlidePuzzle one;
		Solver two = new Solver();
		for (int x = 0; x < 100; x++) {
			one = new SlidePuzzle(3);
			if (one.isSolvable()) {
				two.solve(one);
				totalMoves += two.getNumMoves();
				two.reInitialize();
			}
			else {
			}
		}
		System.out.println("" + totalMoves);*/
		
	}
}
