package smartspace.plugins;

import smartspace.data.ActionEntity;

public interface Plugin {
	
	public Object action(ActionEntity action) throws Exception;

}
