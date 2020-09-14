package com.aipisoft.querier.table;

import com.aipisoft.querier.builder.Builder;

/**
 * Update table. The alias of this table is the same name of the table
 * @author Ivan Perales
 *
 */
public class UpdateTable extends Table{
	
	public UpdateTable(String name){
		super(name, name);
	}

	@Override public void build(Builder builder){
		builder.append(getQuery().getSchema());
		builder.append(".");
		builder.append(getName());
		builder.append(" set");
	}
}
