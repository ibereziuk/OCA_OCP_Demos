import java.util.*;
import java.lang.*;
import java.util.stream.*;

public class Ship implements Container
{
	private final double capacity;
	private double spaceLeft;
	private List<Animal> cargo = new ArrayList<Animal>();
	
	public double getCapacity() { return capacity; }
	
	public List<Animal> getCargo() {
		return cargo;
	}
	
	public Ship(double capacity) {
		this.capacity = capacity;
		this.spaceLeft = capacity;
	}
	
	public void showContent() {
		Map<Boolean, List<Animal>> partitionedAnimals = cargo.stream().collect(
				Collectors.partitioningBy(a -> a instanceof Cat));
		
		int nCatsOnBoard = partitionedAnimals.get(true).size();
		int nSheepsOnBoard = partitionedAnimals.get(false).size();
		
		
		System.out.println("\tSheeps on board: " + nSheepsOnBoard); 
		System.out.println("       .-.'  `; `-._  __  _");
		System.out.println("     (_,         .-:'  `; `-._");
		System.out.println("    ,'o\"(        (_,           )");
		System.out.println("   (__,-'      ,'o\"(            )>");
		System.out.println("      (       (__,-'            )");
		System.out.println("       `-'._.--._(             )");
		System.out.println("          |||  |||`-'._.--._.-'");
		System.out.println("                     |||  |||");
		System.out.println();
	
		System.out.println("\tCats on board: " + nCatsOnBoard); 
		System.out.println(" ");
        System.out.println("              _' \\_");
        System.out.println("            ,' '  '`.");
        System.out.println("            ;,)      \\");
        System.out.println("           /          :");
        System.out.println("           (_         :");
        System.out.println("            `--.       \\");
        System.out.println("               /        `.");
        System.out.println("              ;           `.");
        System.out.println("             /              `.");
        System.out.println("            :                 `.");
        System.out.println("            :                   \\");
        System.out.println("             \\\\                  \\");
        System.out.println("              ::                 :");
        System.out.println("              || |               |");
        System.out.println("              || |`._            ;");
        System.out.println("             _;; ; __`._,       (________");
        System.out.println("            ((__/(_____(______,'______(___)");
	}
	
	public void load(List<Animal> animals) {
		animals.sort((Animal a, Animal b) -> {
			return Double.compare(a.getWeight(), b.getWeight());
		});
		
		while (!animals.isEmpty() 
			&& animals.get(0).getWeight() < spaceLeft) {
			Animal animalToMove = animals.get(0);
			cargo.add(animalToMove);
			spaceLeft -= animalToMove.getWeight();
			animals.remove(0);
		}
	}
	
	public void load (Stream<Animal> animals) {
		animals
			.sorted((a1,a2) -> Double.compare(a1.getWeight(), a2.getWeight()))
			.filter(a -> a.getWeight() < spaceLeft)
			.forEach(a -> {
				cargo.add(a);
				spaceLeft -= a.getWeight();	
			});
	}
}