package com.aipisoft.querier.builder;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aipisoft.querier.Query;
import com.aipisoft.querier.order.Order;
import com.aipisoft.querier.table.InsertTable;
import com.aipisoft.querier.table.JoinTable;
import com.aipisoft.querier.table.PropertyValue;
import com.aipisoft.querier.table.Table;
import com.aipisoft.querier.table.UpdateTable;

public class DefaultBuilder implements Builder{
	
	static final Log logger = LogFactory.getLog(DefaultBuilder.class);

	public final static String SPACE = " ";
	StringBuilder sql;
	List<Object> args;
	List<Integer> argsType;
	
	/**
	 * The query agains we build
	 * @param query
	 */
	public DefaultBuilder(){
		this.sql = new StringBuilder();
		this.args = new ArrayList<Object>();
		this.argsType = new ArrayList<Integer>();
	}
	
	private void space(){
		sql.append(SPACE);
	}
	
	@Override public void build(Query query){
		sql.append(query.getAction().getType().getName());
		
		if(query.getProjection() != null){
			space();
			query.getProjection().build(this);
		}
		
		space();
		Table target = query.getTarget();
		
		if(target != null) {
			target.build(this);
			
			if(target instanceof InsertTable){
				append(" (");
				for(int i=0; i<query.getValues().size(); i++){
					if(i > 0){
						append(", ");
					}
					query.getValues().get(i).buildProperty(this);
				}
				append(") values (");
				for(int i=0; i<query.getValues().size(); i++){
					if(i > 0){
						append(", ");
					}
					query.getValues().get(i).build(this);
				}
				append(")");
				
			} else if(target instanceof UpdateTable){
				space();
				for(int i=0; i<query.getValues().size(); i++){
					if(i > 0){
						append(", ");
					}
					PropertyValue pv = query.getValues().get(i);
					pv.buildProperty(this);
					append(" = ");
					pv.build(this);
				}
			}
		} else {
			append("from (");
			build(query.getSubQuery());
			append(") as ");
			append(query.getSubQueryAlias());
		}
		
		if(query.getJoins().size() > 0){
			for(JoinTable join : query.getJoins()){
				space();
				join.build(this);
			}
		}
		
		if(query.getRestrictions().size() > 0){
			space();
			append("where");
			
			for(int i=0; i<query.getRestrictions().size(); i++){
				if(i > 0){
					space();
					append("and");
				}
				space();
				query.getRestrictions().get(i).build(this);
			}
		}
		
		if(query.getGroups().size() > 0){
			space();
			append("group by");
			space();
			
			for(int i=0; i<query.getGroups().size(); i++){
				String prop = query.getGroups().get(i);
				if(i > 0){
					append(", ");
				}
				append(prop);
			}
		}
		
		if(query.getHavings().size() > 0){
			space();
			append("having");
			
			for(int i=0; i<query.getHavings().size(); i++){
				if(i > 0){
					space();
					append("and");
				}
				space();
				query.getHavings().get(i).build(this);
			}
		}
		
		if(query.getOrders().size() > 0){
			space();
			append("order by");
			space();
			
			for(int i=0; i<query.getOrders().size(); i++){
				Order order = query.getOrders().get(i);
				if(i > 0){
					append(",");
				}
				order.build(this);
			}
		}
		
		if(query.getPageSize() > 0){
			space();
			append("limit");
			append(" ?");
			addArg(new Integer(query.getPageSize()));
			
			if(query.getPageNumber() > 0){
				space();
				append("offset");
				append(" ?");
				addArg(new Integer(query.getPageSize() * query.getPageNumber()));
			}
		}
	}
	
	@Override public void append(String str){
		sql.append(str);
	}
	
	@Override public void addArg(Object arg){
		if(arg == null){
			addArg(arg, java.sql.Types.NULL);
			
		} else if(arg instanceof String){
			addArg(arg, java.sql.Types.VARCHAR);
			
		} else if(arg instanceof Integer){
			addArg(arg, java.sql.Types.INTEGER);
			
		} else if(arg instanceof Long){
			addArg(arg, java.sql.Types.BIGINT);
			
		} else if(arg instanceof Double){
			addArg(arg, java.sql.Types.DOUBLE);
			
		} else if(arg instanceof BigDecimal){
			addArg(arg, java.sql.Types.NUMERIC);
			
		} else if(arg instanceof Date){
			addArg(arg, java.sql.Types.DATE);
			
		} else if(arg instanceof Time){
			addArg(arg, java.sql.Types.TIME);
			
		} else if(arg instanceof Timestamp){
			addArg(arg, java.sql.Types.TIMESTAMP);
			
		} else if(arg instanceof java.util.Date){
			addArg(new Timestamp(((java.util.Date)arg).getTime()), java.sql.Types.TIMESTAMP);
			
		} else if(arg instanceof LocalDate){
			addArg(Date.valueOf((LocalDate)arg), java.sql.Types.DATE);
			
		} else if(arg instanceof LocalTime){
			addArg(Time.valueOf((LocalTime)arg), java.sql.Types.TIME);
			
		} else if(arg instanceof LocalDateTime){
			addArg(Timestamp.valueOf((LocalDateTime)arg), java.sql.Types.TIMESTAMP);
			
		} else if(arg instanceof Boolean){
			addArg(arg, java.sql.Types.BOOLEAN);
			
		} else if(arg instanceof Byte){
			addArg(arg, java.sql.Types.BINARY);
			
		} else if(arg instanceof byte[]){
			addArg(arg, java.sql.Types.BINARY);
			
		}
	}
	
	private void addArg(Object arg, int type){
		args.add(arg);
		argsType.add(type);
	}
	
	@Override public String getSql(){
		logger.debug("Query: " + sql.toString());
		return sql.toString();
	}
	
	@Override public Object[] getArgs(){
		if(logger.isDebugEnabled()){
			StringBuilder log = new StringBuilder("Values: (");
			
			for(int i=0; i<args.size(); i++){
				if(i > 0){
					log.append(", ");
				}
				Object arg = args.get(i);
				if(arg == null){
					log.append("null");
					
				} else if(arg instanceof byte[]){
					log.append("[byte array]");
					
				} else if(arg instanceof String){
					log.append("'");
					log.append(arg);
					log.append("'");
				} else{
					log.append(arg);
				}
			}
			log.append(")");
			logger.debug(log);
		}
		return args.toArray();
	}
	
	@Override public int[] getTypes(){
		int[] types = new int[argsType.size()];
		
		for(int i=0; i<argsType.size(); i++){
			types[i] = argsType.get(i).intValue();
		}
		
		return types;
	}
}
