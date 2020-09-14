package com.example.demo;

import com.aipisoft.common.querier.Filter;
import com.aipisoft.common.querier.Order;
import com.aipisoft.common.util.NumericUtils;
import com.aipisoft.querier.Query;
import com.aipisoft.querier.restriction.Restriction;
import com.aipisoft.querier.restriction.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public abstract class JdbcDefaultDao extends JdbcDaoSupport {
	@Autowired private DataSource dataSource;

	@PostConstruct public void init() {
		setDataSource(dataSource);
	}

	protected void restrictSelect(Query query, List<Filter> filters){
		if (filters != null && filters.size() > 0) {
			for(Filter f : filters){
				query.add(getRestriction(f));
			}
		}
	}

	private Restriction getRestriction(Filter f){
		Restriction res = null;
		if (Filter.EQUALS == f.getOperator()) {
			res = Restrictions.eq(f.getProperty(), f.getValue());

		} else if (Filter.GREATHER_THAN == f.getOperator()) {
			res = Restrictions.gt(f.getProperty(), f.getValue());

		} else if (Filter.GREATHER_EQUALS_THAN == f.getOperator()) {
			res = Restrictions.ge(f.getProperty(), f.getValue());

		} else if (Filter.LESS_THAN == f.getOperator()) {
			res = Restrictions.lt(f.getProperty(), f.getValue());

		} else if (Filter.LESS_EQUALS_THAN == f.getOperator()) {
			res = Restrictions.le(f.getProperty(), f.getValue());

		} else if (Filter.STARTS_WITH == f.getOperator()) {
			res = Restrictions.likeSt(f.getProperty(), f.getValue());

		} else if (Filter.ENDS_WITH == f.getOperator()) {
			res = Restrictions.likeEnd(f.getProperty(), f.getValue());

		} else if (Filter.CONTAINS == f.getOperator()) {
			res = Restrictions.likeAny(f.getProperty(), f.getValue());

		} else if (Filter.IN == f.getOperator()) {
			res = Restrictions.in(f.getProperty(), f.getValue());

		} else if (Filter.BETWEEN == f.getOperator()) {
			//con el between debe ser un array de dos objetos
			Object[] arr = (Object[])f.getValue();
			res = Restrictions.bw(f.getProperty(), arr[0], arr[1]);

		} else if (Filter.NULL == f.getOperator()) {
			res = Restrictions.isNull(f.getProperty());

		} else if (Filter.NOT_NULL == f.getOperator()) {
			res = Restrictions.isNotNull(f.getProperty());

		} else if (Filter.ANY_EQ == f.getOperator()) {
			res = Restrictions.anyEq(f.getProperty(), f.getValue());

		} else if (Filter.ANY_GT == f.getOperator()) {
			res = Restrictions.anyGt(f.getProperty(), f.getValue());

		} else if (Filter.ANY_GE == f.getOperator()) {
			res = Restrictions.anyGe(f.getProperty(), f.getValue());

		} else if (Filter.ANY_LT == f.getOperator()) {
			res = Restrictions.anyLt(f.getProperty(), f.getValue());

		} else if (Filter.ANY_LE == f.getOperator()) {
			res = Restrictions.anyLe(f.getProperty(), f.getValue());

		} else if (Filter.EQUALS_OTHER_PROPERTY == f.getOperator()) {
			res = Restrictions.eqProp(f.getProperty(), (String)f.getValue());

		} else if (Filter.GREATHER_THAN_OTHER_PROPERTY == f.getOperator()) {
			res = Restrictions.gtProp(f.getProperty(), (String)f.getValue());

		} else if (Filter.GREATHER_EQUALS_THAN_OTHER_PROPERTY == f.getOperator()) {
			res = Restrictions.geProp(f.getProperty(), (String)f.getValue());

		} else if (Filter.LESS_THAN_OTHER_PROPERTY == f.getOperator()) {
			res = Restrictions.ltProp(f.getProperty(), (String)f.getValue());

		} else if (Filter.LESS_EQUALS_THAN_OTHER_PROPERTY == f.getOperator()) {
			res = Restrictions.leProp(f.getProperty(), (String)f.getValue());

		} else if(Filter.OR == f.getOperator()) {
			//vamos a suponer que al menos trae 2 elementos
			List<Filter> list = ((List<?>)f.getValue()).stream().map(e -> (Filter)e).collect(Collectors.toList());
			res = Restrictions.or(getRestriction(list.get(0)), getRestriction(list.get(1)));

			if(list.size() > 2){
				for(int i=2; i<list.size(); i++){
					res = Restrictions.or(res, getRestriction(list.get(i)));
				}
			}

		} else if(Filter.AND == f.getOperator()){
			List<Filter> list = ((List<?>)f.getValue()).stream().map(e -> (Filter)e).collect(Collectors.toList());
			res = Restrictions.and(getRestriction(list.get(0)), getRestriction(list.get(1)));

			if(list.size() > 2){
				for(int i=2; i<list.size(); i++){
					res = Restrictions.and(res, getRestriction(list.get(i)));
				}
			}
		}

		if(f.isInverted()){
			res = Restrictions.not(res);
		}
		return res;
	}

	protected void orderSelect(Query query, List<Order> orders){
		if(orders != null && orders.size() > 0){
			for(Order o : orders){
				if(o.getType() == Order.ASC){
					query.orderAsc(o.getProperty());
				} else{
					query.orderDesc(o.getProperty());
				}
			}
		}
	}

	protected void update(Query query){
		int affected = getJdbcTemplate().update(query.getSql(), query.getArgs(), query.getTypes());
		if(query.isVersioning() && affected == 0) {
			throw new RuntimeException("La tabla '"+query.getTarget().getName()+"' ha sido afectada por una transacción previa, realice la operación nuevamente");
		}
	}

	protected int integer(Query query){
		try{
			return getJdbcTemplate().queryForObject(query.getSql(), query.getArgs(), query.getTypes(), Integer.class);
		} catch(org.springframework.dao.EmptyResultDataAccessException ex){
			return 0;
		}
	}

	protected String string(Query query){
		try{
			return getJdbcTemplate().queryForObject(query.getSql(), query.getArgs(), query.getTypes(), String.class);
		} catch(org.springframework.dao.EmptyResultDataAccessException ex){
			return null;
		}
	}

	protected BigDecimal bigDecimal(Query query){
		try{
			BigDecimal valor = getJdbcTemplate().queryForObject(query.getSql(), query.getArgs(), query.getTypes(), BigDecimal.class);
			return NumericUtils.safeBg(valor);
		} catch(org.springframework.dao.EmptyResultDataAccessException ex){
			return NumericUtils.ZERO;
		}
	}

	protected LocalDate date(Query query){
		try{
			return getJdbcTemplate().queryForObject(query.getSql(), query.getArgs(), query.getTypes(), LocalDate.class);
		} catch(org.springframework.dao.EmptyResultDataAccessException ex){
			return null;
		}
	}

	protected <T> T single(String sql, Object[] args, RowMapper<T> mapper){
		List<T> list = getJdbcTemplate().query(sql, args, mapper);

		if(list.size() > 1){
			throw new RuntimeException("No se está devolviendo una sola instancia");
		}
		if(list.size() == 1){
			return list.get(0);
		}
		return null;
	}

	protected <T> T single(Query query, RowMapper<T> mapper){
		List<T> list = getJdbcTemplate().query(query.getSql(), query.getArgs(), query.getTypes(), mapper);

		if(list.size() > 1){
			throw new RuntimeException("No se está devolviendo una sola instancia");
		}
		if(list.size() == 1){
			return list.get(0);
		}
		return null;
	}

	protected <T> T single(List<T> list){
		if(list.size() > 1){
			throw new RuntimeException("No se está devolviendo una sola instancia");
		}
		if(list.size() == 1){
			return list.get(0);
		}
		return null;
	}

	protected <T> List<T> list(Query query, RowMapper<T> mapper){
		return getJdbcTemplate().query(query.getSql(), query.getArgs(), query.getTypes(), mapper);
	}

	protected List<String> listString(Query query){
		return getJdbcTemplate().queryForList(query.getSql(), query.getArgs(), query.getTypes(), String.class);
	}

	protected List<Integer> listInteger(Query query){
		return getJdbcTemplate().queryForList(query.getSql(), query.getArgs(), query.getTypes(), Integer.class);
	}

	protected LocalDate toLocalDate(Date date) {
		return date != null ? date.toLocalDate() : null;
	}

	protected LocalDateTime toTime(Timestamp time) {
		return time != null ? time.toLocalDateTime() : null;
	}
}
