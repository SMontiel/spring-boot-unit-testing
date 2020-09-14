package com.aipisoft.querier.restriction;

import com.aipisoft.querier.builder.Builder;


public class NullRestriction extends Restriction{

	public NullRestriction(String property) {
		super(property, null);
	}
	
	@Override public void build(Builder builder){
		builder.append(getQuery().getFullProperty(getProperty()));
		builder.append(" is null");
	}
}
