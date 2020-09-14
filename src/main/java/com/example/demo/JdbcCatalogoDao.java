package com.example.demo;

import com.aipisoft.common.querier.Filter;
import com.aipisoft.common.querier.Order;
import com.aipisoft.querier.Query;
import com.example.demo.utils.Filters;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.function.Function;

public abstract class JdbcCatalogoDao<T> extends JdbcDefaultDao {

	protected abstract Query getCountQuery();
	protected abstract Query getSelectQuery();
	protected abstract RowMapper<T> getRowMapper();

	public int obtenerCantidad(List<Filter> filters) {
		Query query = getCountQuery();
		restrictSelect(query, filters);
		return integer(query);
	}

	public List<T> obtenerLista(Function<Query, Query> fnQuery, List<Filter> filters, List<Order> orders) {
		return obtenerLista(fnQuery, filters, orders, -1, -1);
	}

	public List<T> obtenerLista(Function<Query, Query> fnQuery, List<Filter> filters, List<Order> orders, int pageSize, int pageNumber) {
		Query query = getSelectQuery();
		query.setPageSize(pageSize).setPageNumber(pageNumber);

		query = fnQuery.apply(query);

		restrictSelect(query, filters);
		orderSelect(query, orders);

		return list(query, getRowMapper());
	}

	public List<T> obtenerLista() {
		return obtenerLista(null, null, -1, -1);
	}

	public List<T> obtenerLista(List<Filter> filters) {
		return obtenerLista(filters, null, -1, -1);
	}

	public List<T> obtenerLista(List<Filter> filters, List<Order> orders) {
		return obtenerLista(filters, orders, -1, -1);
	}

	public List<T> obtenerLista(List<Filter> filters, List<Order> orders, int pageSize, int pageNumber){
		Query query = getSelectQuery();
		query.setPageSize(pageSize).setPageNumber(pageNumber);

		restrictSelect(query, filters);
		orderSelect(query, orders);

		return list(query, getRowMapper());
	}

	public T obtener(List<Filter> filters) {
		return single(obtenerLista(filters, null, -1, -1));
	}

	public T obtenerPorId(int id) {
		return obtener(Filters.on().id(id).get());
	}
}
