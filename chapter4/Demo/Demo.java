import java.util.function.*; // import functional interfaces
import java.util.*;
import java.util.stream.*;

public class Demo {
	public static void main(String[] args) {
		simulateIsland();
	}
	
	static void simulateIsland() {
		int nCats = 20;
		int nSheeps = 25;
public class Demo 
{
	public static void main(String[] args) {
		simulateIsland();	
	}
	
	static void simulateIsland() {
		int nCats = 10;
		int nSheeps = 20;
		Stream<Animal> animals = settleIsland(nCats, nSheeps);

		//// volcano BOOM here!
		List<Animal> animalList = animals.collect(Collectors.toList());
		Collections.shuffle(animalList);
		animals = animalList.stream();
		
		
		//// Ship is comming. Grab as much as possible animals aboard!		
		int capacity = 1000;
		Ship ship = new Ship(capacity);
		ship.load(animals);
		
		
		// calculate ship stats
		System.out.println("Animals saved:");
		ship.showContent();
	}
	
	
	static Stream<Animal> settleIsland(int nCats, int nSheeps) {
		Random rand = new Random();
		
		//// settle an island
		Supplier<Cat> catSupplier = () -> {
			double weight = Cat.MIN_WEIGHT + (Cat.MAX_WEIGHT - Cat.MIN_WEIGHT) * rand.nextDouble() ;
			return new Cat(weight);
		};
		
		Supplier<Sheep> sheepSupplier = () -> {
			double weight = Sheep.MIN_WEIGHT + (Sheep.MAX_WEIGHT - Sheep.MIN_WEIGHT) * rand.nextDouble() ;
			return new Sheep(weight);
		};
		
		Stream<Cat> catStream = Stream.generate(catSupplier).limit(nCats);
		Stream<Sheep> sheepStream = Stream.generate(sheepSupplier).limit(nSheeps);
		
		return Stream.concat(catStream, sheepStream);
	}	
}