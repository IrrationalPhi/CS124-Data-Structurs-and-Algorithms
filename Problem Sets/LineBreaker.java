import java.util.*;
import java.io.*;

public class LineBreaker {
	public static int cube(int n) {
		return n*n*n;
	}

	public static void breaker(String text, int maxLength) {
		String[] words = text.split(" ");
		int n = words.length;

		// initialize stuff for dp bookkeeping
		int[] sumLengths = new int[n+1];
		int[] wordLengths = new int[n];

		// add in 0 and "" for base case
		int[] resVals = new int[n+1];
		String[] resText = new String[n+1];

		resVals[0] = 0;
		resText[0] = "";

		// account for "word 0" for later convenience
		sumLengths[0] = 0;
		int tempSum = 0;
		for (int i = 0; i < n; i++) {
			int temp = words[i].length() +1;
			tempSum += temp;
			wordLengths[i] = temp;
			sumLengths[i+1] = tempSum;
		}

		// from arguments earlier
		maxLength += 1;
		// set min penalty to impossibly large number for replacement
		int minPenalty = cube(maxLength) +1;

		// setup for dp
		// find smallest penalty for first i words
		// penalty includes last line penalty
		for (int i = 0; i < n; i++) {
			// we will set this to be the "inducted" value
			int minTempVal = minPenalty;
			String tempStr = "";
			for (int j = 0; j <= i; j++) {
				// what happens when we start last line at j?
				int lastLine = sumLengths[i+1] - sumLengths[j];
				int temp = resVals[j] + cube(maxLength - lastLine);

				if (lastLine <= maxLength && temp < minTempVal) {
					minTempVal = temp;
					tempStr = resText[j] + '\n' + text.substring(sumLengths[j], sumLengths[i+1] -1);
				}
			}
			resVals[i+1] = minTempVal;
			resText[i+1] = tempStr;
		}

		// System.out.println(Arrays.toString(resVals));

		// now, we bash for the best last line
		// we already have dp array so we gucci
		int bestVal = minPenalty;
		String res = "";
		for (int i = 0; i < n; i++) {
			// if can in last line
			if (sumLengths[n] - sumLengths[i] <= maxLength && resVals[i] < bestVal) {
				bestVal = resVals[i];
				res = resText[i] + '\n' + text.substring(sumLengths[i]);
			}
		}

		System.out.printf("penalty: %d\n", bestVal);
		System.out.println(res);
	}

	public static void main(String[] args) {
		String text = "Buffy the Vampire Slayer fans are sure to get their fix with the DVD release of the show's first season. The three-disc collection includes all 12 episodes as well as many extras. There is a collection of interviews by the show's creator Joss Whedon in which he explains his inspiration for the show as well as comments on the various cast members.  Much of the same material is covered in more depth with Whedon's commentary track for the show's first two episodes that make up the Buffy the Vampire Slayer pilot. The most interesting points of Whedon's commentary come from his explanation of the learning curve he encountered shifting from blockbuster films like Toy Story to a much lower-budget television series. The first disc also includes a short interview with David Boreanaz who plays the role of Angel. Other features include the script for the pilot episodes, a trailer, a large photo gallery of publicity shots and in-depth biographies of Whedon and several of the show's stars, including Sarah Michelle Gellar, Alyson Hannigan and Nicholas Brendon.";
		breaker(text, 40);
		breaker(text, 72);
	}
}