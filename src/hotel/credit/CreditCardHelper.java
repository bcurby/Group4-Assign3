package hotel.credit;

public class CreditCardHelper {
	
	private static CreditCardHelper self;
	
	public static synchronized CreditCardHelper getInstance() {
		if (self == null) {
			self = new CreditCardHelper();
		}
		return self;
		
	}


	public CreditCard makeCreditCard(CreditCardType type, int number, int ccv) {
		CreditCard creditCard = new CreditCard(type, number, ccv);
		return creditCard;
	}

}
