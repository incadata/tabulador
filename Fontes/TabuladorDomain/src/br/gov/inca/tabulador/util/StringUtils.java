package br.gov.inca.tabulador.util;

public class StringUtils {
	
    public static final String BLANK = "";

    public static boolean isBlank(Object... objs){
    	for(Object obj : objs){
            if(obj == null || BLANK.equals(obj.toString().trim())){
                return true;
            }
        }
        return false;
    }
    
    public static boolean isNotBlank(Object... objs){
        for(Object obj : objs){
            if(obj == null || BLANK.equals(obj.toString().trim())){
                return false;
            }
        }
        return true;
    }
    
    public static boolean hasNotBlank(Object... objs){
        for(Object obj : objs){
            if(isNotBlank(obj)){
                return true;
            }
        }
        return false;
    }

    public static String safeToString(Object obj){
    	if(isNotBlank(obj)){
    		return obj.toString();
    	}
    	return null;
    }
    
    public static boolean isInteger(String str){
    	try {
			Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return false;
		}
    	return true;
    }

	public static String trim(Object str) {
		return safeToString(str).trim();
	}
}
