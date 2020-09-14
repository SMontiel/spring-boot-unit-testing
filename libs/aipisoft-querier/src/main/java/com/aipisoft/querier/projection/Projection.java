package com.aipisoft.querier.projection;

import com.aipisoft.querier.Query;
import com.aipisoft.querier.builder.Buildable;

/**
 * A projection for the query. The projection is the information that is queried with the select statement.
 * 
 * @author Ivan Perales
 *
 */
public abstract class Projection implements Buildable{

	/** The query holding the projection */
	private Query query;
	
	/** An alias for the projection */
	private String alias;
	
	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}
	
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}
