package com.aipisoft.querier;

import com.aipisoft.querier.restriction.Restriction;
import com.aipisoft.querier.restriction.Restrictions;
import com.aipisoft.querier.table.PropertyValue;
import com.aipisoft.querier.table.SqlPropertyValue;

/**
 * Para poder realizar optimistic concurrency control. Sin embargo esto es solo para el update, el cliente ocupa
 * extraer la versión previamente.
 * El campo que se use como versión, debe ser un integer.
 * @author Ivan Perales
 *
 */
public class Versioning {

	private PropertyValue value;
	private Restriction restriction;
	
	Versioning(Query query, String property, int version){
		value = new SqlPropertyValue(query, property, property + "+1");
		restriction = Restrictions.eq(property, version);
	}

	public PropertyValue getValue() {
		return value;
	}

	public Restriction getRestriction() {
		return restriction;
	}
}
