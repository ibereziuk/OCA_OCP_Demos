import java.util.*;

public class Cow implements Comparable<Cow> {
	static final int MIN_AGE = 0;
	static final int MAX_AGE = 6;
	
	
	private static class MilkCalculator {
		private static final int MIN_MILK_PER_DAY = 14;
		private static final Random rand = new Random();
		private static final int MILK_AGE_TRESHOLD = 0;
	
		private static MilkCalculator instance;
		private MilkCalculator(){}
	
		public static synchronized MilkCalculator getInstance() {
			if(instance == null) {
				instance = new MilkCalculator();
			}
			return instance;
		}
	
		public static float calculate(int age) {
			double amount = 0;
			if (age >= MILK_AGE_TRESHOLD) {
				amount = 14 + 0.1 * (age - 1) - 0.5 * Math.pow(age - 4, 2);			
				final double MAX_DEVIATION = 0.20;
				Random r = new Random();
				double deviation = (2 * r.nextDouble() - 1) * MAX_DEVIATION;	// -0.2 ... +0.2
				amount *= 1 + deviation;
			}
			return (float)amount;
		}
	}
	
	
	public static class CowBuilder {
		private int age;
		private String name;
		private Color color;
		
		public CowBuilder withAge(int age) {
			this.age = age;
			return this;
		}
		
		public CowBuilder withColor(Color color) {
			this.color = color;
			return this;			
		}			
		
		public CowBuilder withName(String name) {
			this.name = name;
			return this;
		}
		
		public Cow build() {
			return new Cow(age, name, color);
		}
	}
	
	
	public static ArrayList<Cow> createCowHerd(int nCows) {
		
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
			age = rand.nextInt(Cow.MAX_AGE + 1);
			name = namesPool[rand.nextInt(namesPool.length)];
			color = colors[rand.nextInt(colors.length)];
			
			cb.withAge(age)
				.withName(name)
				.withColor(color);
			
			herdCow.add(cb.build());
		}
		
		return herdCow;
	}
	
	
	public enum Color {
		BROWN, WHITE, MIXED
	}
	
	
	private int age;
	private String name;
	private float milkPerDay;
	private Color color;
	
	public int getAge() { return age;}
	public float getMilkPerDay() { return milkPerDay; }
	
	public Cow(int age, String name, Color c) {
		this.age = age;
		this.name = name;
		this.color = c;
		this.milkPerDay = MilkCalculator.getInstance().calculate(age);
	}
	
	
	@Override
	public int compareTo(Cow c) {
		if (this.equals(c)) {
			return 0;
		}
		int yearsLeft1 = (Cow.MAX_AGE + 1 - this.age);
		int yearsLeft2 = (Cow.MAX_AGE + 1 - c.age);
		double milkToProduce1 = yearsLeft1 * this.milkPerDay;
		double milkToProduce2 = yearsLeft2 * c.milkPerDay;
		
		return (int)(1000*(milkToProduce1 - milkToProduce2));
	}
	
	
	@Override
	public int hashCode() {
		int hash = this.toString().hashCode();
		hash ^= Float.hashCode(milkPerDay);
		return hash;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Cow)) {
			return false;
		}
		Cow cow = (Cow)obj;
		
		return this.age == cow.age 
			&& this.name == cow.name 
			&& this.milkPerDay == cow.milkPerDay 
			&& this.color == cow.color;
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Cow ")
			.append(name)
			.append(", ");
		int nameSpace = 17;
		int rest = nameSpace - builder.length();
		
		for(int i = 1; i < rest; i++) {
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