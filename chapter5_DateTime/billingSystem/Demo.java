import java.util.*;
import java.util.stream.*;

public class Demo {
	public static void main(String[] args) {
		shopSimulation();
	}
	
	static void shopSimulation() {
		int cache = 1000;
		Customer customer = makeCustomer(cache, "en"); 
		pickItems(customer);
		Bill bill = customer.settleUp();
		bill.print();
	}
	
	static Customer makeCustomer(int balance, String language) { 
		DebitCard card = new DebitCard(balance, new Locale(language));
		Customer newCustomer = new Customer(card);
		return newCustomer;
	}
	
	static public void pickItems(Customer c) {
		int nItemsToBuy = 10;
		Random rand = new Random();
		
		// extract list of items present on shelf from external file
		ResourceBundle priceBundle = ResourceBundle.getBundle("pricelist");
		String[] itemNamesList = priceBundle.keySet().toArray(new String[0]);
		
		Stream
			.generate(() -> rand.nextInt(itemNamesList.length))
			.limit(nItemsToBuy)
			.map(i -> new Descrete(itemNamesList[i], 1))
			.forEach(item -> c.pickItem(item));
	}
}

