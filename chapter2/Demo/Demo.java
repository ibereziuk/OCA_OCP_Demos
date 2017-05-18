import java.util.*;
import java.util.function.*;

public class Demo {	
	public static void main(String[] args) {
		int nCows = args.length == 0 ? 10 : Integer.parseInt(args[0]);
		
		ArrayList<Cow> herdCow = createCowHerd(nCows);
	
		// print(herdCow);
				
		int nCollisions = calcCollisions(herdCow, (c) -> c);
		int nHashCollisions = calcCollisions(herdCow, (c) -> c.hashCode());
		
		displayResults("Object", nCollisions, nCows);
		displayResults("Hash", nHashCollisions, nCows);
	}
	
	
	private static ArrayList<Cow> createCowHerd(int nCows) {
		final int MIN_AGE = 0;
		final int MAX_AGE = 6;
		
		String[] namesPool = {
			"Bessie", "Clarabelle", "Betty Sue",
			"Emma", "Henrietta", "Ella",
			"Penelope", "Nettie", "Anna",
			"Bella", "Annabelle", "Dorothy",
			"Molly", "Gertie", "Annie"
		};
		
		Random rand = new Random();
		Cow.Color[] colors = Cow.Color.values();


		ArrayList<Cow> herdCow = new ArrayList<>();
		
		int age;
		String name;
		Cow.Color color;
		Cow.CowBuilder cb = new Cow.CowBuilder();
		
		for (int i = 0; i < nCows; i++) {
			age = rand.nextInt(MAX_AGE + 1);
			name = namesPool[rand.nextInt(namesPool.length)];
			color = colors[rand.nextInt(colors.length)];
			
			cb.withAge(age)
				.withName(name)
				.withColor(color);
			
			herdCow.add(cb.build());
		}
		
		return herdCow;
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
		System.out.println("\n\t" + collisionType +
			String.format(" collision rate: %d out of %d = ", nCollisions, nTotal) 
			+ (100 * (double)nCollisions / nTotal) + "%");
	}
}