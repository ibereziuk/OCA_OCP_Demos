public class Sheep extends Animal
{
	static int MIN_WEIGHT = 10;
	static int MAX_WEIGHT = 120;
	
	public void move(Container c) 
	{
		System.out.println("Sheep " + name + " slowly moves to a ship");
		// complete method
	}
	
	public Sheep(double weight, String name)
	{
		this.weight = weight;
		this.name = name;
	}
	
	public Sheep(double weight)
	{
		this(weight, "Sven");
	}
	
	@Override
	public String toString() {
		return "Sheep " + (int)getWeight() + " kg";
	}
}