package com.aipisoft.querier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Helper methods
 * @author Ivan Perales
 *
 */
public class Helper {

	/**
	 * Returns the table alias of the property. 
	 * <br>Example: given the property: <i>e.someProp</i>, it returns <b>e</b>
	 */
	public static String getTableAlias(String property){
		int index = property.indexOf(".");
		return index > 0 ? property.substring(0, index) : null;
	}
	
	/**
	 * Returns the property name, without the table alias if it exist
	 * <br>Example: given the path: <i>e.someProp</i>, it returns <b>someProp</b>
	 */
	public static String getPropertyName(String property){
		int index = property.indexOf(".");
		return index >= 0 ? property.substring(index + 1) : property;
	}
	
	/**
	 * Indicates if the obj1 is different from obj 2
	 */
	public static boolean isDiff(Object obj1, Object obj2){
		if(obj1 == null && obj2 == null){
			return false;
		}
		
		if(obj1 == null && obj2 != null){
			return true;
		}
		
		if(obj1 != null && obj2 == null){
			return true;
		}
		
		if(obj1 == obj2){
			return false;
		}
		
		if(obj1 instanceof Comparable && obj2 instanceof Comparable ){
			Comparable c1 = (Comparable)obj1;
			Comparable c2 = (Comparable)obj2;
			return c1.compareTo(c2) != 0;
		}
		
		return obj1.equals(obj2) == false;
	}
	
	public static boolean isDiff(BigDecimal obj1, BigDecimal obj2){
		if(obj1 == null && obj2 == null){
			return false;
		}
		
		if(obj1 == null && obj2 != null){
			return true;
		}
		
		if(obj1 != null && obj2 == null){
			return true;
		}
		
		if(obj1 == obj2){
			return false;
		}
		
		return obj1.compareTo(obj2) != 0;
	}
	
	public static boolean isDiff(List obj1, List obj2){
		if(obj1 == null && obj2 == null){
			return false;
		}
		
		if(obj1 == null && obj2 != null){
			return true;
		}
		
		if(obj1 != null && obj2 == null){
			return true;
		}
		
		if(obj1 == obj2){
			return false;
		}
		
		if(obj1.size() != obj2.size()){
			return true;
		}
		
		for(int i=0; i<obj1.size(); i++){
			Object o1 = obj1.get(i);
			Object o2 = obj2.get(i);
			
			if(isDiff(o1, o2)){
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isDiffJson(String json1, String json2){
		return Helper.isDiff(json1 !=  null ? new JSONObject(json1) : null, json2 !=  null ? new JSONObject(json2) : null);
	}
	
	public static boolean isDiff(JSONObject json1, JSONObject json2){
		if(json1 == null && json2 == null){
			return false;
		}
		
		if(json1 == null && json2 != null){
			return true;
		}
		
		if(json1 != null && json2 == null){
			return true;
		}
		
		//instead of comparing strings, compare every key
		//obj1.toString().equals(obj2.toString()) == false
		
		if(json1.length() != json2.length()) {
			return true;
		}
		
		return isDiffRecursive(json1, json2);
	}
	
	public static boolean isDiffRecursive(JSONObject json1, JSONObject json2) {
		for(String key1 : json1.keySet()) {
			if(json2.has(key1)) {
				Object obj1 = json1.get(key1);
				Object obj2 = json2.get(key1);
				
				if(obj1 instanceof JSONObject) {
					if(obj2 instanceof JSONObject) {
						if(isDiffRecursive((JSONObject)obj1, (JSONObject)obj2)) {
							return true;
						}
					} else {
						return true;
					}
				} else if(obj1 instanceof JSONArray) {
					if(obj2 instanceof JSONArray) {
						if(((JSONArray)obj1).similar((JSONArray)obj2) == false) {
							return true;
						}
					} else {
						return true;
					}
				} else {
					if(obj1 instanceof Number) {
						obj1 = new BigDecimal(((Number)obj1).doubleValue());
					}
					
					if(obj2 instanceof Number) {
						obj2 = new BigDecimal(((Number)obj2).doubleValue());
					}
					
					if(Helper.isDiff(obj1, obj2)) {
						return true;
					}
				}
				
			} else {
				return true;
			}
		}
		
		//ahora revisamos el json2 en caso de que existan llaves que en el json1 no, por lo que serian diferentes
		for(String key2 : json2.keySet()) {
			if(json1.has(key2) == false) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns a list of objects containing the values of obj. If obj is an instance of an array, the result is put into a list.
	 * If tge obj is not an instance of List or array, a list with one element is returned
	 */
	public static List<Object> getList(Object obj){
		if(obj instanceof List){
			return (List)obj;
		}
		
		if(obj instanceof Collection) {
			Collection<Object> col = (Collection)obj;
			return col.stream().collect(Collectors.toList());
		}
		
		List<Object> list = new ArrayList<Object>();
		if(obj instanceof Object[]){
			Object[] elements = (Object[])obj;
			
			for(int i=0; i<elements.length; i++){
				list.add(elements[i]);
			}
		} else{
			list.add(obj);
		}
		return list;
	}
	
	/** Retorna verdadero si la longitud de la cadena esta en el rango */
	public static boolean safeBw(String str, int from, int to){
		int len = StringUtils.length(str);
		if(len >= from && len <= to){
			return true;
		}
		return false;
	}
}
