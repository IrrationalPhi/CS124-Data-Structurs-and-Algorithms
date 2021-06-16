public class KKarp {
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