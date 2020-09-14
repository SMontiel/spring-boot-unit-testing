package com.aipisoft.querier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aipisoft.querier.builder.Builder;
import com.aipisoft.querier.builder.DefaultBuilder;
import com.aipisoft.querier.order.Order;
import com.aipisoft.querier.projection.Projection;
import com.aipisoft.querier.restriction.Restriction;
import com.aipisoft.querier.table.DeleteTable;
import com.aipisoft.querier.table.FunctionPropertyValue;
import com.aipisoft.querier.table.InsertTable;
import com.aipisoft.querier.table.JoinTable;
import com.aipisoft.querier.table.PropertyValue;
import com.aipisoft.querier.table.SelectTable;
import com.aipisoft.querier.table.SequenceTable;
import com.aipisoft.querier.table.SqlPropertyValue;
import com.aipisoft.querier.table.Table;
import com.aipisoft.querier.table.UpdateTable;

/**
 * Query object that holds all criterias.
 * 
 * @author Ivan Perales
 *
 */
public class Query implements Queriable{
	
	private static List<String> DefaultAliases;
	
	static {
		DefaultAliases = new ArrayList<String>();
		String abc = "abcdefghijklmnopqrstuvwxyz";
		for(int i = 0; i<abc.length(); i++){
			DefaultAliases.add("_" + abc.charAt(i));
		}
	}
	
	private int lastDefaultAliasUsed = -1;

	private String schema;
	private Action action;
	private Table target;
	private Query subQuery;
	private String subQueryAlias;
	private Projection projection;
	private List<PropertyValue> values;
	private List<JoinTable> joins;
	private List<Restriction> restrictions;
	private List<String> groups;
	private List<Restriction> havings;
	private List<Order> orders;
	private int pageSize;
	private int pageNumber;
	private Versioning versioning;
	
	/* Map that hols the properties relationed to some alias (target table or join ones) */
	private Map<String,Set<String>> aliases;
	private Builder wrappedBuilder;
	
	/**
	 * Private constructor for Query object. It requires the action to be performed and the target table
	 * @param from The main table to query 
	 */
	private Query(String schema, Action action, Table target){
		this(schema, action, target, null, null);
	}
	
	private Query(String schema, Query subQuery, String subQueryAlias){
		this(schema, Action.select(), null, subQuery, subQueryAlias);
	}
	
	private Query(String schema, Action action, Table target, Query subQuery, String subQueryAlias){
		this.schema = schema;
		this.action = action;
		this.target = target;
		this.subQuery = subQuery;
		this.subQueryAlias = subQueryAlias;
		this.projection = null;
		this.values = new ArrayList<PropertyValue>();
		this.joins = new ArrayList<JoinTable>();
		this.restrictions = new ArrayList<Restriction>();
		this.groups = new ArrayList<String>();
		this.havings = new ArrayList<Restriction>();
		this.orders = new ArrayList<Order>();
		this.aliases = new HashMap<String, Set<String>>();
		
		if(target != null) {
			target.setQuery(this);
		}
	}
	
	public String nextAlias(){
		lastDefaultAliasUsed++;
		return Query.DefaultAliases.get(lastDefaultAliasUsed);
	}
	
	public String getSchema(){
		return schema;
	}
	
	public Action getAction(){
		return action;
	}
	
	/** Returns the target table of the query */
	public Table getTarget() {
		return target;
	}
	
	/** Returns the subquery for the select */
	public Query getSubQuery() {
		return subQuery;
	}
	
	public String getSubQueryAlias() {
		return subQueryAlias;
	}

	/** Returns the projection object of the query */
	public Projection getProjection() {
		return projection;
	}
	
	/** Returns the list of properties values of this query */
	public List<PropertyValue> getValues(){
		if(versioning != null) {
			List<PropertyValue> newValues = new ArrayList<PropertyValue>(values);
			newValues.add(versioning.getValue());
			return newValues;
		}
		return values;
	}

