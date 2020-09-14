package Tavi007.ElementalCombat.util;

import java.util.HashMap;

import org.apache.commons.lang3.text.WordUtils;

import Tavi007.ElementalCombat.ElementalCombat;
import net.minecraft.util.text.TextFormatting;

public class DefenseDataHelper {
	
	//merge the @param additionalMap into the @param baseMap, so the highest value persists.
	public static void mergeMaps(HashMap<String, Integer> baseMap, HashMap<String, Integer> additionalMap){
		additionalMap.forEach((key, value)->{
			if(!baseMap.containsKey(key)) {
				baseMap.put(key, value);
			}
			else if(baseMap.get(key) > value) {
				baseMap.put(key, value);
			}
		});	
	}

	//merge the @param additionalMap into the @param baseMap, so the values of the same key get summed up.
	public static void sumMaps(HashMap<String, Integer> baseMap, HashMap<String, Integer> additionalMap){
		additionalMap.forEach((key, value)->{
			if(!baseMap.containsKey(key)) {
				baseMap.put(key, value);
			}
			else {
				baseMap.put(key, value + baseMap.get(key));
			}
		});	
	}
	
	public static float getScaling(HashMap<String, Integer> map, String key) {
		Integer factor = map.getOrDefault(key, 0);
		return 1.0f - getPercentage(factor);
	}
	
	public static float getPercentage(Integer factor) {
		return ((float) factor)/ElementalCombat.MAX_FACTOR;
	}
	
	@SuppressWarnings("deprecation")
	public static String toPercentageString(String key, Integer factor) {
		//get color
		Integer percentage = Math.round(DefenseDataHelper.getPercentage(factor)*100);
		TextFormatting textFormatting = TextFormatting.GRAY;
		if (percentage < 0) {textFormatting = TextFormatting.RED;}
		if (percentage > 0 && percentage < 100) {textFormatting = TextFormatting.BLUE;}
		if (percentage == 100) {textFormatting = TextFormatting.YELLOW;}
		if (percentage > 100) {textFormatting = TextFormatting.GREEN;}
		
		//make string
		return "" + TextFormatting.GRAY + " - " + WordUtils.capitalize(key) + " " + textFormatting + String.valueOf(percentage)+ "%" + TextFormatting.RESET;
	}
}
