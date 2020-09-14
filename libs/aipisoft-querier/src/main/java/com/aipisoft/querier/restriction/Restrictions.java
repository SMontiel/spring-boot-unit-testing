package com.aipisoft.querier.restriction;

/**
 * Factory method to get restrictions
 * 
 * @author Ivan Perales
 *
 */
public class Restrictions {

	public static Restriction eq(String property, Object value){
		return new OperationRestriction(property, value, OperationRestriction.OPERATOR.EQ);
	}
	
	public static Restriction gt(String property, Object value){
		return new OperationRestriction(property, value, OperationRestriction.OPERATOR.GT);
	}
	
	public static Restriction ge(String property, Object value){
		return new OperationRestriction(property, value, OperationRestriction.OPERATOR.GE);
	}
	
	public static Restriction lt(String property, Object value){
		return new OperationRestriction(property, value, OperationRestriction.OPERATOR.LT);
	}
	
	public static Restriction le(String property, Object value){
		return new OperationRestriction(property, value, OperationRestriction.OPERATOR.LE);
	}
	
	public static Restriction likeEq(String property, Object value){
		return new LikeRestriction(property, value, LikeRestriction.LIKE.EQUAL);
	}
	
	public static Restriction likeSt(String property, Object value){
		return new LikeRestriction(property, value, LikeRestriction.LIKE.START);
	}
	
	public static Restriction likeEnd(String property, Object value){
		return new LikeRestriction(property, value, LikeRestriction.LIKE.END);
	}
	
	public static Restriction likeAny(String property, Object value){
		return new LikeRestriction(property, value, LikeRestriction.LIKE.ANYWHERE);
	}
	
	public static Restriction ilikeEq(String property, Object value){
		return new IlikeRestriction(property, value, LikeRestriction.LIKE.EQUAL);
	}
	
	public static Restriction ilikeSt(String property, Object value){
		return new IlikeRestriction(property, value, LikeRestriction.LIKE.START);
	}
	
	public static Restriction ilikeEnd(String property, Object value){
		return new IlikeRestriction(property, value, LikeRestriction.LIKE.END);
	}
	
	public static Restriction ilikeAny(String property, Object value){
		return new IlikeRestriction(property, value, LikeRestriction.LIKE.ANYWHERE);
	}
	
	public static Restriction bw(String property, Object firstValue, Object secondValue){
		return new BwRestriction(property, firstValue, secondValue);
	}
	
	public static Restriction in(String property, Object... value){
		return new InRestriction(property, value);
	}
	
	public static Restriction in(String property, Object value){
		return new InRestriction(property, value);
	}
	
	public static Restriction anyEq(String property, Object value){
		return new AnyRestriction(property, value, OperationRestriction.OPERATOR.EQ);
	}
	
	public static Restriction anyGt(String property, Object value){
		return new AnyRestriction(property, value, OperationRestriction.OPERATOR.GT);
	}
	
	public static Restriction anyGe(String property, Object value){
		return new AnyRestriction(property, value, OperationRestriction.OPERATOR.GE);
	}
	
	public static Restriction anyLt(String property, Object value){
		return new AnyRestriction(property, value, OperationRestriction.OPERATOR.LT);
	}
	
	public static Restriction anyLe(String property, Object value){
		return new AnyRestriction(property, value, OperationRestriction.OPERATOR.LE);
	}
	
	public static Restriction isNull(String property){
		return new NullRestriction(property);
	}
	
	public static Restriction isNotNull(String property){
		return not(new NullRestriction(property));
	}
	
	public static Restriction not(Restriction restriction){
		return new NotRestriction(restriction);
	}
	
	public static Restriction and(Restriction value1, Restriction value2){
		return new LogicalRestriction(value1, value2, LogicalRestriction.TYPE.AND);
	}
	
	public static Restriction or(Restriction value1, Restriction value2){
		return new LogicalRestriction(value1, value2, LogicalRestriction.TYPE.OR);
	}
	
	public static Restriction eqProp(String firstProp, String secondProp){
		return new PropertyOperationRestriction(firstProp, secondProp, OperationRestriction.OPERATOR.EQ);
	}
	
	public static Restriction gtProp(String firstProp, String secondProp){
		return new PropertyOperationRestriction(firstProp, secondProp, OperationRestriction.OPERATOR.GT);
	}
	
	public static Restriction geProp(String firstProp, String secondProp){
		return new PropertyOperationRestriction(firstProp, secondProp, OperationRestriction.OPERATOR.GE);
	}
	
	public static Restriction ltProp(String firstProp, String secondProp){
		return new PropertyOperationRestriction(firstProp, secondProp, OperationRestriction.OPERATOR.LT);
	}
	
	public static Restriction leProp(String firstProp, String secondProp){
		return new PropertyOperationRestriction(firstProp, secondProp, OperationRestriction.OPERATOR.LE);
	}
	
	public static Restriction sql(String sql){
		return new SqlRestriction(sql);
	}
	
	public static Restriction sql(String sql, Object... params){
		return new SqlRestriction(sql, params);
	}
}