	/** Returns the list of joins of this query */
	public List<JoinTable> getJoins() {
		return joins;
	}

	/** Returns the list of restrictions of this query */
	public List<Restriction> getRestrictions() {
		if(versioning != null) {
			List<Restriction> newRestrictions = new ArrayList<Restriction>(restrictions);
			newRestrictions.add(versioning.getRestriction());
			return newRestrictions;
		}
		return restrictions;
	}

	/** Returns the list of grouping by of this query */
	public List<String> getGroups() {
		return groups;
	}
	
	/** Returns the list of havings of this query */
	public List<Restriction> getHavings() {
		return havings;
	}
	
	/** Returns the list of orders of this query */
	public List<Order> getOrders() {
		return orders;
	}
	
	/** Returns the page size results of the query  */
	public int getPageSize() {
		return pageSize;
	}

	/** Returns the number page for the query */
	public int getPageNumber() {
		return pageNumber;
	}
	
	/** Returns true if this query is executing optimistic concurrency control, false otherwise */
	public boolean isVersioning() {
		return versioning != null;
	}
	
	@Override public String getSql(){
		return build().getSql();
	}
	
	@Override public Object[] getArgs(){
		return build().getArgs();
	}
	
	@Override public int[] getTypes(){
		return build().getTypes();
	}
	
	private Builder build(){
		if(wrappedBuilder == null){
			wrappedBuilder = new DefaultBuilder();
			wrappedBuilder.build(this);
		}
		return wrappedBuilder;
	}
	
	/** Register the property name in the map of aliases using the table alias of it. If there is no table alias,
	 * then the alias of the target table of this query is used */
	public void registerProperty(String property){
		String alias = Helper.getTableAlias(property);
		
		if(alias == null){
			alias = target.getAlias();
		}
		
		Set<String> set = aliases.get(alias);
		if(set == null){
			set = new HashSet<String>();
			aliases.put(alias, set);
		}
		set.add(Helper.getPropertyName(property));
	}
	
	/** Returns the full property of the passed property (ie e.somProp). If if does not have then it is returned using the 
	 * table alias of the target table only if the action is Select, otherwise the property is returned as same */
	public String getFullProperty(String property){
		if(action.getType() != Action.TYPE.SELECT){
			return property;
		}
		
		if(Helper.getTableAlias(property) != null){
			return property;
		}
		
		for(String alias : aliases.keySet()){
			Set<String> set = aliases.get(alias);
			if(set.contains(property)){
				return alias + "." + property;
			}
		}
		
		//if there is no registered alias for the property, we return the target table alias
		return target.getAlias() + "." + property;
	}
	
	/*
	 * PROJECTIONS
	 */
	public Query setProjection(Projection projection){
		this.projection = projection;
		this.projection.setQuery(this);
		return this;
	}
	
	/*
	 * PROPERTY VALUES
	 */
	
	public Query addValue(String fullProperty, Object value){
		values.add(new PropertyValue(this, fullProperty, value));
		return this;
	}
	
	public Query addValue(String tableAlias, String propertyName, Object value){
		values.add(new PropertyValue(this, tableAlias, propertyName, value));
		return this;
	}
	
	public Query addFunctionValue(String fullProperty, String function, FunctionPropertyValue.PV... pvs){
		values.add(new FunctionPropertyValue(this, fullProperty, function, Arrays.asList(pvs)));
		return this;
	}
	
	public Query addFunctionValue(String tableAlias, String propertyName, String function, FunctionPropertyValue.PV... pvs){
		values.add(new FunctionPropertyValue(this, tableAlias, propertyName, function, Arrays.asList(pvs)));
		return this;
	}
	
	public Query addFunctionValue(String fullProperty, String function, List<FunctionPropertyValue.PV> pvs){
		values.add(new FunctionPropertyValue(this, fullProperty, function, pvs));
		return this;
	}
	
