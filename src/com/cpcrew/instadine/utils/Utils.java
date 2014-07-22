/**
 * 
 */
package com.cpcrew.instadine.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	public static boolean isTimeGreaterThan(String dateNow, String dateStop) {

		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mma");

		Date d1;
		try {
			d1 = df.parse(dateNow);
			Date d2 = df.parse(dateStop);
			long d1Ms = d1.getTime();
			long d2Ms = d2.getTime();
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

}
