import java.util.*;
import java.lang.Math.*;
import java.io.*;

public class RandomizedAlgos {
	public static final int MAX_ITER = 25000;
	public static final Random random = new Random(System.currentTimeMillis());

	// random number generator from 1 to 10^12
	public static long randomNumber() {
	    char[] digits = new char[12];
	    for (int i = 0; i < 12; i++) {
	        digits[i] = (char) (random.nextInt(10) + '0');
	    }
	    return Long.parseLong(new String(digits)) +1;
	}

	public static long[] randomArr(int size) {
		long[] res = new long[size];
		for (int i = 0; i < size; i++) {
			res[i] = randomNumber();
		}
		return res;
	}

	// KK algo
	public static long KK(long[] arr) {
		int n = arr.length;
		MaxHeap heap = new MaxHeap(arr);

		for (int i = 0; i < n-1; i++) {
			long a = heap.pop();
			long b = heap.pop();
			long diff = a-b;
			heap.push(diff);
		}

		return heap.peek();
	}

	// repeated random algorithm
	public static long repeatedRandom(long[] arr, int kind) {
		int n = arr.length;
		int[] sol = generateRandomSol(n, kind);
		long bestResidue = getResidue(sol, arr, kind);
		for (int i = 0; i < MAX_ITER; i++) {
			int[] tempSol = generateRandomSol(n, kind);
			long tempResidue = getResidue(tempSol, arr, kind);
			if (tempResidue < bestResidue) {
				bestResidue = tempResidue;
				sol = tempSol.clone();
			}
		}
		return bestResidue;
	}

	public static long hillClimbing(long[] arr, int kind) {
		int n = arr.length;
		int[] sol = generateRandomSol(n, kind);
		long bestResidue = getResidue(sol, arr, kind);

		for (int i = 0; i < MAX_ITER; i++) {
			int[] tempSol = randomNeighbor(sol, kind);
			long tempResidue = getResidue(tempSol, arr, kind);
			if (tempResidue < bestResidue) {
				bestResidue = tempResidue;
				sol = tempSol.clone();
			}
		}
		return bestResidue;
	}

	public static long simulatedAnnealing(long[] arr, int kind) {
		int n = arr.length;
		int[] sol1 = generateRandomSol(n, kind);
		int[] sol3 = sol1.clone();
		long residue1 = getResidue(sol1, arr, kind);
		long residue3 = getResidue(sol3, arr, kind);

		for (int i = 0; i < MAX_ITER; i++) {
			int[] sol2 = randomNeighbor(sol1, kind);
			long residue2 = getResidue(sol2, arr, kind);
			if (residue2 < residue1) {
				sol1 = sol2.clone();
				residue1 = residue2;
			} else {
				double p = Math.exp(-(residue2-residue1)/t(i));
				if (random.nextDouble() < p) {
					sol1 = sol2.clone();
					residue1 = residue2;
				}
			} 

			if (residue1 < residue3) {
				sol3 = sol1.clone();
				residue3 = residue1;
			}
		}

		return residue3;
	}




	// HELPER FUNCTIONS START HERE
	// if kind = 0, using S
	// otherwise, using P
	private static int[] generateRandomSol(int size, int kind) {
		int[] res = new int[size];
		if (kind == 0) {
			for (int i = 0; i < size; i++) {
				res[i] = random.nextBoolean() ? 1 : -1;
			}
			return res;
		}

		for (int i = 0; i < size; i++) {
			res[i] = random.nextInt(size);
		}
		return res;
	}

	// find residue of S or P
	// kind same as above
	private static long getResidue(int[] sol, long[] arr, int kind) {
		int n = sol.length;
		if (kind == 0) {
			long res = 0;
			for (int i = 0; i < n; i++) {
				if (sol[i] == 1)
					res += arr[i];
				else
					res -= arr[i];
			}
			return Math.abs(res);
		}

		long[] arrNew = new long[n];
		for (int i = 0; i < n; i++) {
			arrNew[sol[i]] += arr[i];
		}
		return KK(arrNew);
	}

	// generate random neighbor of S, P
	// kind same as above
	private static int[] randomNeighbor(int[] sol, int kind) {
		int n = sol.length;
		int[] sol2 = sol.clone();
		if (kind == 0) {
			int i = random.nextInt(n);
			int j;
			do {
				j = random.nextInt(n);
			} while (j == i);
			sol2[i] = -sol2[i];
			if (random.nextDouble() < 0.5)
				sol2[j] = -sol2[j];

			return sol2;
		}

		int i = random.nextInt(n);
		int j;
		do {
			j = random.nextInt(n);
		} while (j == sol2[i]);
		sol2[i] = j;
		return sol2;
	}

