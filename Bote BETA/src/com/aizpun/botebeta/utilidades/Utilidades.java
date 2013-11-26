package com.aizpun.botebeta.utilidades;

import java.text.NumberFormat;
import java.util.Locale;

public class Utilidades {

	public static String formatearDouble(Double d) {
		NumberFormat format = null;
		if (Locale.getDefault().toString().equals("es_ES")) {
			format = NumberFormat.getNumberInstance(Locale.GERMANY);
			return format.format(d);
		} else {
			format = NumberFormat.getNumberInstance();
			return format.format(d);
		}
	}

	public static String formatearMoneda(Double d) {
		NumberFormat format = null;
		if (Locale.getDefault().toString().equals("es_ES")) {
			format = NumberFormat.getCurrencyInstance(Locale.GERMANY);
			return format.format(d);
		} else {
			format = NumberFormat.getCurrencyInstance();
			return format.format(d);
		}
	}

	// DecimalFormatSymbols s = new DecimalFormatSymbols(Locale.getDefault());
	// char sep=s.getDecimalSeparator();
}
