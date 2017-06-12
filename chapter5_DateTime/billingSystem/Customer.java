import java.util.*;

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
}