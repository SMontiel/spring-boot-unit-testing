package com.aipisoft.querier.restriction;

import com.aipisoft.querier.builder.Builder;


public class LikeRestriction extends Restriction{
	
	public static enum LIKE{
		EQUAL,
		START,
		END,
		ANYWHERE;
	}
	
	LIKE like;

	public LikeRestriction(String property, Object value, LIKE like) {
		super(property, value);
		this.like = like;
	}
	
	protected LIKE getLike(){
		return like;
	}

	@Override public void build(Builder builder){
		builder.append("lower(" + getQuery().getFullProperty(getProperty()) + ")");
		builder.append(" like ?");
		
		switch(getLike()){
			case EQUAL:
				builder.addArg(getValue().toString().toLowerCase());
				break;
			case START:
				builder.addArg(getValue().toString().toLowerCase() + "%");
				break;
			case END:
				builder.addArg("%" + getValue().toString().toLowerCase());
				break;
			case ANYWHERE: 
				builder.addArg("%" + getValue().toString().toLowerCase() + "%");
		}
	}
}
