package gd.driver.base;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;

public class DebugCommand {
	
	private Method m;
	private ArrayList<String> params = new ArrayList<String>();
	
	public DebugCommand(Method m){
		this.m = m;
		
		this.params = getParameterNames(m);		
	}	
	
	public Method getMethod() {
		return m;
	}

	public void setMethod(Method m) {
		this.m = m;
	}

	public String getMethodName() {
		return m.getName();
	}

	public ArrayList<String> getParams() {
		return params;
	}


	private ArrayList<String> getParameterNames(Method method) {
        Parameter[] parameters = method.getParameters();
        ArrayList<String> parameterNames = new ArrayList<>();
        for (Parameter parameter : parameters) {
            if(!parameter.isNamePresent()) {
                throw new IllegalArgumentException("Parameter names are not present!");
            }

            String parameterName = parameter.getName();
            parameterNames.add(parameterName);
        }

        return parameterNames;
    }
	
	public Class<?> getParameterType(String param){
		int index = this.params.indexOf(param);		
		return m.getParameterTypes()[index];
	}
	
	public String run(Object ...args){
		Object obj = null;		
		try {			
			obj = m.invoke(null, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.err.println(e.getMessage());
		}		
		return (String)obj;
	}
	
	public static HashMap<String, DebugCommand> getDebugCommands(){
		Method[] methods = DebugCommandSet.class.getDeclaredMethods();
		HashMap<String, DebugCommand> commands = new HashMap<String, DebugCommand>();
		for(Method m : methods){
			commands.put(m.getName() ,new DebugCommand(m));
		}
		
		return commands;		
	}
}
