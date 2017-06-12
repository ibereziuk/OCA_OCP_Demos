import java.util.*;
import java.util.stream.*;
import java.time.*;
import java.time.format.*;

public class Bill {
	List<Item> itemList;
	Locale locale;
	
	public Bill(List<Item> basket, Locale _locale) {
		itemList = basket;
		locale = _locale;
	}
	
	
	public void print() {
		int width = 60;
		String prefix = "\t\t\tWelcome to the shop\n\t";
		
		double moneyToPay = itemList.stream()
			.mapToDouble(Item::getPrice)
			.sum();
		String suffix =	"\n\n\tin total:...... $" + moneyToPay;
		StringJoiner joiner = new StringJoiner("\n\t", prefix, suffix);
		
		itemList.stream()
			.map(item -> composeItemEntry(item, width))
			.forEach(joiner::add);
			
		ZonedDateTime time = ZonedDateTime.now();
		String bill = joiner.toString()
			+ "\n\ttime:  " + time.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
		System.out.println(bill);
	}
	
	
	static String composeItemEntry(Item item, int width) {
		String price = Double.toString(item.getPrice());
		String name = item.getName();
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