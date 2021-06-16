#include<bits/std.c++>
using namespace std;

enum array {A11, A12, A21, A22, 
			B11, B12, B21, B22, 
			prod1, prod2, prod3, prod4, prod5, prod6, prod7, 
			res11, res12, res21, res22, 
			sub1, sub2, sub3, sub4, sub5, sub6, sub7, sub8, sub9, sub10, sub11, sub12, sub13, sub14, num_arrs};

// TODO: change this as needed later
int crossover = 100;



// here, our matrices will be arrays of len n^2 instead
void strassen_multiply(int *A, int *B, int *res, int size) {
	// edge case, i think
	if (size == 1) {
		*res = *A * *B;
		return;
	}

	int half = size/2;
	// store the matrices for the ones in enum array
	int *temp[num_arrs];

	if (size <= crossover) {
		traditional_multiply(A, B, res, size);
		return;
	}

	// memory allocation
	for (int i = 0; i < num_arrs; i++) {
		temp[i] = malloc(size * size * sizeof(int));
	}

	// now, we initialize the A's and B's for bashing later
	// TODO
	for (int row = 0, i = 0; row < half; row++, i++) {
		for (int col = 0, j = 0; col < half; col++, j++) {
			temp[A11][i * half + j] = A[row * size + col];
			temp[B11][i * half + j] = B[row * size + col];
		}

		for (int col = half, j = 0; col < size; col++, j++) {
			temp[A12][i * half + j] = A[row * size + col];
			temp[B12][i * half + j] = B[row * size + col];
		}
	}

	for (int row = half, i = 0; row < size; row++, i++) {
		for (int col = 0, j = 0; col < half; col++, j++) {
			temp[A21][i * half + j] = A[row * size + col];
			temp[B21][i * half + j] = B[row * size + col];
		}

		for (int col = half, j = 0; col < size; col++, j++) {
			temp[A22][i * half + j] = A[row * size + col];
			temp[B22][i * half + j] = B[row * size + col];
		}
	}

	// now we calculate the prod's
	for 
}

// maybe improve caching later, idk. 'tis monke implementation
void traditional_multiply(int *A, int *B, int *res, int size) {
	for (int i = 0; i < size; i++) {
		for (int j = 0; j < size; j++) {
			int sum = 0;
			for (int k = 0; k < size; k++) {
				sum += A[i*size + k]*B[k*size + j];
			}
			res[i*size + j] = sum;
		}
	}
}

void add(int *A, int *B, int *res, int size) {
	for (int i = 0; i < size; i++) {
		for (int j = 0; j < size; j++) {
			res[i*size + j] = A[i*size + j] + B[i*size + j];
		}
	}
}

void subtract(int *A, int *B, int *res, int size) {
	for (int i = 0; i < size; i++) {
		for (int j = 0; j < size; j++) {
			res[i*size + j] = A[i*size + j] - B[i*size + j];
		}
	}
}

void 