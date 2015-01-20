

import java.io.File;
import java.io.FileNotFoundException;
//import java.io.IOException;
import java.util.Scanner;

public class operations {
	int[] sizeMatrixFile(String fileName) throws FileNotFoundException {
		int[] size = new int[] { 0, 0 };
		Scanner scan = new Scanner(new File(fileName));
		while (scan.hasNext()) {
			size[1]++;
			scan.next();
		}
		scan.close();
		Scanner scan1 = new Scanner(new File(fileName));
		while (scan1.hasNextLine()) {
			size[0]++;
			scan1.nextLine();
		}
		scan1.close();
		size[1] /= size[0];
		return size;
	}

	int[][] readMatrix(String filename) throws FileNotFoundException {
		int[] size=new int[2];
		size=sizeMatrixFile(filename);
		int[][] mat = new int[size[0]][size[1]];
		Scanner scan = new Scanner(new File(filename));
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				mat[i][j] = (int)scan.nextDouble();
				if(mat[i][j]<0){
					mat[i][j]*=-1;
				}
			}
		}
		scan.close();
		return mat;
	}
	
	int[][] readSparseMatrix(String filename,int[] size) throws FileNotFoundException {
		int[][] mat = new int[size[0]][size[1]];
		Scanner scan = new Scanner(new File(filename));
		for (int i = 0; i < size[0]; i++) {
			for (int j = 0; j < size[1]; j++) {
				int var = scan.nextInt();
				mat[var][j] = 1;
				if (mat[var][j] < 0)
					mat[var][j] *= -1;
			}
		}
		scan.close();
	    return mat;
	}

	int[][] deleteRow(int[][] matrix, int row) {
		int[][] deletedMatrix = new int[matrix.length - 1][matrix[0].length];
		for (int j = 0; j < matrix[0].length; j++) {
			for (int i = 0; i < row; i++) {
				deletedMatrix[i][j] = matrix[i][j];
			}
		}
		for (int j = 0; j < matrix[0].length; j++) {
			for (int i = row; i < matrix.length - 1; i++) {
				deletedMatrix[i][j] = matrix[i + 1][j];
			}
		}
		return deletedMatrix;
	}

	int[][] deleteColumn(int[][] matrix, int column) {
		int[][] deletedMatrix = new int[matrix.length][matrix[0].length - 1];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < column; j++) {
				deletedMatrix[i][j] = matrix[i][j];
			}
		}
		for (int i = 0; i < matrix.length; i++) {
			for (int j = column; j < matrix[0].length - 1; j++) {
				deletedMatrix[i][j] = matrix[i][j + 1];
			}
		}
		return deletedMatrix;
	}

	int[] deleteEntry(int[] vector, int index) {
		int[] deletedVector = new int[vector.length - 1];
		for (int i = 0; i < index; i++) {
			deletedVector[i] = vector[i];
		}
		for (int i = index; i < vector.length - 1; i++) {
			deletedVector[i] = vector[i + 1];
		}
		return deletedVector;
	}

	int numNonzerosColumn(int[][] matrix, int column) {
		int count = 0;
		for (int i = 0; i < matrix.length; i++) {
			if (matrix[i][column] != 0) {
				count++;
			}
		}
		return count;
	}

	int[] numNonzerosColumn(int[][] matrix) {
		int[] numNonzeros = new int[matrix[0].length];
		for (int i = 0; i < numNonzeros.length; i++) {
			numNonzeros[i] = numNonzerosColumn(matrix, i);
		}
		return numNonzeros;
	}

	int numNonzerosRow(int[][] matrix, int row) {
		int count = 0;
		for (int j = 0; j < matrix[0].length; j++) {
			if (matrix[row][j] != 0) {
				count++;
			}
		}
		return count;
	}

	int findMin(int[] matrix) {
		int min = matrix[0];
		for (int i = 1; i < matrix.length; i++) {
			if (matrix[i] < min) {
				min = matrix[i];
			}
		}
		return min;
	}

	int findMin(int[][] matrix) {
		int min = matrix[0][0];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] < min) {
					min = matrix[i][j];
				}
			}
		}
		return min;
	}

	/*
	 * this method finds the index of count-th number in vector and if it
	 * doesn't find anything, it will return -1 for example
	 * vector={1,3,4,1,5,6,1} findPosition(vector,1,2)=3 because the second 1
	 * has index 3 or findPosition(vector,1,3)=6
	 */
	int findPosition(int[] vector, int number, int count) {
		int position = 0;
		for (int i = 0; i < vector.length; i++) {
			if (vector[i] == number) {
				position++;
				if (position == count) {
					return i;
				}
			}
		}
		return -1;
	}

	int[] pickupColumn(int[][] matrix, int number) {
		int[] col = new int[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			col[i] = matrix[i][number];
		}
		return col;
	}
}
