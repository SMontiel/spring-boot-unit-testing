package com.aipisoft.querier.projection;

/**
 * Factory class to get projections.
 * 
 * @author Ivan Perales
 *
 */
public class Projections {

	public static Projection property(String property){
		return new PropertyProjection(property);
	}
	
	public static Projection property(String property, String alias){
		return new PropertyProjection(property, alias);
	}
	
	public static Projection count(String property){
		return new AggregateProjection(AggregateProjection.AGGREGATE.COUNT, property);
	}
	
	public static Projection count(String property, String alias){
		return new AggregateProjection(AggregateProjection.AGGREGATE.COUNT, property, alias);
	}
	
	public static Projection sum(String property){
		return new AggregateProjection(AggregateProjection.AGGREGATE.SUM, property);
	}
	
	public static Projection sum(String property, String alias){
		return new AggregateProjection(AggregateProjection.AGGREGATE.SUM, property, alias);
	}
	
	public static Projection max(String property){
		return new AggregateProjection(AggregateProjection.AGGREGATE.MAX, property);
	}
	
	public static Projection max(String property, String alias){
		return new AggregateProjection(AggregateProjection.AGGREGATE.MAX, property, alias);
	}
	
	public static Projection min(String property){
		return new AggregateProjection(AggregateProjection.AGGREGATE.MIN, property);
	}
	
	public static Projection min(String property, String alias){
		return new AggregateProjection(AggregateProjection.AGGREGATE.MIN, property, alias);
	}
	
	public static Projection distinct(String property){
		return new AggregateProjection(AggregateProjection.AGGREGATE.DISTINC, property);
	}
	
	public static Projection distinct(String property, String alias){
		return new AggregateProjection(AggregateProjection.AGGREGATE.DISTINC, property, alias);
	}
	
	public static Projection coalesce(String property, String[] values){
		return new CoalesceProjection(property, values);
	}
	
	public static Projection coalesce(String property, String alias, String[] values){
		return new CoalesceProjection(property, alias, values);
	}
	
	public static Projection free(String sql, String alias){
		return new FreeProjection(sql, alias);
	}
	
	public static Projection free(String sql, String alias, Object... values){
		return new FreeProjection(sql, alias, values);
	}
	
	public static ProjectionList createList(){
		return new ProjectionList();
	}
}
