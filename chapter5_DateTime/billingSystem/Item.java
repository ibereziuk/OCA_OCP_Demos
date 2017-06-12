import java.util.*;

abstract class Item {
	double pricePerUnit;
	String productName;
	
	public abstract double getPrice();
	public String getName() { return productName; }
}


class Descrete extends Item {
	static ResourceBundle priceBundle = ResourceBundle.getBundle("pricelist");
	
	int quantity;
	
	Descrete(String _name, int _quantity) {
		productName = _name;
		
		pricePerUnit = Double.parseDouble(priceBundle.getString(_name));
		quantity = _quantity;
	}
	
	@Override
	public double getPrice() {
		return pricePerUnit * quantity;
	}
	
	@Override
	public String toString() {
		return "Descrete " + productName;
	}
}


class Continuous extends Item {
	double quantity;
	
	Continuous(String _name, double _quantity) {
		quantity = _quantity;
	}
	
	
	@Override
	public double getPrice() {
		return pricePerUnit * quantity;
	}
	
	@Override
	public String toString() {
		return "Continuous " + productName;
	}
}	