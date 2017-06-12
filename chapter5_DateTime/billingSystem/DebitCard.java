import java.util.*;

public class DebitCard {
	double balance;
	Locale locale;
	public Locale getLocale() { return locale;}
	
	
	public DebitCard(int initalBalance, Locale _locale) {
		balance = initalBalance;
		locale = _locale;
	}
}