	public Query addFunctionValue(String tableAlias, String propertyName, String function, List<FunctionPropertyValue.PV> pvs){
		values.add(new FunctionPropertyValue(this, tableAlias, propertyName, function, pvs));
		return this;
	}
	
	public Query addSqlValue(String fullProperty, String sql){
		values.add(new SqlPropertyValue(this, fullProperty, sql));
		return this;
	}
	
	public Query addSqlValue(String fullProperty, String sql, Object value){
		values.add(new SqlPropertyValue(this, fullProperty, sql, value));
		return this;
	}
	
	public Query addSqlValue(String tableAlias, String propertyName, String sql){
		values.add(new SqlPropertyValue(this, tableAlias, propertyName, sql));
		return this;
	}
	
	public Query addSqlValue(String tableAlias, String propertyName, String sql, Object value){
		values.add(new SqlPropertyValue(this, tableAlias, propertyName, sql, value));
		return this;
	}
	
	/*
	 * JOINS
	 */
	public Query innerJoin(String fullTargetTable, String targetColumnName, String sourceFullProperty){
		return join(new JoinTable(this, fullTargetTable, targetColumnName, sourceFullProperty, JoinTable.TYPE.INNER)); 
	}
	
	public Query innerJoin(String targetTableName, String targetTableAlias, String targetColumnName, String sourceFullProperty){
		return join(new JoinTable(this, targetTableName, targetTableAlias, targetColumnName, sourceFullProperty, JoinTable.TYPE.INNER)); 
	}
	
	public Query fullJoin(String fullTargetTable, String targetColumnName, String sourceFullProperty){
		return join(new JoinTable(this, fullTargetTable, targetColumnName, sourceFullProperty, JoinTable.TYPE.FULL)); 
	}
	
	public Query fullJoin(String targetTableName, String targetTableAlias, String targetColumnName, String sourceFullProperty){
		return join(new JoinTable(this, targetTableName, targetTableAlias, targetColumnName, sourceFullProperty, JoinTable.TYPE.FULL)); 
	}
	
	public Query leftJoin(String fullTargetTable, String targetColumnName, String sourceFullProperty){
		return join(new JoinTable(this, fullTargetTable, targetColumnName, sourceFullProperty, JoinTable.TYPE.LEFT)); 
	}
	
	public Query leftJoin(String targetTableName, String targetTableAlias, String targetColumnName, String sourceFullProperty){
		return join(new JoinTable(this, targetTableName, targetTableAlias, targetColumnName, sourceFullProperty, JoinTable.TYPE.LEFT)); 
	}
	
	public Query rightJoin(String fullTargetTable, String targetColumnName, String sourceFullProperty){
		return join(new JoinTable(this, fullTargetTable, targetColumnName, sourceFullProperty, JoinTable.TYPE.RIGHT)); 
	}
	
	public Query rightJoin(String targetTableName, String targetTableAlias, String targetColumnName, String sourceFullProperty){
		return join(new JoinTable(this, targetTableName, targetTableAlias, targetColumnName, sourceFullProperty, JoinTable.TYPE.RIGHT)); 
	}
	
	public Query join(JoinTable join){
		joins.add(join);
		join.setQuery(this);
		return this;
	}
	
	/*
	 * RESTRICTIONS
	 */
	public Query add(Restriction restriction){
		restrictions.add(restriction);
		restriction.setQuery(this);
		return this;
	}
	
	/*
	 * GOUPING BY
	 */
	public Query addGroupBy(String property){
		groups.add(property);
		return this;
	}
	
	/*
	 * HAVING BY
	 */
	public Query addHaving(Restriction restriction){
		havings.add(restriction);
		restriction.setQuery(this);
		return this;
	}
	
	/*
	 * ORDERS
	 */
	public Query orderAsc(String property){
		orders.add(new Order(this, property, Order.DIR.ASC));
		return this;
	}
	
