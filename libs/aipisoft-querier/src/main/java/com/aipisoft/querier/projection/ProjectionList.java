package com.aipisoft.querier.projection;

import java.util.ArrayList;
import java.util.List;

import com.aipisoft.querier.Query;
import com.aipisoft.querier.builder.Builder;

/**
 * Wrapper class to hold a list a projections, not just one.
 * 
 * @author Ivan Perales
 *
 */
public class ProjectionList extends Projection{

	List<Projection> projections;
	
	public ProjectionList(){
		projections = new ArrayList<Projection>();
	}
	
	public ProjectionList add(Projection projection){
		projections.add(projection);
		if(getQuery() !=  null) {
			projection.setQuery(getQuery());
		}
		return this;
	}
	
	@Override public void setQuery(Query query) {
		super.setQuery(query);
		
		for(int i=0; i<projections.size(); i++){
			projections.get(i).setQuery(query);
		}
	}

	@Override public void build(Builder builder){
		for(int i=0; i<projections.size(); i++){
			if(i > 0){
				builder.append(", ");
			}
			projections.get(i).build(builder);
		}
	}
}
