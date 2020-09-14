package com.aipisoft.querier.restriction;

import com.aipisoft.querier.Query;
import com.aipisoft.querier.builder.Buildable;

/**
 * A restriction represents conditions to be used on the query
 * 
 * @author Ivan Perales
 *
 */
public abstract class Restriction implements Buildable{

	/** Query object that holds the projection */
	Query query;
	String property;
	Object value;
	
	public Restriction(String property, Object value){
		this.property = property;
		this.value = value;
	}
	
	/**
	 * Method to assign the query object. This is intented to be called only once.
	 */
	public void setQuery(Query query){
		this.query = query;
	}
	
	/**
	 * Obtain the query object. It is protected for it use just for subclasses.
	 */
	protected Query getQuery(){
		return query;
	}
	
	protected String getProperty(){
		return property;
	}
	
	public Object getValue(){
		return value;
	}
}
