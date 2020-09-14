package com.aipisoft.querier.restriction;

import java.util.List;

import com.aipisoft.querier.Helper;
import com.aipisoft.querier.builder.Builder;
import com.aipisoft.querier.restriction.OperationRestriction.OPERATOR;

public class AnyRestriction extends Restriction{

	OPERATOR operator;
	
	public AnyRestriction(String property, Object value, OPERATOR operator) {
		super(property, value);
		this.operator = operator;
	}

	@Override public void build(Builder builder) {
		List<Object> values = Helper.getList(getValue());
		
		//si la lista esta vacia no hay condicion in
		if(values.size() == 0){
			throw new RuntimeException("La lista de valores para la condicion 'any' no debe estar vacia");
		}
		
		builder.append(getQuery().getFullProperty(getProperty()));
		builder.append(" ");
		builder.append(operator.getSign());
		builder.append(" any (values ");
		
		for(int i=0; i<values.size(); i++){
			if(i > 0){
				builder.append(",");
			}
			builder.append("(?)");
			builder.addArg(values.get(i));
		}
		
		builder.append(")");
	}

}
