package com.example.demo.utils;

import com.aipisoft.common.querier.Filter;
import com.aipisoft.common.querier.FilterFactory;

import java.util.ArrayList;
import java.util.List;

public class Filters {

	List<Filter> filters;

	private Filters() {
		filters = new ArrayList<Filter>();
	}

	public static Filters on() {
		return new Filters();
	}

	public List<Filter> get(){
		return filters;
	}

	public Filters eq(String prop, Object value){
		filters.add(FilterFactory.eq(prop, value));
		return this;
	}

	public Filters gt(String prop, Object value){
		filters.add(FilterFactory.gt(prop, value));
		return this;
	}

	public Filters gte(String prop, Object value){
		filters.add(FilterFactory.gte(prop, value));
		return this;
	}

	public Filters lt(String prop, Object value){
		filters.add(FilterFactory.lt(prop, value));
		return this;
	}

	public Filters lte(String prop, Object value){
		filters.add(FilterFactory.lte(prop, value));
		return this;
	}

	public Filters sw(String prop, Object value){
		filters.add(FilterFactory.sw(prop, value));
		return this;
	}

	public Filters ew(String prop, Object value){
		filters.add(FilterFactory.ew(prop, value));
		return this;
	}

	public Filters con(String prop, Object value){
		filters.add(FilterFactory.con(prop, value));
		return this;
	}

	public Filters in(String prop, List list){
		filters.add(FilterFactory.in(prop, list));
		return this;
	}

	public Filters isNull(String prop){
		filters.add(FilterFactory.isNul(prop));
		return this;
	}

	public Filters notNull(String prop){
		filters.add(FilterFactory.notNull(prop));
		return this;
	}

	public Filters notEq(String prop, Object value){
		filters.add(FilterFactory.not_eq(prop, value));
		return this;
	}

	public Filters notGt(String prop, Object value){
		filters.add(FilterFactory.not_gt(prop, value));
		return this;
	}

	public Filters notGte(String prop, Object value){
		filters.add(FilterFactory.not_gte(prop, value));
		return this;
	}

	public Filters notLt(String prop, Object value){
		filters.add(FilterFactory.not_lt(prop, value));
		return this;
	}

	public Filters notLte(String prop, Object value){
		filters.add(FilterFactory.not_lte(prop, value));
		return this;
	}

	public Filters notSw(String prop, Object value){
		filters.add(FilterFactory.not_sw(prop, value));
		return this;
	}

	public Filters notEw(String prop, Object value){
		filters.add(FilterFactory.not_ew(prop, value));
		return this;
	}

	public Filters notCon(String prop, Object value){
		filters.add(FilterFactory.not_con(prop, value));
		return this;
	}

	public Filters bw(String prop, Object from, Object to){
		filters.add(FilterFactory.between(prop, from, to));
		return this;
	}

	public Filters anyEq(String prop, Object value){
		filters.add(FilterFactory.anyEq(prop, value));
		return this;
	}

	public Filters anyGt(String prop, Object value){
		filters.add(FilterFactory.anyGt(prop, value));
		return this;
	}

	public Filters anyGte(String prop, Object value){
		filters.add(FilterFactory.anyGte(prop, value));
		return this;
	}

	public Filters anyLt(String prop, Object value){
		filters.add(FilterFactory.anyLt(prop, value));
		return this;
	}

	public Filters anyLte(String prop, Object value){
		filters.add(FilterFactory.anyLte(prop, value));
		return this;
	}

	public Filters eqOp(String prop, String otherProp){
		filters.add(FilterFactory.eqOp(prop, otherProp));
		return this;
	}

	public Filters gtOp(String prop, String otherProp){
		filters.add(FilterFactory.gtOp(prop, otherProp));
		return this;
	}

	public Filters gteOp(String prop, String otherProp){
		filters.add(FilterFactory.gteOp(prop, otherProp));
		return this;
	}

	public Filters ltOp(String prop, String otherProp){
		filters.add(FilterFactory.ltOp(prop, otherProp));
		return this;
	}

	public Filters lteOp(String prop, String otherProp){
		filters.add(FilterFactory.lteOp(prop, otherProp));
		return this;
	}

	public Filters or(Filter a, Filter b) {
		filters.add(FilterFactory.or(a, b));
		return this;
	}

	//haxs
	public Filters id(int id){
		filters.add(FilterFactory.eq("e.id", id));
		return this;
	}

	public Filters eqOrNullInt(String prop, int id){
		filters.add(id == 0 ? FilterFactory.isNul(prop) : FilterFactory.eq(prop, id));
		return this;
	}
}
