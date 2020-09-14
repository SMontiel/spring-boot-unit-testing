package com.aipisoft.querier.builder;

import com.aipisoft.querier.Queriable;
import com.aipisoft.querier.Query;


/**
 * Interfaz to build the sql code. Overrides can implement vendor-specific functions.
 * 
 * @author Ivan Perales
 *
 */
public interface Builder extends Queriable{
	
	void build(Query query);
	void append(String str);
	void addArg(Object arg);
}