	private static double raise(double a, int n) {
		if (a == 1 || n == 0)
			return 1;
		int half = n/2;
		if (n % 2 == 0)
			return raise(a, half) * raise(a, half);
		return raise(a, half) * raise(a, half) * a;

	}

	private static double t(int iter) {
		double temp = raise(10, 10);
		double temp2 = raise(0.8, iter/300);
		return temp * temp2;
	}

	public static void main(String[] args) throws FileNotFoundException {

		long[] arr = {233743058398L, 530162287063L, 567798971615L, 964941064206L, 889057821892L, 34610904592L};
		int[] sol = {1,1,2,3,2};
		System.out.println(getResidue(sol, arr, 1));

		// long[] arr = randomArr(100);
		// PrintStream outfile = new PrintStream(new File("test.txt"));

		// outfile.println(Arrays.toString(arr));
		// System.out.println(KK(arr));
		// System.out.println(repeatedRandom(arr, 0));
		// System.out.println(repeatedRandom(arr, 1));
		// System.out.println(hillClimbing(arr, 0));
		// System.out.println(hillClimbing(arr, 1));
		// System.out.println(simulatedAnnealing(arr, 0));
		// System.out.println(simulatedAnnealing(arr, 1));


		// int n = 5;
		// int[] sol = generateRandomSol(n, 1);
		// System.out.println(Arrays.toString(sol));
		// System.out.println();

		// for (int i = 0; i < 50; i++) {
		// 	sol = randomNeighbor(sol, 1);
		// 	System.out.println(Arrays.toString(sol));
		// 	System.out.println();
		// }


		// PrintStream kkOut = new PrintStream(new File("KKResult.txt"));
		// PrintStream rr0Out = new PrintStream(new File("RR0Result.txt"));
		// PrintStream rr1Out = new PrintStream(new File("RR1Result.txt"));
		// PrintStream hc0Out = new PrintStream(new File("HC0Result.txt"));
		// PrintStream hc1Out = new PrintStream(new File("HC1Result.txt"));
		// PrintStream sa0Out = new PrintStream(new File("SA0Result.txt"));
		// PrintStream sa1Out = new PrintStream(new File("SA1Result.txt"));
		// int reps = 100;

		// kkOut.println("score,time");
		// rr0Out.println("score,time");
		// rr1Out.println("score,time");
		// hc0Out.println("score,time");
		// hc1Out.println("score,time");
		// sa0Out.println("score,time");
		// sa1Out.println("score,time");

		// for (int rep = 0; rep < reps; rep++) {
		// 	long[] arr = randomArr(100);
		// 	// KK
		// 	long start = System.currentTimeMillis();
		// 	kkOut.print(KK(arr));
		// 	long end = System.currentTimeMillis();
		// 	kkOut.println("," + (end-start));

		// 	// repeated random 0
		// 	start = System.currentTimeMillis();
		// 	rr0Out.print(repeatedRandom(arr, 0));
		// 	end = System.currentTimeMillis();
		// 	rr0Out.println("," + (end-start));

		// 	// repeated random 1
		// 	start = System.currentTimeMillis();
		// 	rr1Out.print(repeatedRandom(arr, 1));
		// 	end = System.currentTimeMillis();
		// 	rr1Out.println("," + (end-start));

		// 	// hill climbing 0
		// 	start = System.currentTimeMillis();
		// 	hc0Out.print(hillClimbing(arr, 0));
		// 	end = System.currentTimeMillis();
		// 	hc0Out.println("," + (end-start));

		// 	// hill climbing 1
		// 	start = System.currentTimeMillis();
		// 	hc1Out.print(hillClimbing(arr, 1));
		// 	end = System.currentTimeMillis();
		// 	hc1Out.println("," + (end-start));

		// 	// sim annealing 0
		// 	start = System.currentTimeMillis();
		// 	sa0Out.print(simulatedAnnealing(arr, 0));
		// 	end = System.currentTimeMillis();
		// 	sa0Out.println("," + (end-start));

		// 	// sim annealing 1
		// 	start = System.currentTimeMillis();
		// 	sa1Out.print(simulatedAnnealing(arr, 1));
		// 	end = System.currentTimeMillis();
		// 	sa1Out.println("," + (end-start));
		// }
	}
}