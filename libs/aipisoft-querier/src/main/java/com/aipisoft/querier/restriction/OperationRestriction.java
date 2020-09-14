package com.aipisoft.querier.restriction;

import com.aipisoft.querier.builder.Builder;


/**
 * Restriction for the five basic operations: equal, greater than, greater equals than, less than, less equals than.
 * 
 * @author Ivan Perales
 *
 */
public class OperationRestriction extends Restriction{
	
	public static enum OPERATOR{
		EQ("="),
		GT(">"),
		GE(">="),
		LT("<"),
		LE("<=");
		
		String sign;
		
		OPERATOR(String sign){
			this.sign = sign;
		}
		
		public String getSign(){
			return this.sign;
		}
	};
	
	OPERATOR operator;

	public OperationRestriction(String property, Object value, OPERATOR operator) {
		super(property, value);
		this.operator = operator;
	}

	protected OPERATOR getOperator() {
		return operator;
	}

	@Override public void build(Builder builder){
		builder.append(getQuery().getFullProperty(getProperty()));
		builder.append(" ");
		builder.append(operator.getSign());
		builder.append(" ");
		builder.append("?");
		
		builder.addArg(getValue());
	}

}
