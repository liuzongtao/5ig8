/**
 * 
 */
package cn.guba.igu8.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zongtao liu
 *
 */
public class MatcherUtil {

	private static final String letterPatternStr = "^[A-Za-z]+$";

	public static boolean matcher(String value, String patternStr) {
		if (value == null || value == "") {
			return false;
		}
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(value);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	public static boolean isAllLetter(String value) {
		return matcher(value, letterPatternStr);
	}
}
