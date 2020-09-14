package com.aipisoft.querier.table;

import com.aipisoft.querier.builder.Builder;

/**
 * Object that represents a sequence for fetching values
 * @author Ivan Perales
 *
 */
public class SequenceTable extends Table{

	
	public SequenceTable(String name){
		super(name, name);
	}

	@Override public void build(Builder builder){
		//h2 format
		//builder.append("next value for ");
		//builder.append(getName());
		
		//postgres format
		builder.append("nextval('");
		builder.append(getQuery().getSchema());
		builder.append(".");
		builder.append(getName());
		builder.append("')");
	}
}
