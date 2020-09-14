package com.aipisoft.querier.table;

import org.apache.commons.lang3.StringUtils;

import com.aipisoft.querier.Helper;
import com.aipisoft.querier.Query;
import com.aipisoft.querier.builder.Buildable;
import com.aipisoft.querier.builder.Builder;

/**
 * Object for define a property with a value for modifications. The simplest way is to add ? for the value assigned to a property.
 * 
 * @author Ivan Perales
 *
 */
public class PropertyValue implements Buildable{
	private String tableAlias;
	private String property;
	private Object value;
	
	public PropertyValue(Query query, String fullProperty, Object value){
		this(query, Helper.getTableAlias(fullProperty), Helper.getPropertyName(fullProperty), value);
	}
	
	public PropertyValue(Query query, String tableAlias, String property, Object value){
		this.tableAlias = tableAlias;
		this.property = property;
		this.value = value;
		query.registerProperty((tableAlias != null ? (tableAlias + ".") : StringUtils.EMPTY) + property);
	}
	
	public String getTableAlias(){
		return tableAlias;
	}
	
	public String getProperty(){
		return property;
	}
	
	public Object getValue(){
		return value;
	}
	
	public void buildProperty(Builder builder){
		if(getTableAlias() != null){
			builder.append(getTableAlias());
			builder.append(".");
		}
		
		builder.append(getProperty());
	}
	
	/** Este metodo solo construye la parte del valor, es responsabilidad del implementador saber a que se lo asigna */
	@Override public void build(Builder builder){
		builder.append("?");
		builder.addArg(value);
	}
}
