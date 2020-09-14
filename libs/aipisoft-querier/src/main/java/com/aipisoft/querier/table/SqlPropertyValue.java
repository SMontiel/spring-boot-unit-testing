package com.aipisoft.querier.table;

import org.apache.commons.lang3.StringUtils;

import com.aipisoft.querier.Helper;
import com.aipisoft.querier.Query;
import com.aipisoft.querier.builder.Builder;

public class SqlPropertyValue extends PropertyValue {
	private Query query;
	private String sql;
	
	public SqlPropertyValue(Query query, String fullProperty, String sql){
		this(query, Helper.getTableAlias(fullProperty), Helper.getPropertyName(fullProperty), sql, null);
	}
	
	public SqlPropertyValue(Query query, String fullProperty, String sql, Object value){
		this(query, Helper.getTableAlias(fullProperty), Helper.getPropertyName(fullProperty), sql, value);
	}
	
	public SqlPropertyValue(Query query, String tableAlias, String property, String sql){
		this(query, tableAlias, property, sql, null);
	}
	
	public SqlPropertyValue(Query query, String tableAlias, String property, String sql, Object value){
		super(query, tableAlias, property, value);
		this.query = query;
		this.sql = sql;
	}
	
	@Override public void build(Builder builder){
		String _sql = StringUtils.replace(sql, "[]", query.getSchema());
		builder.append("(");
		builder.append(_sql);
		builder.append(")");
		
		if(getValue() != null && getValue() instanceof Object[]) {
			for(Object val : (Object[])getValue()) {
				builder.addArg(val);
			}
		} else if(getValue() != null) {
			builder.addArg(getValue());
		}
	}
}