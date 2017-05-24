import java.util.*;
import java.util.function.*;
import java.time.*;

public class Demo3 {
	public static void main(String[] args) {
		LocalTime startTime = LocalTime.now();
		
		int nCows = getNcows(args);
		ArrayList<Cow> herdCow = Cow.createCowHerd(nCows);
	
		// print(herdCow);
		
		int nCollisions = calcCollisions(herdCow, (c) -> c);
		int nHashCollisions = calcCollisions(herdCow, (c) -> c.hashCode());
		
		displayResults("Object", nCollisions, nCows);
		displayResults("Hash", nHashCollisions, nCows);
		
		checkTime(startTime);
		
		startTime = LocalTime.now();
		

		System.out.println("compare by current Productivity:");
		Comparator<Cow> compMilk = new Comparator<Cow>() {
			public int compare(Cow c1, Cow c2) {
				return (int)(1000 * (c1.getMilkPerDay() - c2.getMilkPerDay()));
			}
		};
		Collections.sort(herdCow, compMilk);
		print(herdCow);
		
		
		System.out.println("compare by Age:");
		Collections.sort(herdCow, (c1,c2) -> c1.getAge() - c2.getAge() );
		print(herdCow);
		
		
		System.out.println("\ncompare by milkToDeath:");
		Collections.sort(herdCow);
		print(herdCow);
		
		checkTime(startTime);
	}
	
	
	static void checkTime(LocalTime timeStamp) {
		Duration d = Duration.between(timeStamp, LocalTime.now());
		System.out.println("clock: " + ((double)d.toMillis() / 1000) + " ms");
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

	
	private static int calcCollisions(ArrayList<Cow> herd,
									  Extractor<Cow, ? extends Object> extractor) {
		Set<Object> cowHashSet = new HashSet<>();
		int hashCollisions = 0;
		for(Cow c : herd) {
			boolean success = cowHashSet.add(extractor.extract(c));
			if(!success) {
				hashCollisions++;
			}
		}
		return hashCollisions;
	}
	
	
	static void displayResults(String collisionType, int nCollisions, int nTotal) {
		System.out.println("\t" + collisionType
			+ String.format(" collision rate: %d out of %d = %.3f", 
				nCollisions, nTotal, (100 * (double)nCollisions / nTotal)) 
			+ "%");
	}
}


interface Extractor<IT, OT> {
	OT extract(IT c);
}