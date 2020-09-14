package com.aipisoft.querier.projection;

import com.aipisoft.querier.builder.Builder;

/**
 * Projection that represents an standar sql aggregate function.
 * 
 * @author Ivan Perales
 *
 */
public class AggregateProjection extends PropertyProjection{
	
	/** List of sql-defined aggregate functions */
	public static enum AGGREGATE{
		COUNT("count"),
		SUM("sum"),
		MAX("max"),
		MIN("min"),
		DISTINC("distinct");
		
		String name;
		
		AGGREGATE(String name){
			this.name = name;
		}
		
		public String getName(){
			return this.name;
		}
	}
	
	AGGREGATE aggregate;

	public AggregateProjection(AGGREGATE aggregate, String property){
		this(aggregate, property, null);
	}
	
	public AggregateProjection(AGGREGATE aggregate, String property, String alias){
		super(property, alias);
		this.aggregate = aggregate;
	}

	@Override public void build(Builder builder){
		builder.append(aggregate.getName());
		builder.append("(");
		builder.append(getQuery().getFullProperty(getProperty()));
		builder.append(") as ");
		builder.append(getAlias() != null ? getAlias() : aggregate.getName());
	}
}
