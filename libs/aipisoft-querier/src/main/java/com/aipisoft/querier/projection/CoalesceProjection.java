package com.aipisoft.querier.projection;

import com.aipisoft.querier.Helper;
import com.aipisoft.querier.Query;
import com.aipisoft.querier.builder.Builder;

/**
 * Coalesce projection.
 * 
 * @author Ivan Perales
 *
 */
public class CoalesceProjection extends Projection{

	private String property;
	private String[] values;
	
	public CoalesceProjection(String property, String[] values){
		this(property, null, values);
	}
	
	public CoalesceProjection(String property, String alias, String[] values){
		this.property = property;
		this.values = values;
		setAlias(alias);
	}
	
	@Override public void setQuery(Query query){
		super.setQuery(query);
		query.registerProperty(property);
	}
	
	@Override public void build(Builder builder){
		builder.append("coalesce(");
		builder.append(getQuery().getFullProperty(property));
		
		for(String val : values){
			builder.append(", ");
			builder.append(val);
		}
		
		builder.append(") as ");
		builder.append(getAlias() != null ? getAlias() : Helper.getPropertyName(property));
	}
}
