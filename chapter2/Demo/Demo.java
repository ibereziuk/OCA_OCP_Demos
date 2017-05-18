import java.util.*;
import java.util.function.*;

public class Demo {
	public static void main(String[] args) {
		int nCows = getNcows(args);
		ArrayList<Cow> herdCow = Cow.createCowHerd(nCows);
	
		// print(herdCow);
		
		int nCollisions = calcCollisions(herdCow, (c) -> c);
		int nHashCollisions = calcCollisions(herdCow, (c) -> c.hashCode());
		
		displayResults("Object", nCollisions, nCows);
		displayResults("Hash", nHashCollisions, nCows);
	}
	
	
	static int getNcows(String[] args) {
		int amount = 10;	// default amount
		try {
			if (args.length != 0) {
				amount = Integer.parseInt(args[0]);
				if (amount < 1) {
					throw new NumberFormatException("Negative doesn't work here");
				}
			}
		}
		catch(NumberFormatException ex) {
			System.out.println("Please enter positive integer number");
			System.exit(13);
		}
		return amount;
	}
	
	
	private static void print(ArrayList<Cow> herd) {
		StringJoiner sj = new StringJoiner(",\n\t", "[\n\t", "\n]");
		for(Cow c : herd) {
			sj.add(c.toString());
		}
		System.out.println(sj.toString());
	}

	
	private interface ValueRetriever {
		Object retrieve(Object c);
	}
	
	private static int calcCollisions(ArrayList<Cow> herd, ValueRetriever retriever) {
		Set<Object> cowHashSet = new HashSet<>();
		int hashCollisions = 0;
		for(Cow c : herd) {
			boolean success = cowHashSet.add(retriever.retrieve(c));
			if(!success) {
				// System.out.println(c);
				hashCollisions++;
			}
		}
		return hashCollisions;
	}
	
	
	static void displayResults(String collisionType, int nCollisions, int nTotal) {
		System.out.println("\n\t" + collisionType
			+ String.format(" collision rate: %d out of %d = %.3f", 
				nCollisions, nTotal, (100 * (double)nCollisions / nTotal)) 
			+ "%");
	}
}