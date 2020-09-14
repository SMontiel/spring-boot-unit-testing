package com.aipisoft.querier.table;

import com.aipisoft.querier.builder.Builder;

public class DeleteTable extends Table{
	
	public DeleteTable(String name){
		super(name, name);
	}

	@Override public void build(Builder builder){
		builder.append("from ");
		builder.append(getQuery().getSchema());
		builder.append(".");
		builder.append(getName());
	}
}