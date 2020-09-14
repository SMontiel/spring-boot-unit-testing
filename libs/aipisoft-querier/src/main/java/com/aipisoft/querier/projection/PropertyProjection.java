package com.aipisoft.querier.projection;

import com.aipisoft.querier.Helper;
import com.aipisoft.querier.Query;
import com.aipisoft.querier.builder.Builder;



/**
 * Class that represents a property projection. It is build with a property and an optional alias.
 * <br>The property is a compound string of table alias and property, being table alias optional. Example: e.someProp. If not
 * table alias is found on property, the alias of the table based on the property name is used.
 * <br>If alias is not assigned, the property name is used.
 * 
 * @author Ivan Perales
 *
 */
public class PropertyProjection extends Projection{

	private String property;
	
	public PropertyProjection(String property){
		this(property, null);
	}
	
	public PropertyProjection(String property, String alias){
		this.property = property;
		setAlias(alias);
	}
	
	protected String getProperty(){
		return property;
	}
	
	@Override public void setQuery(Query query){
		super.setQuery(query);
		query.registerProperty(property);
	}
	
	@Override public void build(Builder builder){
		builder.append(getQuery().getFullProperty(getProperty()));
		builder.append(" as ");
		builder.append(getAlias() != null ? getAlias() : Helper.getPropertyName(property));
	}
}
