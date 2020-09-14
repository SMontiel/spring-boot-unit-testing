package com.example.demo;

import com.aipisoft.querier.Query;
import com.aipisoft.querier.projection.Projections;
import com.aipisoft.querier.restriction.Restrictions;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * @author Salvador Montiel
 */
@Repository
public class StaffDao extends JdbcCatalogoDao<Staff> {

	public int create(Staff dto) {
		Assert.notNull(dto, "El dto es nulo");

		int id = integer(Query.fromSequence("staff_id_seq"));

		Query query = Query.insert("staff")
				.addValue("id", id)
				.addValue("name", dto.getName())
				.addValue("email", dto.getEmail());
		update(query);

		return id;
	}

	public void update(Staff dto) {
		Assert.notNull(dto, "El dto es nulo");

		Staff ori = obtenerPorId(dto.getId());
		Assert.notNull(ori, "El dto no existe con el id proporcionado");

		Query query = Query.update("staff")
				.addValue("name", dto.getName())
				.addValue("email", dto.getEmail())
				.add(Restrictions.eq("id", dto.getId()));
		update(query);
	}

	public void remove(int id) {
		Staff ori = obtenerPorId(id);
		Assert.notNull(ori, "El dto no existe con el id proporcionado");

		Query query = Query.delete("staff").add(Restrictions.eq("id", id));
		update(query);
	}

	@Override
	protected Query getCountQuery() {
		return Query.select("staff as e").setProjection(Projections.count("e.*"));
	}

	@Override
	protected Query getSelectQuery() {
		return Query.select("staff as e")
				.setProjection(Projections.createList()
						.add(Projections.property("e.id", "id"))
						.add(Projections.property("e.name", "name"))
						.add(Projections.property("e.email", "email"))
				);
	}

	@Override
	protected RowMapper<Staff> getRowMapper() {
		return (rs, rowNum) -> {
			Staff d = new Staff();
			d.setId(rs.getInt("id"));
			d.setName(rs.getString("name"));
			d.setEmail(rs.getString("email"));

			return d;
		};
	}
}
