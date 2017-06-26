import java.util.*;
import java.util.stream.*;
import java.time.*;
import java.time.format.*;

public class Bill {
	static int width = 60;
	List<Item> itemList;
	Locale locale;
	
	public Bill(List<Item> basket, Locale _locale) {
		itemList = basket;
		locale = _locale;
	}
	
	@Override
	public String toString() {
		ResourceBundle greetingsBundle = ResourceBundle.getBundle("terms", locale);
		String greetings = greetingsBundle.getString("greetings");
		String prefix = "\t\t\t" + greetings + "\n\t";
		
		double moneyToPay = inTotal();
		String suffix =	"\n\n\t" + composeAny("in total", "$" + moneyToPay, width, ".");
		
		StringJoiner joiner = new StringJoiner("\n\t", prefix, suffix);
		
		itemList.stream()
			.sorted((it1,it2) -> it1.getName().compareTo(it2.getName()))
			.map(item -> composeItemEntry(item, width))
			.forEach(joiner::add);
			
		ZonedDateTime time = ZonedDateTime.now();

		String returnValue = joiner.toString()
			+ "\n\n\ttime:  " 
			+ time.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
			
		return returnValue;
	}
	
	double inTotal() {
		return itemList.stream()
			.mapToDouble(Item::getPrice)
			.sum();
	}
	
	String composeItemEntry(Item item, int width) {
		String price = Double.toString(item.getPrice());
		ResourceBundle itemNames = ResourceBundle.getBundle("itemnames", locale);
		String name = itemNames.getString(item.getName());
		return composeAny(name, price, width, ".");
	}
	
	static String composeAny(String startWord, String endWord, int width, String interStr) {
		StringBuilder builder = new StringBuilder();
		builder.append(startWord);
		
		int upTo = width - endWord.length();
		for(int i = startWord.length(); i < upTo; i++) {
			builder.append(interStr);
		}
		builder.append(endWord);
		
		return builder.toString();
	}
}