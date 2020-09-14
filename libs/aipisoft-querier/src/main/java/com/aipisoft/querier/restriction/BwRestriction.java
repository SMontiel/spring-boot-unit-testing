package com.aipisoft.querier.restriction;

import com.aipisoft.querier.builder.Builder;


/**
 * Between restriction
 * @author Ivan Perales
 *
 */
public class BwRestriction extends Restriction{
	
	Object secondValue;

	public BwRestriction(String path, Object firstValue, Object secondValue) {
		super(path, firstValue);
		this.secondValue = secondValue;
	}

	@Override public void build(Builder builder){
		builder.append(getQuery().getFullProperty(getProperty()));
		builder.append(" between ? and ?");
		builder.addArg(getValue());
		builder.addArg(secondValue);
	}
}
