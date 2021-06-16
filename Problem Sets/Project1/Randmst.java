import java.util.*;

public class Randmst {
	/*
		Say that there are n vertices
		inMST: array bookkeeping if each vertex is in MST already or not
		dist: array storing shortest distance from vertex to MST
		numpoints: number of vertices in graph
		numtrials: number of simulations to perform
		dimension: 0 means random weights, else >= 2 is R^d
		rand: rng
		locations: coordinates if using R^d
	*/
	private boolean[] inMST;
	private double[] dist;
	private int numpoints;
	private int numtrials;
	private int dimension;
	private Random rand;
	private double[][] locations;

	// initialize "infinity" for comparisons sake
	private static final double INF = Double.MAX_VALUE;

	// constructor
	public Randmst(int numpoints, int numtrials, int dimension) {
		this.numpoints = numpoints;
		this.numtrials = numtrials;
		this.dimension = dimension;
		this.inMST = new boolean[numpoints];
		this.dist = new double[numpoints];
		this.rand = new Random();
	}

	private void reset() {
		// reset rng
		this.rand = new Random(System.currentTimeMillis());
		if (this.dimension >= 2) {
			this.locations = new double[numpoints][dimension];

			// bash initialize
			for (int row = 0; row < numpoints; row++) {
				for (int col = 0; col < dimension; col++) {
					this.locations[row][col] = rand.nextDouble();
				}
			}
		}
	}

	// BASHER
	public double simulate() {
		double totalWeight = 0;
		for (int t = 0; t < numtrials; t++) {
			this.reset();
			double treeWeight = 0;
			// (re)initialize arrays
			for (int i = 0; i < numpoints; i++) {
				this.inMST[i] = false;
				this.dist[i] = INF;
			}
			// look at first vertex and we prims from there
			this.dist[0] = 0;
			// add one to MST at a time
			for (int currentNodes = 0; currentNodes < numpoints; currentNodes++) {
				int closestNode = 0;
				double minDist = INF;
				// bash through the nodes
				// get the closest node not in MST
				for (int i = 0; i < numpoints; i++) {
					if (!this.inMST[i] && this.dist[i] < minDist) {
						minDist = this.dist[i];
						closestNode = i;
					}
				}
				// add in closest to MST and update treeWeight
				treeWeight += minDist;
				this.inMST[closestNode] = true;

				// now bash through remaining not in MST
				// generate/update their distances to MST accordingly
				// 'tis prim's algo
				for (int i = 0; i < numpoints; i++) {
					if (!this.inMST[i]) {
						// get the weight between i and closestNode
						double weight = getWeight(i, closestNode);
						// update if smaller, else ignore
						dist[i] = Math.min(dist[i], weight);
					}
				}
			}
			totalWeight += treeWeight;
		}
		double avgWeight = totalWeight/numtrials;
		return avgWeight;
	}

	// private method to get edge weights
	private double getWeight(int i, int j) {
		if (this.dimension == 0) {
			return this.rand.nextDouble();
		}

		double res = 0;
		for (int k = 0; k < this.dimension; k++) {
			res += (locations[i][k] - locations[j][k]) * (locations[i][k] - locations[j][k]);
		}
		return Math.sqrt(res);
	}

	public static void main(String[] args) {
		int n = 0;
		int sims = 0;
		int dim = 0;
		if (args.length == 4) {
			try {
				n = Integer.parseInt(args[1]);
				sims = Integer.parseInt(args[2]);
				dim = Integer.parseInt(args[3]);
			} catch (NumberFormatException e) {
				System.err.println("Arguments must be integers.");
				System.exit(1);
			}
		} else {
			System.out.println("Incorrect format.");
			System.out.println("args has length " + args.length + ", expected 4");
			System.exit(1);
		}

		// start simulating if ok
		Randmst sim = new Randmst(n, sims, dim);
		double average = sim.simulate();
		System.out.println(average + " " + n + " " + sims + " " + dim);
	}
}