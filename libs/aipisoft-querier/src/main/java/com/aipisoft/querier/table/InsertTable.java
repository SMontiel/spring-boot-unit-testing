package com.aipisoft.querier.table;

import com.aipisoft.querier.builder.Builder;

public class InsertTable extends Table{
	
	public InsertTable(String name){
		super(name, name);
	}

	@Override public void build(Builder builder){
		builder.append("into ");
		builder.append(getQuery().getSchema());
		builder.append(".");
		builder.append(getName());
	}
}
