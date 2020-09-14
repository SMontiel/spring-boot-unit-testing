package com.aipisoft.querier.table;

import com.aipisoft.querier.Query;
import com.aipisoft.querier.builder.Builder;

/**
 * Object that represents a table for querying results. Alias is required
 * @author Ivan Perales
 *
 */
public class SelectTable extends Table{
	/**
	 * Constructor of table indicatingthe fullName. Example: 'tableName as e'
	 * @param fullName
	 */
	public SelectTable(String fullName){
		fullName = fullName.trim();
		
		int index = fullName.indexOf(" as ");
		if(index > 0){
			setName(fullName.substring(0, index));
			setAlias(fullName.substring(index + 4));
			
		} else{
			index = fullName.indexOf(" ");
			
			if(index > 0){
				setName(fullName.substring(0, index));
				setAlias(fullName.substring(index + 1));
			} else{
				setName(fullName);
			}
		}
	}

	public SelectTable(String name, String alias){
		super(name, alias);
	}
	
	@Override public void setQuery(Query query){
		super.setQuery(query);
		
		if(getAlias() == null){
			setAlias(query.nextAlias());
		}
	}

	@Override public void build(Builder builder){
		builder.append("from ");
		builder.append(getQuery().getSchema());
		builder.append(".");
		builder.append(getName());
		
		builder.append(" as ");
		builder.append(getAlias());
	}
}
