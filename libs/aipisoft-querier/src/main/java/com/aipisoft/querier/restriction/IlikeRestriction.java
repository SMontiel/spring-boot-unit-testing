package com.aipisoft.querier.restriction;

import com.aipisoft.querier.builder.Builder;


public class IlikeRestriction extends LikeRestriction{

	public IlikeRestriction(String property, Object value, LikeRestriction.LIKE like) {
		super(property, value, like);
	}

	@Override public void build(Builder builder){
		builder.append(getQuery().getFullProperty(getProperty()));
		builder.append(" ilike ?");
		
		switch(getLike()){
			case EQUAL:
				builder.addArg(getValue().toString());
				break;
			case START:
				builder.addArg(getValue().toString() + "%");
				break;
			case END:
				builder.addArg("%" + getValue().toString());
				break;
			case ANYWHERE: 
				builder.addArg("%" + getValue().toString() + "%");
		}
	}
}
