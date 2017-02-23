
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.Logger;

/**
 * 日期工具类.
 *
 * @author abel<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016-01-18 <br>
 */
public class DateUtil {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(DateUtil.class);
	
	/** The Constant FORMAT_DAY. */
	public static final String FORMAT_DAY="yyyy-MM-dd";
	
	/** The Constant FORMAT_SEC. */
	public static final String FORMAT_SEC="yyyy-MM-dd HH:mm:ss";
	
	/** 获取带年月日的当前时间，发送邮件用 */
	public static final String FORMAT_NOW="yyyy年MM月dd日HH时mm分ss秒";

	/**
	 * Date to string.
	 *
	 * @param date the date
	 * @return the string
	 */
	public static String dateToString(Date date) {
		if (date == null) {
			return null;
		}
		String formatStr = FORMAT_SEC;
		DateFormat sdf = new SimpleDateFormat(formatStr);
		return sdf.format(date);
	}
	
	/**
	 * 格式化日期,默认格式 yyyy-MM-dd HH:mm:ss.
	 *
	 * @param date the date
	 * @param formatStr the format str
	 * @return the string
	 */
	public static String dateToString(Date date, String formatStr) {
		if (date == null) {
			return null;
		}
		if (formatStr == null || formatStr.equals("")) {
			formatStr = FORMAT_SEC;
		}
		DateFormat sdf = new SimpleDateFormat(formatStr);
		return sdf.format(date);
	}
	
	/**
	 * String to date.
	 *
	 * @param dateStr the date str
	 * @return the date
	 */
	public static Date stringToDate(String dateStr) {
		if (dateStr == null || dateStr.equals("")) {
			return null;
		}
		DateFormat sdf = new SimpleDateFormat(FORMAT_SEC);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			log.error(e);
		}
		return date;
	}
	
	/**
	 * 字符串转换到时间格式.
	 *
	 * @param dateStr the date str
	 * @param formatStr the format str
	 * @return the date
	 */
	public static Date stringToDate(String dateStr, String formatStr) {
		if (dateStr == null || dateStr.equals("")) {
			return null;
		}
		DateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			log.error(e);
		}
		return date;
	}
	
	/**
	 * 昨天.
	 *
	 * @param date the date
	 * @return the yester day
	 */
	public static Date getYesterDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}
	
	/**
	 * 得到几天前的时间.
	 *
	 * @param d the d
	 * @param day the day
	 * @return the date before
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 得到几天后的时间.
	 *
	 * @param d the d
	 * @param day the day
	 * @return the date after
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}
	
	/**
	 * 得到之前或之后几分钟的时间.
	 *
	 * @param d the d
	 * @param minute the minute
	 * @return the date after minute
	 */
	public static Date getDateAfterMinute(Date d, int minute) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.add(Calendar.MINUTE, minute);
		return now.getTime();
	}
	
	/**
	 * 获取本周的第一天.
	 *
	 * @return the first day of week
	 */
	public static Date getFirstDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal.getTime();
	}
	
	/**
	 * 昨天.
	 *
	 * @param format the format
	 * @return the yester day
	 */
	public static String getYesterDay(String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		DateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(calendar.getTime());
	}
	
	/**
	 * 获取当前时间
	 * @return CurrentDateToString
	 */
	public static String getCurrentDate() {
		//转换服务器时区为东八区
		TimeZone timeZoneSH = TimeZone.getTimeZone("Asia/Shanghai");
		SimpleDateFormat outputFormat = new SimpleDateFormat(FORMAT_NOW, Locale.CHINA);
		outputFormat.setTimeZone(timeZoneSH);
		Date date = new Date(System.currentTimeMillis());
		return outputFormat.format(date);
	}
	/**
	 * The main method.
	 *
	 * @param str the arguments
	 */
	public static void main(String[]str){
		System.out.println(getCurrentDate());
	}
}
