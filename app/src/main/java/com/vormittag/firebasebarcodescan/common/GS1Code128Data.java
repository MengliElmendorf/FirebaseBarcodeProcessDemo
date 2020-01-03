package com.vormittag.firebasebarcodescan.common;

import android.util.Log;
import com.google.common.collect.Maps;


import org.joda.time.DateMidnight;

import java.util.Map;

public class GS1Code128Data {
	/** Maps the AI to the corresponding data from the barcode. */
	  private final Map<String, String> data = Maps.newHashMap();

	  private static final Map<String, AII> aiinfo = Maps.newHashMap();

	  static class AII {
	    final int minLength;
	    final int maxLength;

	    public AII(String id, int minLength, int maxLength) {
	      this.minLength = minLength;
	      this.maxLength = maxLength;
	    }
	  }

	  private static void ai(String id, int minLength, int maxLength) {
	    aiinfo.put(id, new AII(id, minLength, maxLength));
	  }

	  private static void ai(String id, int length) {
	    aiinfo.put(id, new AII(id, length, length));
	  }

	  static {
	    ai("00", 18, 18);
	    ai("01", 14);
	    ai("02", 14);
	    ai("10", 1, 20);
	    ai("11", 6);
	    ai("12", 6);
	    ai("13", 6);
	    ai("15", 6);
	    ai("17", 6);
	    ai("20", 2);
	    ai("21", 1, 20);
	    ai("3200", 6);
	    ai("3201", 6);
		ai("3202", 6);
	    ai("3203", 6);
	    ai("3204", 6);
	    ai("3105", 6);
	  
	  }

	  /**
	   * Decodes a Unicode string from a Code128-like encoding.
	   *
	   * @param fnc1 The character that represents FNC1.
	   */
	  public GS1Code128Data(String s, char fnc1) {
	    StringBuilder ai = new StringBuilder();
	    int index = 0;
	    while (index < s.length()) {
	    	int position = index++;
	  	  	ai.append(s.charAt(position));
	  	  	//Remove Group Separator
	  	  	int lastCharCodePoint = ai.codePointAt(ai.length()-1);
			if (lastCharCodePoint == 29) {
				ai = ai.deleteCharAt(ai.length()-1);
			}
		  	AII info = aiinfo.get(ai.toString());
		    if (info != null) {
		    	StringBuilder value = new StringBuilder();
		    	for (int i = 0; i < info.maxLength && index < s.length(); i++) {
		    		char c = s.charAt(index++);
		    		if (c == fnc1) {
		    			break;
		    		}
		    		value.append(c);
		    	}
		    	if (value.length() < info.minLength) {
		    		throw new IllegalArgumentException("Short field for AI \"" + ai + "\": \"" + value + "\".");
		    	}
		    	Log.v("128 data", "key is " + ai.toString() + " value is " + value.toString());
		    	data.put(ai.toString(), value.toString());
		    	ai.setLength(0);
		    }
	    }
	    if (ai.length() > 0) {
	      throw new IllegalArgumentException("Unknown AI \"" + ai + "\".");
	    }
	  }

	 

	private static DateMidnight asDate(String s) {
	    if (s == null) {
	      return null;
	    }
	    String century = s.compareTo("500000") < 0 ? "20" : "19";
	    return new DateMidnight(century + s);
	  }

	  public DateMidnight getDueDate() {
	    return asDate(data.get("12"));
	  }
	  
	  
	  public String get3200() {
		   return data.get("3200");
	  }
	  
	  public String get3202() {
		   return data.get("3202");
	  }
	
	  public String get3100() {
		   return data.get("3100");
	  }
	  
	  public String get01() {
		   return data.get("01");
	  }
	  
	  public String get00() {
		   return data.get("00");
	  }
	
	  public String get02() {
		   return data.get("02");
	  }
	
	  public String get10() {
		   return data.get("10");
	  }
	  
	  
	  public String get11() {
		   return data.get("11");
	  }
	  
	  public String get12() {
		   return data.get("12");
	  }
	  public String get13() {
		   return data.get("13");
		   
	  } public String get15() {
		   return data.get("15");
	  }
	  
	  public String get17() {
		   return data.get("17");
	  }
		  
	  public String get21() {
		   return data.get("21");
	  }
	
	  
	
}
