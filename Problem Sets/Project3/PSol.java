import java.util.*;

public class PSol implements Sol{
	public long[] arr;
	public int[] P;
	public static final Random random = new Random();

	// CONSTRUCTORS
	public PSol(long[] arr) {
		int n = arr.length;
		this.P = new int[n];
		this.arr = arr;

		for (int i = 0; i < n; i++) {
			this.P[i] = random.nextInt(n);
		}
	}

	public PSol(long[] arr, int[] P) {
		this.arr = arr;
		this.P = P;
	}

	// METHODS
	public Sol randomNeighbor() {
		int n = this.arr.length;
		int[] pNew = this.P.clone();

		int i = random.nextInt(n);
		int j = this.P[i];

		while (j == this.P[i]) {
			j = random.nextInt(n);
		}
		pNew[i] = j;
		return new PSol(this.arr, pNew);
	}

	public long getResidue() {
		int n = this.arr.length;
		long[] arrNew = new long[n];
		for (int i = 0; i < n; i++) {
			arrNew[this.P[i]] += arr[i];
		}
		return KK(arrNew);
	}

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
}