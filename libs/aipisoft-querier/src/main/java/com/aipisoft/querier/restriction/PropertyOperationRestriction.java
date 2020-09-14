package com.aipisoft.querier.restriction;

import com.aipisoft.querier.builder.Builder;


/**
 * As same as OperationRestriction, with the difference vs another property but a value.
 * 
 * @author Ivan Perales
 *
 */
public class PropertyOperationRestriction extends OperationRestriction{
	
	String secondProperty;

	public PropertyOperationRestriction(String firstProp, String secondProp, OperationRestriction.OPERATOR operator){
		super(firstProp, null, operator);
		this.secondProperty = secondProp;
	}
	
	@Override public void build(Builder builder){
		builder.append(getQuery().getFullProperty(getProperty()));
		builder.append(" ");
		builder.append(operator.getSign());
		builder.append(" ");
		builder.append(getQuery().getFullProperty(secondProperty));
	}
}
