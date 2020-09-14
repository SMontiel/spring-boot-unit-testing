package com.aipisoft.querier.table;

import com.aipisoft.querier.Helper;
import com.aipisoft.querier.Query;
import com.aipisoft.querier.builder.Builder;


/**
 * Represents a join. The data on Table object is the target table
 * 
 * @author Ivan Perales
 *
 */
public class JoinTable extends SelectTable{
	
	public static enum TYPE{
		INNER("inner join"),
		FULL("full join"),
		LEFT("left join"),
		RIGHT("right join");
		
		private String join;
		
		TYPE(String join){
			this.join = join;
		}
		
		public String getJoin(){
			return this.join;
		}
	}
	
	String targetColumnName;//this contain just the name ofthe property of the target table of the join
	String sourceFullProperty;//this contain the full property name of the source table (table alias and property name) 
	TYPE type;

	public JoinTable(Query query, String fullTargetTable, String targetColumnName, String sourceFullProperty, TYPE type){
		super(fullTargetTable);
		this.targetColumnName = Helper.getPropertyName(targetColumnName);//nos aseguramos de que la columna destino tenga solo el nombre de la propiedad
		this.sourceFullProperty = query.getFullProperty(sourceFullProperty);
		this.type = type;
	}
	
	public JoinTable(Query query, String targetTableName, String targetTableAlias, String targetColumnName, String sourceFullProperty, TYPE type){
		super(targetTableName, targetTableAlias);
		this.targetColumnName = Helper.getPropertyName(targetColumnName);//nos aseguramos de que la columna destino tenga solo el nombre de la propiedad
		this.sourceFullProperty = query.getFullProperty(sourceFullProperty);
		this.type = type;
	}
	
	@Override public void build(Builder builder){
		builder.append(type.getJoin());
		builder.append(" ");
		builder.append(getQuery().getSchema());
		builder.append(".");
		builder.append(getName());
		builder.append(" ");
		builder.append(getAlias());
		builder.append(" on ");
		builder.append(getAlias());
		builder.append(".");
		builder.append(targetColumnName);
		builder.append(" = ");
		builder.append(sourceFullProperty);
	}
}
