import java.util.*;
import java.io.*;

public class Strassen {

	// temporary crossover for now
	public static int crossover = 256;

	private static Matrix traditionalMultiply(Matrix a, Matrix b) {
		int n = a.dim;
		int[][] res = new int[n][n];

		// weird implementation to speed things up
		int temp;
		for (int k = 0; k < n; k++) {
			for (int i = 0; i < n; i++) {
				temp = a.rows[i][k];
				for (int j = 0; j < n; j++) {
					res[i][j] += temp * b.rows[k][j];
				} 
			}
		}
		return new Matrix(res);
	}

	// return A+B
	private static Matrix add(Matrix a, Matrix b) {
		int n = a.dim;
		// temporary array for now to speed up computation
		int[][] res = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				res[i][j] = a.rows[i][j] + b.rows[i][j];
			}
		}
		return new Matrix(res);
	}

	// return A-B
	private static Matrix subtract(Matrix a, Matrix b) {
		int n = a.dim;
		int[][] res = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				res[i][j] = a.rows[i][j] - b.rows[i][j];
			}
		}
		return new Matrix(res);
	}

	/*
		performs strassen algorithm for matrix multiplication
	*/
	public static Matrix strassenMultiply(Matrix a, Matrix b) {
		if (a.dim != b.dim)
			throw new IllegalArgumentException("strassenMultiply error: dimensions must be the same");
		int n = a.dim;
		if (n <= crossover)
			return traditionalMultiply(a, b);

		boolean isPadded = false;
		// if odd dim, need to pad
		if (n % 2 == 1) {
			a = pad(a);
			b = pad(b);

			n++;
			isPadded = true;
		}

		Matrix c = new Matrix(n);
		int half = n / 2;

		// store the submatrices to multiply here
		Matrix[] matrices = new Matrix[8];
		
		// extract the submatrices
		for (int i = 0; i < 4; i++) {
			matrices[i] = extractMatrix(a, i);
			matrices[i+4] = extractMatrix(b, i);
		}

		// compute the products as in strassen
		// BASH them now
		// TODO can also reallocate memory here to save if needed
		Matrix[] products = new Matrix[7];
		products[0] = strassenMultiply(matrices[0], subtract(matrices[5], matrices[7]));
		products[1] = strassenMultiply(add(matrices[0], matrices[1]), matrices[7]);
		products[2] = strassenMultiply(add(matrices[2], matrices[3]), matrices[4]);
		products[3] = strassenMultiply(matrices[3], subtract(matrices[6], matrices[4]));
		products[4] = strassenMultiply(add(matrices[0], matrices[3]), add(matrices[4], matrices[7]));
		products[5] = strassenMultiply(subtract(matrices[1], matrices[3]), add(matrices[6], matrices[7]));
		products[6] = strassenMultiply(subtract(matrices[0], matrices[2]), add(matrices[4], matrices[5]));

		// store the submatrices to concatenate
		// TODO can reallocate memory to save if needed
		Matrix[] subresults = new Matrix[4];
		subresults[0] = add(add(products[4], products[3]), subtract(products[5], products[1]));
		subresults[1] = add(products[0], products[1]);
		subresults[2] = add(products[2], products[3]);
		subresults[3] = subtract(add(products[4], products[0]), add(products[2], products[6]));

		// if not padded, just concatenate
		if (!isPadded) {
			int[][] res = new int[n][n];

			// fill in
			for (int i = 0; i < half; i++) {
				for (int j = 0; j < half; j++) {
					res[i][j] = subresults[0].rows[i][j];
					res[i][half + j] = subresults[1].rows[i][j];
					res[half + i][j] = subresults[2].rows[i][j];
					res[half + i][half + j] = subresults[3].rows[i][j];
				}
			}
			return new Matrix(res);
		}

		// otherwise, need to insert cleverly
		// return size n-1
		int[][] res = new int[n-1][n-1];

		// fill in top
		for (int i = 0; i < half; i++) {
			// fill in upper left
			for (int j = 0; j < half; j++) {
				res[i][j] = subresults[0].rows[i][j];
			}
			// fill in upper right
			// only until half-1 since we remove final col of 0's
			for (int j = 0; j < half -1; j++) {
				res[i][half + j] = subresults[1].rows[i][j];
			}
		}

		// now we fill in the bottom
		for (int i = 0; i < half -1; i++) {
			// fill in lower left
			for (int j = 0; j < half; j++) {
				res[half + i][j] = subresults[2].rows[i][j];
			}
			// fill in lower right
			for (int j = 0; j < half -1; j++) {
				res[half + i][half + j] = subresults[3].rows[i][j];
			}
		}

		return new Matrix(res);
	}

	/* 
		private method which will be specialized later
		extract submatrix from a matrix a
		upper left corner is (startRow, startCol), size is size of submatrix
	*/
	private static Matrix extractMatrix(Matrix a, int startRow, int startCol, int size) {
		Matrix res = new Matrix(size);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				res.rows[i][j] = a.rows[startRow + i][startCol + j];
			}
		}
		return res;
	}

	/* 
		public method to extract submatrix
		type 0-3 is upperleft, upperright, lowerleft, lowerright of half * half submatrix
		only works if dim a is even
	*/
	public static Matrix extractMatrix(Matrix a, int type) {
		if (a.dim % 2 != 0) {
			throw new IllegalArgumentException("extractMatrix error: dim A must be even");
		}
		int half = a.dim/2;
		if (type == 0) {
			// extract upper left
			return extractMatrix(a, 0, 0, half);
		} else if (type == 1) {
			// extract upper right
			return extractMatrix(a, 0, half, half);
		} else if (type == 2) {
			// extract lower left
			return extractMatrix(a, half, 0, half);
		} 
		// else extract lower right
		return extractMatrix(a, half, half, half);
	}

	private static Matrix pad(Matrix a) {
		int n = a.dim;
		int[][] res = new int[n+1][n+1];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				res[i][j] = a.rows[i][j];
			}
		}
		return new Matrix(res);
	}

	public static void main(String[] args) {
		int n;
		String src;

		if (args.length == 3) {
			n = Integer.parseInt(args[1]);
			src = args[2];

			try {
				Scanner console = new Scanner(new File(src));
				// create first matrix
				int[][] temp1 = new int[n][n];
				int[][] temp2 = new int[n][n];

				for (int i = 0; i < n; i++) {
					for (int j = 0; j < n; j++) {
						temp1[i][j] = console.nextInt();
					}
				}

				// create next matrix
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < n; j++) {
						temp2[i][j] = console.nextInt();
					}
				}

				Matrix a = new Matrix(temp1);
				Matrix b = new Matrix(temp2);

				Matrix c = strassenMultiply(a, b);

				for (int i = 0; i < c.dim; i++) {
					System.out.println(c.rows[i][i]);
				}

			} catch (FileNotFoundException e) {
				System.out.println("Matrix initFromFile: file not found.");
				e.printStackTrace();
			} 

		} else {
			System.out.println("Incorrect format.");
			System.out.println("args has length " + args.length + ", expected 3");
			System.exit(1);
		}

		
		// simulate number of triangles in graph
		// int numtrials = 100;
		// double[] probs = {0.05};
		// int size = 1024;
		// for (double p : probs) {
		// 	double average = 0;
		// 	for (int trial = 0; trial < numtrials; trial++) {
		// 		Random rand = new Random(System.currentTimeMillis());
		// 		int[][] temp = new int[size][size];

		// 		// generate graph with Bern(p)
		// 		for (int i = 0; i < size; i++) {
		// 			for (int j = 0; j < i; j++) {
		// 				// Bern(p) if not in diag
		// 				if (rand.nextDouble() < p) {
		// 					// generate
		// 					temp[i][j] = 1;
		// 					temp[j][i] = 1;
		// 				}
		// 			}
		// 		}

		// 		Matrix adj = new Matrix(temp);
		// 		// adj.display();
		// 		Matrix prod = strassenMultiply(adj, adj);
		// 		Matrix resMatrix = strassenMultiply(prod, adj);
		// 		// resMatrix.display();

		// 		// extract by summing diagonals and divide by 6
		// 		int res = 0;
		// 		for (int i = 0; i < size; i++) {
		// 			res += resMatrix.rows[i][i];
		// 		}
		// 		res /= 6;
		// 		average += res;
		// 	}
		// 	average /= numtrials;
		// 	System.out.println(average + " triangles for p = " + p);
		// }
		

		/* 
		checking random monke
		int temp = 0;
		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i < 1000; i++) {
			if (rand.nextDouble() < 0.01) {
				temp++;
			}
		}
		System.out.println((double)temp/1000);
		*/		

		/*
		MONKE DEBUGGING

		int[][] rows1 = {{17,24,1,8,15},{23,5,7,14,16},{4,6,13,20,22},{10,12,19,21,3},{11,18,25,2,9}};
        int[][] rows2 = {{17,23,4,10,11},{24,5,6,12,18},{1,7,13,19,25},{8,14,20,21,2},{15,16,22,3,9}};
        Matrix m1 = new Matrix(rows1);
        Matrix m2 = new Matrix(rows2);

        // test
        Matrix traditional = traditionalMultiply(m1, m2);
        Matrix strassen = strassenMultiply(m1, m2);

        traditional.display();
        System.out.println();
        strassen.display();

        */
	}
}