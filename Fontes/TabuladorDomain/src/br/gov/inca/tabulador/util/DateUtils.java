package br.gov.inca.tabulador.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {

	public static final Integer COMP_ANTERIOR = -1;
	public static final Integer COMP_IGUAL = 0;
	public static final Integer COMP_POSTERIOR = 1;
	public static String DEFAUT_DATE_FMT = "dd/MM/yyyy";
	public static String DEFAUT_DATE_HOUR_FMT = "dd/MM/yyyy HH:mm";
	public static String DEFAUT_HOUR_FMT = "HH:mm";
	private static int[][] NUM_DAYS_OF_MONTHS = {{31,28,31,30,31,30,31,31,30,31,30,31}, {31,29,31,30,31,30,31,31,30,31,30,31}};
	public static Long DAY_IN_MILISECONDS = 24 * 60 * 60 * 1000L;
	public static Locale DEFAULT_LOCALE = new Locale("pt", "BR");
	
    public static boolean isDate(String date){
    	//dd/mm/yyyy
    	if (date == null) return false;
    	Pattern p = Pattern.compile("([0][0-9]|[12][0-9]|[3][01])/([0][1-9]|[1][012])/((18|19|20)[0-9]{2})");
		 Matcher m = p.matcher(date);
		 return m.find();
    }

    public static boolean isMonthYear(String mothYear){
    	//dd/mm/yyyy
    	if (mothYear == null) return false;
    	Pattern p = Pattern.compile("([0][1-9]|[1][012])/((18|19|20)[0-9]{2})");
		 Matcher m = p.matcher(mothYear);
		 return m.find();
    }

    public static boolean isHour(String hour){
    	//HH:mm
    	Pattern p = Pattern.compile("([0][0-9]|[1][0-9]|[2][0-4]):([0-5][0-9])");
		 Matcher m = p.matcher(hour);
		 return m.find();
    }
    
    public static boolean isLeapYear(int year){
    	return year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
    }

    public static String formatDate(Date data){
		return new SimpleDateFormat(DEFAUT_DATE_FMT).format(data);
	}

	public static String formatDate(String formato, Date data){
		return formatDate(formato, DEFAULT_LOCALE, data);
	}
    
	public static String formatDate(String formato, Locale locale, Date data){
		return new SimpleDateFormat(formato, locale).format(data);
	}

	public static String formatHour(Date data){
		return new SimpleDateFormat(DEFAUT_HOUR_FMT).format(data);
	}

	public static String formatDateHour(Date data){
		return new SimpleDateFormat(DEFAUT_DATE_FMT + " " + DEFAUT_HOUR_FMT).format(data);
	}
	
	public static Integer ageFromDateInterval(Calendar dataNascimento, Calendar dataCalculo){
		int anoNasc = dataNascimento.get(Calendar.YEAR);
		int mesNasc = dataNascimento.get(Calendar.MONTH);
		int diaNasc = dataNascimento.get(Calendar.DAY_OF_MONTH);
		
		int anoCalc = dataCalculo.get(Calendar.YEAR);
		int mesCalc = dataCalculo.get(Calendar.MONTH);
		int diaCalc = dataCalculo.get(Calendar.DAY_OF_MONTH);
		
		if ((mesCalc > mesNasc)||((mesCalc==mesNasc)&&(diaCalc >= diaNasc)))
			return (anoCalc - anoNasc);
		else
			return (anoCalc - anoNasc - 1);
	}

	public static int ageFromDateInterval(Date dataNascimento, Date dataCalculo){
		GregorianCalendar gcDataNasc = new GregorianCalendar();   
		gcDataNasc.setTime(dataNascimento);  
		
		int anoNasc = gcDataNasc.get(Calendar.YEAR);
		int mesNasc = gcDataNasc.get(Calendar.MONTH);
		int diaNasc = gcDataNasc.get(Calendar.DAY_OF_MONTH);
		
		GregorianCalendar gcDataCalc = new GregorianCalendar();
		gcDataCalc.setTime(dataCalculo);
		
		int anoCalc = gcDataCalc.get(Calendar.YEAR);
		int mesCalc = gcDataCalc.get(Calendar.MONTH);
		int diaCalc = gcDataCalc.get(Calendar.DAY_OF_MONTH);
		
		if ((mesCalc > mesNasc)||((mesCalc==mesNasc)&&(diaCalc >= diaNasc)))
			return (anoCalc - anoNasc);
		else
			return (anoCalc - anoNasc - 1);
	}
	
	public static long daysFromDateInterval(Date data1, Date data2){
		if (data1 == null || data2 == null) return 0;
		return (data1.getTime() - data2.getTime()) / DAY_IN_MILISECONDS;
	}
	
	public static Long daysFromCalendarInterval(Calendar data1, Calendar data2, boolean somentePositivo){
		if (data1 == null || data2 == null) return null;
		long days = (data1.getTimeInMillis() - data2.getTimeInMillis()) / DAY_IN_MILISECONDS;
		return days > 0 || !somentePositivo ? days : null;
	}

	public static int monthsBetweenDates(Date dataIni, Date dataFim){
		GregorianCalendar gcDataIni = new GregorianCalendar();   
		gcDataIni.setTime(dataIni);  
		GregorianCalendar gcDataFim = new GregorianCalendar();
		gcDataFim.setTime(dataFim);
		
		int anos  = gcDataFim.get(Calendar.YEAR) - gcDataIni.get(Calendar.YEAR);
		int meses = gcDataFim.get(Calendar.MONTH) - gcDataIni.get(Calendar.MONTH);
		int dias  = gcDataFim.get(Calendar.DAY_OF_MONTH) - gcDataIni.get(Calendar.DAY_OF_MONTH);
		
		if (dias <= 0) meses--;
		if (meses <= 0){
			anos--;
			meses += 12;
		}
			
		return meses + (anos * 12);
		
	}
	
	public static Date toDate(String formato, String data){
		try {
			return new SimpleDateFormat(formato).parse(data);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date toDate(String data){
		try {
			return new SimpleDateFormat(DEFAUT_DATE_FMT).parse(data);
		} catch (ParseException e) {
			return null;
		} 
	}
	
	public static Date today(){
		return Calendar.getInstance().getTime();
	}
	
	/**
	 * Retorna somente a data
	 * @return
	 */
	public static Date currentDate(){
		return removerHourFromDate(Calendar.getInstance().getTime());
	}

	public static Date todayPlusDays(int days){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}
	
	public static Date removerHourFromDate(Date data){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date primeiroDiaDoProximoMes(){
		Calendar calendar = new GregorianCalendar();
		int mes = calendar.get(Calendar.MONTH);
		int ano = calendar.get(Calendar.YEAR);
		if(mes == Calendar.DECEMBER){
			mes = Calendar.JANUARY;
			ano++;
		} else {
			mes++;
		}
		
		calendar.set(ano, mes, 1);
		
		return calendar.getTime();
	}
	
	/**
	 * 
	 * @param ano campo ano.
	 * @param mes campo obitido do Calendar.MONTH, que vai de 0 a 11.
	 * @return
	 */
	public static int ultimoDiaDoMesCalendar(int ano, int mes){
		return NUM_DAYS_OF_MONTHS[isLeapYear(ano) ? 1 : 0][mes];
	}

	public static int ultimoDiaDoMes(int ano, int mes){
		return NUM_DAYS_OF_MONTHS[isLeapYear(ano) ? 1 : 0][mes-1];
	}

	public static Date ultimoDiaDoMesDate(String mesAno){
		if (!isMonthYear(mesAno)) return null;
		
		int mes = Integer.parseInt(mesAno.split("/")[0], 10);
		int ano = Integer.parseInt(mesAno.split("/")[1], 10);
		int dia = ultimoDiaDoMes(ano, mes);
		return new GregorianCalendar(ano, mes, dia).getTime();
	}

	public static int get(int calendarType, Date date){
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal.get(calendarType);
		} catch (NullPointerException e) {
			return -1;
		}
	}
	
    /**
     * Insere a hora e os minutos em uma Data
     * @param dateParam - Data a ser manipulada
     * @param hour - Hora a ser includa
     * @param minute - Minuto a ser includo
     * @param second - Segundo a ser includo
     * @return - Retorna uma data com a hora, minutos e segundos setados
     * @throws Exception - Caso acontea algum erro
     */
    public static Date setDateHourMinuteSecond(Date dateParam, Integer hour, Integer minute, Integer second ) throws Exception {
        Calendar dteComp = Calendar.getInstance();
        dteComp.setTime(dateParam);
        dteComp.set(Calendar.HOUR_OF_DAY, hour);
        dteComp.set(Calendar.MINUTE, minute);
        dteComp.set(Calendar.SECOND, second);
        dteComp.set(Calendar.MILLISECOND, 0);
        return dteComp.getTime();
    }
    
    public static Date ultimaHoraData(Date data) throws Exception{
    	return setDateHourMinuteSecond(data, 23, 59, 59);
    }
    
    public static boolean DataMaiorHoje(String data){
		return Calendar.getInstance().getTime().before(DateUtils.toDate(data));
	}
	
    public static String formataMesAno(int anoMes){
    	return String.format("%02d/%4d", anoMes % 100, anoMes / 100);
    }
    
    /**
     * @author rmanoel
     * @param data
     * @return
     */
    public static Date copiarData(Date data){
    	return new Date(data.getTime());
    }
    
    /**
     * Transforma uma string anoMes para mesAno.
     * <p>
     * Exemplo: 201001 = 01/2010 <br>
     * Exemplo por extenso: 201001 = Janeiro/2010
     * </p>
     * @author rmanoel
     * @param data
     * @param extenso
     * @return
     */
    public static String anoMesParaMesAno(String data, boolean extenso){
    	if (data == null || data.length() != 6 || !StringUtils.isInteger(data)) return "";
    	
    	String ano = data.substring(0,4);
    	String mes = data.substring(4,6);
    	String formato = extenso ? "MMMM/yyyy" : "MM/yyyy";
    	
    	return formatDate(formato, toDate("01/" + mes + "/" + ano));
    }
    
    /**
     * 
     * @author rmanoel
     * @param data
     * @param limite
     * @return
     */
    public static Date ultimoDiaDoMes(Date data, Date limite){
    	int ano = get(Calendar.YEAR, data);
    	int mes = get(Calendar.MONTH, data);
    	Date retorno = new GregorianCalendar(ano, mes, ultimoDiaDoMesCalendar(ano, mes)).getTime();
    	return limite != null && limite.before(retorno) ? limite : retorno; 
    }

    /**
     * @author rmanoel
     * @param data
     * @return
     */
    public static Date primeiroDiaDoProximoMes(Date data){
    	int ano = get(Calendar.YEAR, data);
    	int mes = get(Calendar.MONTH, data);
    	if(mes == Calendar.DECEMBER){
			mes = Calendar.JANUARY;
			ano++;
		} else {
			mes++;
		}
    	return new GregorianCalendar(ano, mes, 1).getTime();
    }

    /**
     * Adiciona dias a data
     * @param date Data
     * @param days quantidade de Dias
     * @author rmanoel
     * @return Retorna uma nova instancia do tipo Date com o valor da data passada por parametro acrescida dos dias informados
     */
	public static Date addDays(Date date, int days) {
		return add(date, Calendar.DAY_OF_MONTH, days);
	}

    /**
     * Adiciona dias a data
     * @param date Data
     * @param field campo da data a ser acrescido
     * @param amount quantidade a ser acrescida no campo da data
     * @author rmanoel
     * @return Retorna uma nova instancia do tipo Date com o valor da data passada por parametro acrescida no campo informados
     */
	public static Date add(Date date, int field, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, amount);
		return cal.getTime();
	}
	
	public static String formatPeriodo(Date dataInicio, Date dataFim){
		if (dataInicio.equals(dataFim))
			return formatDate("dd 'de' mmmm 'de' yyyy", dataInicio);
		
		Calendar calIni = Calendar.getInstance();
		calIni.setTime(dataInicio);
		Calendar calFim = Calendar.getInstance();
		calFim.setTime(dataFim);
		
		if (calIni.get(Calendar.YEAR) != calFim.get(Calendar.YEAR))
			return formatDate("dd 'de' mmmm 'de' yyyy", dataInicio) + " a " + formatDate("dd 'de' mmmm 'de' yyyy", dataFim);
		else if (calIni.get(Calendar.MONTH) != calFim.get(Calendar.MONTH))
			return formatDate("dd 'de' mmmm", dataInicio) + " a " + formatDate("dd 'de' mmmm 'de' yyyy", dataFim);
		else 
			return formatDate("dd", dataInicio) + " a " + formatDate("dd 'de' mmmm 'de' yyyy", dataFim);
	}
	
	public static java.sql.Date toSqlDate(Date date) {
		return new java.sql.Date(date.getTime());
	}

	public static java.sql.Date todaySql() {
		return toSqlDate(today());
	}
	
	public static Calendar toCalendar(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static String nowFormattedInverse() {
		return formatDate("yyyyMMddHHmmss", today());
	}

	public static Integer extractYearFrom(Date data) {
		return get(Calendar.YEAR, data);
	}
	
	public static Integer extractYearFrom(Calendar data) {
		return data != null ? data.get(Calendar.YEAR) : null;
	}
	
	public static Long extractYearLongFrom(Calendar data) {
		return data != null ? Long.valueOf(data.get(Calendar.YEAR)) : null;
	}

	public static Long extractYearLongFrom(String data) {
		return isDate(data) ? Long.valueOf(toCalendar(data).get(Calendar.YEAR)) : null;
	}

	public static Calendar toCalendar(String formato, String data) {
		return toCalendar(toDate(formato, data));
	}

	public static String formatCalendar(String formato, Calendar cal) {
		if (cal != null && cal instanceof Calendar)
			return formatDate(formato, cal.getTime());
		else
			return null;
	}

	public static Calendar toCalendar(String data) {
		return toCalendar(DEFAUT_DATE_FMT, data);
	}
	
	public static Integer comparaDatas(String data1, String data2){
		if (!isDate(data1) && !isDate(data2)) return null;
		
		return toCalendar(data1).compareTo(toCalendar(data2));
	}
	
	public static boolean ehMenorQue(String data1, String data2){
		return COMP_ANTERIOR.equals(comparaDatas(data1, data2));
	}

	public static boolean ehMaiorQue(String data1, String data2){
		return COMP_POSTERIOR.equals(comparaDatas(data1, data2));
	}

	public static Calendar ultimaHoraData(Calendar data) {
		Calendar novaData = new GregorianCalendar(data.get(Calendar.YEAR), data.get(Calendar.MONTH), data.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		novaData.set(Calendar.MILLISECOND, 999);
		return novaData;
	}

}
