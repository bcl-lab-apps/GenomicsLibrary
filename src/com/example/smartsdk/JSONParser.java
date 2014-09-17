package com.example.smartsdk;
/**
 * @author Xi(Stephen) Chen
 */
import android.util.Log;

public class JSONParser {
	static final String TAG="JSONParser";
	public static String parseBrackets(String json){
		int firstIndex=json.indexOf("[");
		int lastIndex=json.lastIndexOf("]");
		json=json.substring(firstIndex+1,lastIndex);
		if(json.contains(",")){
			String splitString="";
			Log.d(TAG, "comma detected");
			for(int i=0;i<json.split(",").length;i++){
				splitString=splitString+removeQuotes(json.split(",")[i])+" ";
			};
			json=splitString;
		}
		return json;
	}
	
	static String removeQuotes(String chars){
		if (chars.contains("\"")){
			chars=chars.replaceAll("^\"|\"$", "");			
		}
		return chars;
	}
	
	public static String[] coordinateParse(String coordinates){
		coordinates=coordinates.replace(":", " ");
		coordinates=coordinates.replace("-", " ");
		return coordinates.split(" ");
	}
}
