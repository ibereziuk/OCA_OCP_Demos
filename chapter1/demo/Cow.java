import java.util.*;

public class Cow
{
	private static class MilkCalculator
	{
		public static final int MIN_MILK_PER_DAY = 14;
		private static final Random rand = new Random();
		private static final int MILK_AGE_TRESHOLD = 0;
	
		public static float calculate(int age)
		{
			double amount = 0;
			if (age >= MILK_AGE_TRESHOLD)
			{
				amount = 14 + 0.1 * (age - 1) - 0.5 * Math.pow(age - 4, 2);			
				final double MAX_DEVIATION = 0.20;
				Random r = new Random();
				double deviation = (2 * r.nextDouble() - 1) * MAX_DEVIATION;	// -0.2 ... +0.2
				amount *= 1 + deviation;
			}
			return (float)amount;
		}
	}
	
	public enum Color {
		BROWN, WHITE, MIXED
	}
	
	private static MilkCalculator milkCalc = new MilkCalculator();
	
	private int age;
	private String name;
	private float milkPerDay;
	private Color color;
	
	
	public Cow(int age, String name, Color c)
	{
		this.age = age;
		this.name = name;
		this.color = c;
		this.milkPerDay = milkCalc.calculate(age);
	}
	
	
	@Override
	public int hashCode()
	{
		int hash = this.toString().hashCode();
		hash ^= Float.hashCode(milkPerDay);
		return hash;
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Cow))
		{
			return false;
		}
		Cow cow = (Cow)obj;
		
		return this.age == cow.age 
			&& this.name == cow.name 
			&& this.milkPerDay == cow.milkPerDay 
			&& this.color == cow.color;
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Cow ")
			.append(name)
			.append(", ");
		int nameSpace = 17;
		int rest = nameSpace - builder.length();
		for(int i = 1; i < rest; i++)
		{
			 builder.append(" ");
		}
		
		builder.append(age)
			.append(" years old. ")
			.append(color)
			.append(". Produce ")
			.append(String.format("%4.1f", milkPerDay))
			.append(" l/day");
		return builder.toString();
	}
}