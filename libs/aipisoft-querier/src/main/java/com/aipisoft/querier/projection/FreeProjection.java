package com.aipisoft.querier.projection;

import org.apache.commons.lang3.StringUtils;

import com.aipisoft.querier.builder.Builder;

/**
 * Execute some sql query inside the select clause, Alias is mandatory
 * @author Ivan Perales
 *
 */
public class FreeProjection extends Projection{
	
	private String sql;
	private Object value;
	
	public FreeProjection(String sql, String alias){
		this(sql, alias, null);
	}
	
	public FreeProjection(String sql, String alias, Object value){
		this.sql = sql;
		this.value = value;
		setAlias(alias);
	}

	@Override public void build(Builder builder) {
		String _sql = StringUtils.replace(sql, "[]", getQuery().getSchema());
		builder.append(_sql);
		builder.append(" as ");
		builder.append(getAlias());
		
		if(value != null && value instanceof Object[]) {
			for(Object val : (Object[])value) {
				builder.addArg(val);
			}
		} else if(value != null) {
			builder.addArg(value);
		}
	}

}
