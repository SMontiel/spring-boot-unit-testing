package com.aipisoft.querier.restriction;

import java.util.List;

import com.aipisoft.querier.Helper;
import com.aipisoft.querier.builder.Builder;

public class InRestriction extends Restriction{

	public InRestriction(String property, Object value) {
		super(property, value);
	}

	@Override public void build(Builder builder) {
		List<Object> values = Helper.getList(getValue());
		
		//si la lista esta vacia no hay condicion in
		if(values.size() == 0){
			throw new RuntimeException("La lista de valores para la condicion 'in' no debe estar vacia");
		}
		
		builder.append(getQuery().getFullProperty(getProperty()));
		builder.append(" in (");
		
		for(int i=0; i<values.size(); i++){
			if(i > 0){
				builder.append(",");
			}
			builder.append("?");
			builder.addArg(values.get(i));
		}
		
		builder.append(")");
	}

}
