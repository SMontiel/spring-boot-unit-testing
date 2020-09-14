package com.aipisoft.querier.table;

import com.aipisoft.querier.Query;
import com.aipisoft.querier.builder.Buildable;


/**
 * Object that represents a table
 * 
 * @author Ivan Perales
 *
 */
public abstract class Table implements Buildable{
	private Query query;
	private String name;
	private String alias;
	
	public Table(){
		this(null, null);
	}
	
	public Table(String name, String alias){
		this.name = name;
		this.alias = alias;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}
