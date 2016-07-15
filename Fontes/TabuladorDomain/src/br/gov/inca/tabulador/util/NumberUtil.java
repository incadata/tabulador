package br.gov.inca.tabulador.util;

import java.math.BigDecimal;

public class NumberUtil {

	public static double arredondar(double num, int casasDecimais){
		BigDecimal bd = new BigDecimal(num);  
        bd = bd.setScale(casasDecimais, BigDecimal.ROUND_HALF_UP);  
        return bd.doubleValue();  
	}
	
	public static BigDecimal arredondarBD(double num, int casasDecimais){
		BigDecimal bd = new BigDecimal(num);  
        bd = bd.setScale(casasDecimais, BigDecimal.ROUND_HALF_UP);  
        return bd;  
	}

	public static Long toLong(Object valor) {
		if (valor == null)
			return 0L;
		else if (valor instanceof Number)
			return ((Number)valor).longValue();
		else if (valor instanceof String && StringUtils.isInteger(valor.toString()))
			return Long.parseLong(valor.toString(), 10);
		else
			return null;
	}

}
