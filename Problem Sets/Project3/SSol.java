import java.util.*;
import java.lang.Math.*;

public class SSol implements Sol{
	public long[] arr;
	public int[] S;
	public static final Random random = new Random();

	public SSol(long[] arr) {
		int n = arr.length;
		this.arr = arr;
		this.S = new int[n];
		for (int i = 0; i < n; i++) {
			this.S[i] = random.nextBoolean() ? 1 : -1;
		}
	}

	public SSol(long[] arr, int[] S) {
		this.arr = arr;
		this.S = S;
	}

	public long getResidue() {
		long res = 0;
		int n = this.arr.length;
		for (int i = 0; i < n; i++) {
			res += this.S[i] * arr[i];
		}
		return Math.abs(res);
	}

	public Sol randomNeighbor() {
		int n = this.arr.length;
		int[] newSol = this.S.clone();

		int i = random.nextInt(n);
		newSol[i] *= -1;

		if (random.nextBoolean()) {
			int j = i;
			while (j == i) {
				j = random.nextInt(n);
			}
			newSol[j] *= -1;
		}
		return new SSol(arr, newSol);
	}
}