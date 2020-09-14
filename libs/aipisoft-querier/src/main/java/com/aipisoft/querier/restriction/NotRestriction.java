package com.aipisoft.querier.restriction;

import com.aipisoft.querier.Query;
import com.aipisoft.querier.builder.Builder;


public class NotRestriction extends Restriction{

	Restriction restriction;

	public NotRestriction(Restriction restriction){
		super(null, null);
		this.restriction = restriction;
	}
	
	@Override public void setQuery(Query query){
		super.setQuery(query);
		restriction.setQuery(query);
	}

	@Override public void build(Builder builder){
		builder.append("not (");
		restriction.build(builder);
		builder.append(")");
	}
}
