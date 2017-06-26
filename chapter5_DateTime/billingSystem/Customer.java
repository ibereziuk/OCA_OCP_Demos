import java.util.*;
import java.util.stream.*;

public class Customer {
	List<Item> basket;
	DebitCard card;
	
	public Customer(DebitCard _card) {
		card = _card;
		basket = new ArrayList<Item>();
	}
	
	public void pickItem(Item item) {
		basket.add(item);
	}
	
	public Bill settleUp() {
		// implement
		return new Bill(basket, card.getLocale());
	}
	
	public void pickItems(String[] productNames, int nItemsToBuy) {
		Random rand = new Random();
		
		Stream
			.generate(() -> rand.nextInt(productNames.length))
			.limit(nItemsToBuy)
			.map(i -> new Descrete(productNames[i], 1))
			.forEach(item -> this.pickItem(item));
	}
}