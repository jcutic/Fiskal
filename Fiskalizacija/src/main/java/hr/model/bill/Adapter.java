package hr.model.bill;

import java.text.DecimalFormat;
import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter extends XmlAdapter<String, Double>{

	/**
	 * Metoda za postavljanje Double s dvije decimale i defaultnim localeom prilikom kreiranja XML-a 
	 */
	@Override
	public String marshal(Double arg0) {
			Locale.setDefault(new Locale("en", "US"));
	        DecimalFormat twoDForm = new DecimalFormat("##.00");
	    return twoDForm.format(arg0);
	}

	/**
	 * Ova metoda se trenutno ne koristi, koristila bi se prilikom raspakiravanja XML-a u objekt
	 */
	@Override
	public Double unmarshal(String arg0) {
		return null;
	}

}
