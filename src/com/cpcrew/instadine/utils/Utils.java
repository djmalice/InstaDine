/**
 * 
 */
package com.cpcrew.instadine.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author raji
 * 
 */
public class Utils {

	/**
	 * Return true if DateNow > dateStop Return false if dateNow < dateStop (
	 * expired )
	 * 
	 * @param dateNow
	 * @param dateStop
	 * @return
	 */
	public static boolean isTimeGreaterThanNow(String dateStop) {

		String t3 = "7/23/2014 18:30 PDT"; // Should have been in this format
		System.out.println(" T3 " + t3);
		
		String t2;
		boolean isPm = false;
		if (dateStop.contains("PM") ){
			// Workaround since AM/PM written by pickers are not parsed
			// Not changing on the pickers to avoid breaking existing data
			isPm = true;
			t2 = dateStop.replace("PM" ,"");
		} else {
			t2 = dateStop.replace("AM", "");
		}
		SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy HH:mm z");
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm z");
		//df2.setTimeZone(TimeZone.getTimeZone("PDT"));
		//df.setTimeZone(TimeZone.getTimeZone("PDT"));
		System.out.println("Expiry Time " + dateStop);
		Date d1;
		try {
			d1 = new Date();
			System.out.println(t2);
			Date d2 = df.parse(t2 + " PDT");
			Date d3 = df2.parse(t3);
			long d1Ms = d1.getTime();
			long d2Ms = d2.getTime();
			
			System.out.println("D2 " + d2.getTime());
			if (isPm ){
				// Workaround since AM/PM written by pickers are not parsed
				// Not changing on the pickers to avoid breaking existing data
				d2Ms = d2Ms + 43200000;
			}
			System.out.println(d1 + ":::" + d2 + "::::" + d3 );
			System.out.println(d1Ms + "::::" + d2Ms + "::::" + d3.getTime());
			if (d2Ms > d1Ms)
				return false;
			else
				return true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static String toDisplayDate(String dt) {
		if ( dt != null && !dt.equals("" )) {
			SimpleDateFormat inF = new SimpleDateFormat("MM/dd/yyyy");
			SimpleDateFormat outF = new SimpleDateFormat("MMM dd");
			Date date;
			try {
				date = inF.parse(dt);
				return outF.format(date);	
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}
			
		}
		return "";
	}
	
	public static String toDisplayNow() {
		
		SimpleDateFormat outF = new SimpleDateFormat("MMM dd");
		Date today = Calendar.getInstance().getTime();        
		String reportDate = outF.format(today);
		return reportDate;
	}
	
	public static String getFacebookImageUrl(String facebookId) {
		return ("https://graph.facebook.com/" + facebookId + "/picture?type=large");
	}
	
	public static boolean lessThanTwoMinLeft(String dateStop) {

		String t3 = "7/23/2014 18:30 PDT"; // Should have been in this format
		System.out.println(" T3 " + t3);
		
		String t2;
		boolean isPm = false;
		if (dateStop.contains("PM") ){
			// Workaround since AM/PM written by pickers are not parsed
			// Not changing on the pickers to avoid breaking existing data
			isPm = true;
			t2 = dateStop.replace("PM" ,"");
		} else {
			t2 = dateStop.replace("AM", "");
		}
		SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy HH:mm z");
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm z");
		//df2.setTimeZone(TimeZone.getTimeZone("PDT"));
		//df.setTimeZone(TimeZone.getTimeZone("PDT"));
		System.out.println("Expiry Time " + dateStop);
		Date d1;
		try {
			d1 = new Date();
			System.out.println(t2);
			Date d2 = df.parse(t2 + " PDT");
			Date d3 = df2.parse(t3);
			long d1Ms = d1.getTime(); 
			long d2Ms = d2.getTime();
			
			System.out.println("D2 " + d2.getTime());
			if (isPm ){
				// Workaround since AM/PM written by pickers are not parsed
				// Not changing on the pickers to avoid breaking existing data
				d2Ms = d2Ms + 43200000;
			}
			System.out.println(d1 + ":::" + d2 + "::::" + d3 );
			System.out.println(d1Ms + "::::" + d2Ms + "::::" + d3.getTime());
			if ( Math.abs(d2Ms - d1Ms) < 120000 ) //d1Ms is current time
				return true;
			else
				return false;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static long eventTimeinMilliseconds(String dateStop) {

		String t3 = "7/23/2014 18:30 PDT"; // Should have been in this format
		System.out.println(" T3 " + t3);
		
		String t2;
		boolean isPm = false;
		if (dateStop.contains("PM") ){
			// Workaround since AM/PM written by pickers are not parsed
			// Not changing on the pickers to avoid breaking existing data
			isPm = true;
			t2 = dateStop.replace("PM" ,"");
		} else {
			t2 = dateStop.replace("AM", "");
		}
		SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy HH:mm z");
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm z");
		//df2.setTimeZone(TimeZone.getTimeZone("PDT"));
		//df.setTimeZone(TimeZone.getTimeZone("PDT"));
		System.out.println("Expiry Time " + dateStop);
		Date d1;
		try {
			d1 = new Date();
			System.out.println(t2);
			Date d2 = df.parse(t2 + " PDT");
			Date d3 = df2.parse(t3);
			long d1Ms = d1.getTime();
			long d2Ms = d2.getTime();
			
			System.out.println("D2 " + d2.getTime());
			if (isPm ){
				// Workaround since AM/PM written by pickers are not parsed
				// Not changing on the pickers to avoid breaking existing data
				d2Ms = d2Ms + 43200000;
			}
			return d2Ms;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}

}
