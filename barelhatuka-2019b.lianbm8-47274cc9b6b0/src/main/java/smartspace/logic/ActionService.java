package smartspace.logic;

import java.util.List;

import smartspace.data.ActionEntity;

public interface ActionService {
	public boolean publishNewActionByPlayer (ActionEntity actionEntity);
	public List<ActionEntity> getActions (int size, int page, String smartspace, String email);
	public void checkIfUserAdmin(String smartspace, String email);
	public String checkUserRole(String smartspace, String email, String userFlag);
	//public ActionEntity publishNewActionWithGeneratedKeyByPlayer(ActionEntity actionEntity);
	public boolean checkKey(String smartspace, String id);
	public List<ActionEntity> publishActionsByAdmin(List<ActionEntity> entities, String smartspace, String email);
	public Object performAction(ActionEntity entity);
}
