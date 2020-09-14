package com.aipisoft.querier.order;

import com.aipisoft.querier.Query;
import com.aipisoft.querier.builder.Buildable;
import com.aipisoft.querier.builder.Builder;


public class Order implements Buildable{

	public static enum DIR{
		ASC, DESC;
	}
	
	Query query;
	String property;
	DIR dir;
	boolean addIdentifier;
	
	public Order(Query query, String property, DIR dir){
		this(query, property, dir, true);
	}
	
	public Order(Query query, String property, DIR dir, boolean addIdentifier){
		this.query = query;
		this.property = property;
		this.dir = dir;
		this.addIdentifier = addIdentifier;
	}

	public String getProperty() {
		return property;
	}

	public DIR getDir() {
		return dir;
	}
	
	@Override public void build(Builder builder){
		if(addIdentifier){
			builder.append(query.getFullProperty(property));
		} else{
			builder.append(property);
		}
		
		if(getDir() == DIR.DESC){
			builder.append(" desc");
		}
	}
}
