package com.aipisoft.querier.builder;


/**
 * Interfaz to be implemented by objects that build some kind of sql snipet
 * 
 * @author Ivan Perales
 *
 */
public interface Buildable {

	void build(Builder builder);
}
