import java.util.*;
import java.io.*;

public class Matrix {
	// n x n matrix
	public int[][] rows;
	public int dim;

	// constructor
	public Matrix(int[][] rows) {
		if (rows.length != rows[0].length)
			throw new IllegalArgumentException("initializer must be square array");
		this.rows = rows;
		this.dim = rows.length;
	}

	public Matrix(int dim) {
		// all zeros
		this.rows = new int[dim][dim];
		this.dim = dim;
	}
	
	// for debugging monke
	public void display() {
		for (int i = 0; i < this.dim; i++) {
			for (int j = 0; j < this.dim; j++) {
				System.out.print(this.rows[i][j] + " ");
			}
			System.out.println();
		}
	}
}