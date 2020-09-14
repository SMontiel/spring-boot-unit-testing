package com.aipisoft.querier;

/**
 * Accion que indica lo que hara el query: select, update, insert, delete.
 * 
 * @author Ivan Perales
 *
 */
public class Action {

	public static enum TYPE{
		
		SELECT("select"),
		INSERT("insert"),
		UPDATE("update"),
		DELETE("delete");
		
		private String name;
		
		TYPE(String name){
			this.name = name;
		}
		
		public String getName(){
			return name;
		}
	}
	
	private TYPE type;

	private Action(TYPE type){
		this.type = type;
	}
	
	public TYPE getType(){
		return type;
	}
	
	public static Action select(){
		return new Action(TYPE.SELECT);
	}
	
	public static Action insert(){
		return new Action(TYPE.INSERT);
	}
	
	public static Action update(){
		return new Action(TYPE.UPDATE);
	}
	
	public static Action delete(){
		return new Action(TYPE.DELETE);
	}
}
