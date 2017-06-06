public abstract class Animal
{
	protected double weight;
	protected String name = "no_name";
	
	public double getWeight() { 
		return weight;
	}
	
	public void setWeight(double weight) { 
		this.weight = weight;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String s) {
		name = s;
	}
	
	public abstract void move(Container c);
}