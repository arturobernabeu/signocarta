package es.fcc.signocarta.controller.util;

import java.util.regex.Pattern;

public class Validation {

	// método para comprobar si el dato de entrada es un email
	public static boolean isEmail(String datoEntrada) {

		String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
		if (datoEntrada == null) {
			return false;
		}
		return Pattern.matches(EMAIL_REGEX, datoEntrada);
	}
	
	

}
