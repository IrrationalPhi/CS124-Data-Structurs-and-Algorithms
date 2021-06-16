import java.util.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.Math.*;

public class Partition {
	public static final Random random = new Random();
	public static final int MAX_ITER = 25000;

	public static long KK(long[] arr) {
		int n = arr.length;
		MaxHeap heap = new MaxHeap(arr);

		for (int i = 0; i < n-1; i++) {
			long a = heap.pop();
			long b = heap.pop();
			heap.push(a-b);
		}

		return heap.peek();
	}

	public static long repeatedRandom(long[] arr, boolean isP) {
		Sol bestSol = isP ? new PSol(arr) : new SSol(arr);
		long bestResidue = bestSol.getResidue();

		for (int i = 0; i < MAX_ITER; i++) {
			Sol tempSol = isP ? new PSol(arr) : new SSol(arr);
			long tempResidue = tempSol.getResidue();

			if (tempResidue < bestResidue) {
				// bestSol = tempSol;
				bestResidue = tempResidue;
			}
		}

		return bestResidue;
	}

	public static long hillClimbing(long[] arr, boolean isP) {
		Sol bestSol = isP ? new PSol(arr) : new SSol(arr);
		long bestResidue = bestSol.getResidue();

		for (int i = 0; i < MAX_ITER; i++) {
			Sol tempSol = bestSol.randomNeighbor();
			long tempResidue = tempSol.getResidue();

			if (tempResidue < bestResidue) {
				bestSol = tempSol;
				bestResidue = tempResidue;
			}
		}
		return bestResidue;
	}

	public static long simulatedAnnealing(long[] arr, boolean isP) {
		Sol bestSol = isP ? new PSol(arr) : new SSol(arr);
		long bestResidue = bestSol.getResidue();
		Sol currentSol = bestSol;
		long currentResidue = bestResidue;

		for (int i = 0; i < MAX_ITER; i++) {
			Sol tempSol = currentSol.randomNeighbor();
			long tempResidue = tempSol.getResidue();

			if (tempResidue < currentResidue) {
				currentResidue = tempResidue;
				currentSol = tempSol;
			} else {
				double p = Math.exp(-(tempResidue - currentResidue)/t(i));
				if (random.nextDouble() < p) {
					currentSol = tempSol;
					currentResidue = tempResidue;
				}
			}

			if (currentResidue < bestResidue) {
				bestSol = currentSol;
				bestResidue = currentResidue;
			}
		}
		return bestResidue;
	}

	private static double t(int i) {
		double temp = Math.pow(10, 10);
		temp *= Math.pow(0.8, i/300);
		return temp;
	}

	public static long randomNumber() {
		return ThreadLocalRandom.current().nextLong(1000000000000L)+1;
	}

	public static long[] randomArr(int size) {
		// Random random = new Random(System.currentTimeMillis());
		long[] res = new long[size];
		for (int i = 0; i < size; i++) {
			res[i] = randomNumber();
		}
		return res;
	}

	public static void main(String[] args) throws FileNotFoundException {

		int flag = 0;
		int algo = 0;
		String filename = "";

		try {
			algo = Integer.parseInt(args[1]);
			filename = args[2];
		} catch (Exception e) {
			e.printStackTrace();
		}

		long[] arr = new long[100];
		Scanner console = new Scanner(new File(filename));
		for (int i = 0; i < 100; i++) {
			arr[i] = console.nextLong();
		}

		switch (algo) {
			case 0: System.out.println(KK(arr));
					break;
			case 1: System.out.println(repeatedRandom(arr, false));
					break;
			case 2: System.out.println(hillClimbing(arr, false));
					break;
			case 3: System.out.println(simulatedAnnealing(arr, false));
					break;
			case 11: System.out.println(repeatedRandom(arr, true));
					break;
			case 12: System.out.println(hillClimbing(arr, true));
					break;
			case 13: System.out.println(simulatedAnnealing(arr, true));
					break;
		}
		

		// PrintStream out = new PrintStream(new File("test.txt"));
		// long[] arr = randomArr(100);
		// for (int i = 0; i < 100; i++) {
		// 	out.println(arr[i]);
		// }


		// // EXPERIMENTS
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
		// 	long kkTemp = KKarp.KK(arr);
		// 	kkOut.print(kkTemp);
		// 	long end = System.currentTimeMillis();
		// 	kkOut.println("," + (end-start));

		// 	// repeated random 0
		// 	start = System.currentTimeMillis();
		// 	long rr0Temp = repeatedRandom(arr, false);
		// 	rr0Out.print(rr0Temp);
		// 	end = System.currentTimeMillis();
		// 	rr0Out.println("," + (end-start));

		// 	// repeated random 1
		// 	start = System.currentTimeMillis();
		// 	long rr1Temp = repeatedRandom(arr, true);
		// 	rr1Out.print(rr1Temp);
		// 	end = System.currentTimeMillis();
		// 	rr1Out.println("," + (end-start));

		// 	// hill climbing 0
		// 	start = System.currentTimeMillis();
		// 	long hc0Temp = hillClimbing(arr, false);
		// 	hc0Out.print(hc0Temp);
		// 	end = System.currentTimeMillis();
		// 	hc0Out.println("," + (end-start));

		// 	// hill climbing 1
		// 	start = System.currentTimeMillis();
		// 	long hc1Temp = hillClimbing(arr, true);
		// 	hc1Out.print(hc1Temp);
		// 	end = System.currentTimeMillis();
		// 	hc1Out.println("," + (end-start));

		// 	// sim annealing 0
		// 	start = System.currentTimeMillis();
		// 	long sa0Temp =  simulatedAnnealing(arr, false);
		// 	sa0Out.print(sa0Temp);
		// 	end = System.currentTimeMillis();
		// 	sa0Out.println("," + (end-start));

		// 	// sim annealing 1
		// 	start = System.currentTimeMillis();
		// 	long sa1Temp = simulatedAnnealing(arr, true);
		// 	sa1Out.print(sa1Temp);
		// 	end = System.currentTimeMillis();
		// 	sa1Out.println("," + (end-start));
		// }
	}
}