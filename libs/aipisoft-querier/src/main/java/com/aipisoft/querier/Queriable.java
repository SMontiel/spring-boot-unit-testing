package com.aipisoft.querier;

/**
 * Interfaz to implement by objects that are queriable.
 * 
 * @author Ivan Perales
 *
 */
public interface Queriable {
	/** Returns a plain sql string */
	String getSql();
	
	/** Returns the array of values used in the sql string */
	Object[] getArgs();
	
	/** Returns the array on sql-types for the values */
	int[] getTypes();
}
