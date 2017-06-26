import java.util.*;
import java.io.File;
import java.net.*;

public class Shop {
	static public String[] getProductNames() {
		ResourceBundle priceBundle = ResourceBundle.getBundle("pricelist");
		String[] names = priceBundle.keySet().toArray(new String[0]);
		return names;
	}
}