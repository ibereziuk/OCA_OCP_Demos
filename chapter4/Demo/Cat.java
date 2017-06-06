public class Cat extends Animal
{
	static int MIN_WEIGHT = 5;
	static int MAX_WEIGHT = 75;
	
	public void move(Container c) 
	{
		System.out.println("Cat " + name + " jmp to ship");
		// complete method
	}
	
	public Cat(double weight, String name)
	{
		this.weight = weight;
		this.name = name;
	}
	
	public Cat(double weight)
	{
		this(weight, "Tom");
	}
	
	@Override
	public String toString() {
		return "Cat " + (int)getWeight() + " kg";
	}
}