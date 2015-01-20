import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MyThread implements Runnable {

	private operations op = new operations();
	String f = new String();
	//String in = new String();
	String fMatch=new String();
	String sMatch=new String();

	public MyThread( String inputFile,String firstMatch,String secondMatch) {
		f = inputFile;
		fMatch=firstMatch;
		sMatch=secondMatch;
	}


	// first method
	public void run() {
		try {
			matching(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// second method
	private void matching(String inf) throws IOException {
		int[] matsize = new int[2];
		matsize = op.sizeMatrixFile(inf);
		int[][] boundaryMatrix = new int[matsize[0]][matsize[1]];
		boundaryMatrix = op.readMatrix(inf);
		/*
		 * Reding the boundary matrix is done. In the next step some
		 * contributive vectors are introduced.
		 */

		int[] faceSet = new int[boundaryMatrix.length];
		int[] rowPosition = new int[boundaryMatrix.length];
		int[] cofaceSet = new int[boundaryMatrix[0].length];
		int[] columnPosition = new int[boundaryMatrix[0].length];
		for (int i = 0; i < faceSet.length; i++) {
			faceSet[i] = 0;
			rowPosition[i] = i + 1;
		}
		for (int i = 0; i < cofaceSet.length; i++) {
			cofaceSet[i] = 0;
			columnPosition[i] = i + 1;
		}

		/*
		 * the body of algorithm starts here
		 */

		while ((boundaryMatrix.length > 0) && (boundaryMatrix[0].length > 0)) {
			while (op.findMin(op.numNonzerosColumn(boundaryMatrix)) > 1) {
				boundaryMatrix = op.deleteRow(boundaryMatrix, 0);
				rowPosition = op.deleteEntry(rowPosition, 0);
			}
			int colNum = op.findPosition(op.numNonzerosColumn(boundaryMatrix),
					1, 1);
			if (colNum != -1) {

				int[] column = op.pickupColumn(boundaryMatrix, colNum);

				int rowNum = op.findPosition(column, 1, 1);

				if (boundaryMatrix.length > 0 && boundaryMatrix[0].length > 0) {
					boundaryMatrix = op.deleteRow(boundaryMatrix, rowNum);
				}
				if (boundaryMatrix.length > 0 && boundaryMatrix[0].length > 0) {
					boundaryMatrix = op.deleteColumn(boundaryMatrix, colNum);

				}
				faceSet[rowPosition[rowNum] - 1] = columnPosition[colNum];
				cofaceSet[columnPosition[colNum] - 1] = rowPosition[rowNum];
				rowPosition = op.deleteEntry(rowPosition, rowNum);
				columnPosition = op.deleteEntry(columnPosition, colNum);
			} else {
				boundaryMatrix = op.deleteRow(boundaryMatrix, 0);
				rowPosition = op.deleteEntry(rowPosition, 0);
			}
		}

		PrintWriter outn1 = new PrintWriter(new FileWriter(fMatch,
				true));
		PrintWriter outn2 = new PrintWriter(new FileWriter(sMatch,
				true));
		for (int i = 0; i < faceSet.length; i++)
			outn1.print(-faceSet[i] + " ");
		outn1.println();
		for (int i = 0; i < cofaceSet.length; i++)
			outn2.print(-cofaceSet[i] + " ");
		outn2.println();
		outn1.close();
		outn2.close();
	}
}
