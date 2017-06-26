import java.util.*;
import java.util.stream.*;
import java.util.regex.Pattern;

public class Demo {
	public static void main(String[] args) {
		try {
			String lang = readLanguea(args);
			int nItems = readNItems(args);
			shoppingSimulation(nItems, lang);
		}
		catch (IllegalArgumentException e) {
			System.out.println("input problem: " + e.getMessage());
			System.out.println("Correct usage: [language [quantity]]"
				+ "\nwhere: \n"
				+ "\tsupported languages is 'en' and 'de' \n"
				+ "\tquantity should be an integer number");
		}
	}
	
	static int readNItems(String[] args) throws NumberFormatException {
		int nItems = 10;
		if (args.length >= 1) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].matches("\\d+")) {
					nItems = Integer.parseInt(args[i]);
				}
			}
		}
		return nItems;
	}
	
	static String readLanguea(String[] args) throws NumberFormatException {
		String lang = "en"; 	// default
		if (args.length >= 1) {
			for (int i = 0; i < args.length; i++) {
				boolean isWord = args[i].matches("[a-zA-Z]+");
				if (isWord) {
					lang = checkLanguage(args[i]);
				}
			}
		}
		return lang;
	}
	
	static String checkLanguage(String input) {
		String lang;
		if (input.trim().matches("en|de")) {
			lang = input;
		} else {
			throw new IllegalArgumentException(input + " language is not supported yet");
		}
		return lang;
	}
	
	static void shoppingSimulation(int nItems, String lang) {
		System.out.println("laguage: " + lang);
		Shop shop = new Shop();
		Customer customer = makeCustomer(lang);
		
		String[] productNames = shop.getProductNames();
		
		customer.pickItems(productNames, nItems);
		
		Bill bill = customer.settleUp();
		System.out.println(bill);
	}
	
	static Customer makeCustomer(String language) {
		int balance = 1000;
		DebitCard card = new DebitCard(balance, new Locale(language));
		Customer newCustomer = new Customer(card);
		return newCustomer;
	}	
}