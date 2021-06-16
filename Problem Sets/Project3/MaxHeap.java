public class MaxHeap {
	private long[] arr;
	private int nElems;
	private int maxSize;

	//specifies maxsize
	public MaxHeap(int maxSize) {
		this.maxSize = maxSize;
		this.arr = new long[maxSize];
		this.nElems = 0;
	}

	//constructor based on input array
	public MaxHeap(long[] arr) {
		this.arr = arr.clone();
		this.nElems = this.arr.length;
		this.maxSize = this.arr.length;

		for (int i = maxSize/2; i >= 0; i--) {
			this.maxHeapify(i);
		}
	}

	public long peek() {
		return arr[0];
	}

	public long pop() {
		long temp = arr[0];
		arr[0] = arr[--nElems];
		maxHeapify(0);
		return temp;
	}

	public boolean push(long i) {
		if (this.isFull())
			return false;
		int k = nElems;
		arr[nElems++] = i;
		while (k > 0 && arr[k] > arr[parent(k)]) {
			swap(arr, k, parent(k));
			k = parent(k);
		}
		return true;
	}

	public boolean isFull() {
		return nElems == maxSize;
	}

	public boolean isEmpty() {
		return nElems == 0;
	}

	public void display() {
		for (int i = 0; i < this.nElems; i++) {
			System.out.print(this.arr[i] + " ");
		}
		System.out.println();
	}

	private void maxHeapify(int i) {
		//compare i with children, get max
		int largest = i;
		if (left(i) < nElems && arr[left(i)] > arr[i])
			largest = left(i);
		if (right(i) < nElems && arr[right(i)] > arr[largest])
			largest = right(i);
		//swap with largest
		if (largest != i) {
			swap(arr, i, largest);
			maxHeapify(largest);
		}
	}

	private static void swap(long[] arr, int i, int j) {
		long temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	private static int left(int i) {
		return 2*i + 1;
	}

	private static int right(int i) {
		return 2*i + 2;
	}

	private static int parent(int i) {
		return (i-1)/2;
	}
}