	public Query orderAsc(String property, boolean addIdentifier){
		orders.add(new Order(this, property, Order.DIR.ASC, addIdentifier));
		return this;
	}
	
	public Query orderDesc(String property){
		orders.add(new Order(this, property, Order.DIR.DESC));
		return this;
	}
	
	public Query orderDesc(String property, boolean addIdentifier){
		orders.add(new Order(this, property, Order.DIR.DESC, addIdentifier));
		return this;
	}
	
	/*
	 * OPTIMISTIC CONCURRENCY CONTROL
	 */
	public Query setVersioning(int version) {
		setVersioning("version", version);
		return this;
	}
	
	public Query setVersioning(String property, int version) {
		this.versioning = new Versioning(this, property, version);
		return this;
	}
	
	/*
	 * PAGINATION
	 */
	public Query setPageSize(int pageSize){
		this.pageSize = pageSize;
		return this;
	}
	
	public Query setPageNumber(int pageNumber){
		this.pageNumber = pageNumber;
		return this;
	}
	
	/* Para el schema public */
	public static Query select(String fullTableName){
		return new Query("public", Action.select(), new SelectTable(fullTableName));
	}
	
	public static Query select(Query subQuery, String alias){
		return new Query("public", subQuery, alias);
	}

	public static Query fromSequence(String sequenceName){
		return new Query("public", Action.select(), new SequenceTable(sequenceName));
	}
	
	public static Query insert(String tableName){
		return new Query("public", Action.insert(), new InsertTable(tableName));
	}
	
	public static Query update(String tableName){
		return new Query("public", Action.update(), new UpdateTable(tableName));
	}
	
	public static Query update(String tableName, int version){
		return new Query("public", Action.update(), new UpdateTable(tableName)).setVersioning(version);
	}
	
	public static Query delete(String tableName){
		return new Query("public", Action.delete(), new DeleteTable(tableName));
	}
	
	public static Query delete(String tableName, int version){
		return new Query("public", Action.delete(), new DeleteTable(tableName)).setVersioning(version);
	}
	
	/* Para empresas */
	public static Query select(int empresa, String fullTableName){
		return new Query(buildEmpresaSchema(empresa), Action.select(), new SelectTable(fullTableName));
	}
	
	public static Query select(int empresa, Query subQuery, String alias){
		return new Query(buildEmpresaSchema(empresa), subQuery, alias);
	}

	public static Query fromSequence(int empresa, String sequenceName){
		return new Query(buildEmpresaSchema(empresa), Action.select(), new SequenceTable(sequenceName));
	}
	
	public static Query insert(int empresa, String tableName){
		return new Query(buildEmpresaSchema(empresa), Action.insert(), new InsertTable(tableName));
	}
	
	public static Query update(int empresa, String tableName){
		return new Query(buildEmpresaSchema(empresa), Action.update(), new UpdateTable(tableName));
	}
	
	public static Query delete(int empresa, String tableName){
		return new Query(buildEmpresaSchema(empresa), Action.delete(), new DeleteTable(tableName));
	}
	
	public static String buildEmpresaSchema(int id){
		return "cofac_" + Integer.toString(id);
	}
	
	/* Para cualquier schema */
	public static Query select(String schema, String fullTableName){
		return new Query(schema, Action.select(), new SelectTable(fullTableName));
	}
	
	public static Query select(String schema, Query subQuery, String alias){
		return new Query(schema, subQuery, alias);
	}

	public static Query fromSequence(String schema, String sequenceName){
		return new Query(schema, Action.select(), new SequenceTable(sequenceName));
	}
	
	public static Query insert(String schema, String tableName){
		return new Query(schema, Action.insert(), new InsertTable(tableName));
	}
	
	public static Query update(String schema, String tableName){
		return new Query(schema, Action.update(), new UpdateTable(tableName));
	}
	
	public static Query delete(String schema, String tableName){
		return new Query(schema, Action.delete(), new DeleteTable(tableName));
	}
}
