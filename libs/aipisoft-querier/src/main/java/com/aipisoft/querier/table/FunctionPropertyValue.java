package com.aipisoft.querier.table;

import java.util.List;

import com.aipisoft.querier.Helper;
import com.aipisoft.querier.Query;
import com.aipisoft.querier.builder.Builder;

/**
 * Objeto to set a value to a property with a little difference. The value is going to be retrieved from a function.
 * 
 * @author Ivan Perales
 *
 */
public class FunctionPropertyValue extends PropertyValue {
	private String function;
	
	public FunctionPropertyValue(Query query, String fullProperty, String function, List<FunctionPropertyValue.PV> params){
		this(query, Helper.getTableAlias(fullProperty), Helper.getPropertyName(fullProperty), function, params);
	}
	
	public FunctionPropertyValue(Query query, String tableAlias, String property, String function, List<FunctionPropertyValue.PV> params){
		super(query, tableAlias, property, params);
		
		this.function = function;
	}
	
	@Override public void build(Builder builder){
		builder.append(function);
		builder.append("(");
		
		List<FunctionPropertyValue.PV> params = (List)getValue();
		if(params != null && params.size() > 0){
			for(int i=0; i<params.size(); i++){
				FunctionPropertyValue.PV pv = params.get(i);
				if(i > 0){
					builder.append(", ");
				}
				
				if(pv.plain == true){
					builder.append(pv.parameter.toString());
				} else{
					builder.append("?");
					builder.addArg(pv.parameter);
				}
			}
		}
		
		builder.append(")");
	}
	
	/** Clase privada para indicar el tipo de argumentos de la funcion, el cual solo puede ser uno.
	 * Si se especifica plain, pondra tal cual, si se especifica parameter, será un valor proporcionado al query */
	public final static class PV{
		boolean plain;
		Object parameter;
		
		private PV(boolean plain, Object parameter){
			this.plain = plain;
			this.parameter = parameter;
		}
		
		public final static PV plain(Object plain){
			return new PV(true, plain);
		}
		
		public final static PV param(Object param){
			return new PV(false, param);
		}
	}
}