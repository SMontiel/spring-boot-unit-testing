package com.aipisoft.querier.restriction;

import com.aipisoft.querier.Query;
import com.aipisoft.querier.builder.Builder;

public class LogicalRestriction extends Restriction{
	
	public static enum TYPE{
		AND,
		OR;
	}
	
	Restriction value1;
	Restriction value2;
	TYPE type;
	
	public LogicalRestriction(Restriction value1, Restriction value2, TYPE type){
		super(null, null);
		
		this.value1 = value1;
		this.value2 = value2;
		this.type = type;
	}
	
	@Override public void setQuery(Query query){
		super.setQuery(query);
		value1.setQuery(query);
		value2.setQuery(query);
	}

	@Override public void build(Builder builder) {
		boolean val1 = value1 instanceof LogicalRestriction;
		boolean val2 = value2 instanceof LogicalRestriction;
		
		builder.append("(");
		if(!val1){ builder.append("("); }
		
		value1.build(builder);
		
		switch(type){
			case AND:
				if(!val1){ builder.append(")"); }
				builder.append(" and ");
				if(!val2){ builder.append("("); }
				break;
			case OR:
				if(!val1){ builder.append(")"); }
				builder.append(" or ");
				if(!val2){ builder.append("("); }
		}
		
		value2.build(builder);
		if(!val2){ builder.append(")"); }
		builder.append(")");
	}

}
