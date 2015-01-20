import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MorseMatching {

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String[] args) throws InterruptedException,
			IOException {
		System.out.println("Read the name of boundary files from "+args[0]);
		operations op = new operations();
		
		int num=op.sizeMatrixFile(args[0])[0];
		String[] inputFiles = new String[num];
		String[] matchVectors = new String[2 * num];
		Scanner files = new Scanner(new File(args[0]));
		
		for(int i=0;i<num;i++){
			inputFiles[i]=files.nextLine();
		}
		files.close();

		matchVectors[0] = "N0.txt";
		matchVectors[2 * num - 1] = "N" + num + "_neg.txt";

		for (int i = 1; i < num; i++) {
			matchVectors[2 * i - 1] = "N" + i + "_neg.txt";
			matchVectors[2 * i] = "N" + i + "_pos.txt";
		}
		
		for(int i=0;i<matchVectors.length;i++){
			File fileTemp=new File(matchVectors[i]);
			if(fileTemp.exists()){
				fileTemp.delete();
			}
		}

		for(int i=0;i<num+1;i++){
			File fileTemp=new File("N"+i+".txt");
			if(fileTemp.exists()){
				fileTemp.delete();
			}
		}

		ExecutorService executor = Executors.newFixedThreadPool(num);

		for (int i = 0; i < num; i++) {
			MyThread th = new MyThread(inputFiles[i], matchVectors[2 * i], matchVectors[2 * i + 1]);
			executor.execute(th);
		}
		executor.shutdown();

		// wait till all thread get finished
		while (!executor.isTerminated()) {// wait here in the loop
		}
		System.out.println("Threads finished!");
		/*
		 * Here the sequential part of the algorithm starts
		 */
		

		/*
		 * for 1-simplices no double matching control is not needed.
		 */
		for (int i = 1; i < 2; i++) {
			PrintWriter outn = new PrintWriter(new FileWriter("N" + i + ".txt",
					true));
			int nrCritical = 0;
			int[] size_neg = new int[2];
			size_neg = op.sizeMatrixFile(matchVectors[2 * i - 1]);
			int[][] boundMat_neg = new int[size_neg[0]][size_neg[1]];
			Scanner scan = new Scanner(new File(matchVectors[2 * i - 1]));
			for (int k = 0; k < boundMat_neg.length; k++) {
				for (int j = 0; j < boundMat_neg[0].length; j++) {
					boundMat_neg[k][j] = scan.nextInt();
				}
			}
			scan.close();

			int[] size_pos = new int[2];
			size_pos = op.sizeMatrixFile(matchVectors[2 * i]);
			int[][] boundMat_pos = new int[size_pos[0]][size_pos[1]];
			Scanner scan2 = new Scanner(new File(matchVectors[2 * i]));
			for (int k = 0; k < boundMat_pos.length; k++) {
				for (int j = 0; j < boundMat_pos[0].length; j++) {
					boundMat_pos[k][j] = scan2.nextInt();
				}
			}
			scan2.close();

			for (int j = 0; j < size_pos[1]; j++) {
				outn.print(-boundMat_neg[0][j] + boundMat_pos[0][j] + " ");
				if (boundMat_neg[0][j] == 0 && boundMat_pos[0][j] == 0)
					nrCritical++;
			}
			outn.println();
			outn.close();
			System.out.println("criticals=" + nrCritical);
		}

		// the list defined below is used for double matching
		List<Integer> list = new ArrayList<Integer>();

		for (int i = 2; i < num; i++) {
			PrintWriter outn = new PrintWriter(new FileWriter("N" + i + ".txt",
					true));
			int nrCritical = 0;
			int[] size_neg = new int[2];
			size_neg = op.sizeMatrixFile(matchVectors[2 * i - 1]);
			int[][] boundMat_neg = new int[size_neg[0]][size_neg[1]];
			boundMat_neg = op.readMatrix(matchVectors[2 * i - 1]);

			Scanner scan = new Scanner(new File(matchVectors[2 * i - 1]));
			for (int k = 0; k < boundMat_neg.length; k++) {
				for (int j = 0; j < boundMat_neg[0].length; j++) {
					boundMat_neg[k][j] = scan.nextInt();
				}
			}
			scan.close();

			int[] size_pos = new int[2];
			size_pos = op.sizeMatrixFile(matchVectors[2 * i]);
			int[][] boundMat_pos = new int[size_pos[0]][size_pos[1]];
			boundMat_pos = op.readMatrix(matchVectors[2 * i]);

			Scanner scan2 = new Scanner(new File(matchVectors[2 * i]));
			for (int k = 0; k < boundMat_pos.length; k++) {
				for (int j = 0; j < boundMat_pos[0].length; j++) {
					boundMat_pos[k][j] = scan2.nextInt();
				}
			}
			scan2.close();

			if (!list.isEmpty()) {
				for (int j = 0; j < list.size(); j++)
					boundMat_neg[0][list.get(j) - 1] = 0;
				list.clear();
			}

			for (int j = 0; j < size_neg[1]; j++) {
				if (boundMat_neg[0][j] * boundMat_pos[0][j] != 0) {
					// System.out.println("confusing number"+boundMat[1][j]);
					list.add(-boundMat_pos[0][j]);
					boundMat_pos[0][j] = 0;
				}
				outn.print((-boundMat_neg[0][j] + boundMat_pos[0][j]) + " ");
				if (boundMat_neg[0][j] == 0 && boundMat_pos[0][j] == 0)
					nrCritical++;
			}
			System.out.println("criticals=" + nrCritical);

			outn.close();
		}

		for (int i = num; i < num + 1; i++) {

			PrintWriter outn = new PrintWriter(new FileWriter("N" + i + ".txt",
					true));
			int nrCritical = 0;
			int[] size_neg = new int[2];
			size_neg = op.sizeMatrixFile(matchVectors[2 * i - 1]);
			int[][] boundMat_neg = new int[size_neg[0]][size_neg[1]];
			Scanner scan = new Scanner(new File(matchVectors[2 * i - 1]));
			for (int k = 0; k < boundMat_neg.length; k++) {
				for (int j = 0; j < boundMat_neg[0].length; j++) {
					boundMat_neg[k][j] = scan.nextInt();
				}
			}
			scan.close();

			if (!list.isEmpty()) {
				for (int j = 0; j < list.size(); j++)
					boundMat_neg[0][list.get(j) - 1] = 0;
				list.clear();
			}

			for (int j = 0; j < size_neg[1]; j++) {
				outn.print(-boundMat_neg[0][j] + " ");
				if (boundMat_neg[0][j] == 0)
					nrCritical++;
			}
			System.out.println("criticals=" + nrCritical);
			outn.close();
		}
	}
}
