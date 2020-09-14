package com.aipisoft.querier.restriction;

import org.apache.commons.lang3.StringUtils;

import com.aipisoft.querier.builder.Builder;

/**
 * SQL restriction
 * @author Ivan Perales
 *
 */
public class SqlRestriction extends Restriction{
	
	String sql;
	Object[] params;

	public SqlRestriction(String sql) {
		this(sql, (Object[])null);
	}
	
	public SqlRestriction(String sql, Object... params) {
		super(null, null);
		this.sql = sql;
		this.params = params;
	}

	@Override public void build(Builder builder){
		String _sql = StringUtils.replace(sql, "[]", getQuery().getSchema());
		builder.append(_sql);
		
		if(params != null && params.length > 0){
			for(int i=0; i<params.length; i++){
				builder.addArg(params[i]);
			}
		}
	}
}
