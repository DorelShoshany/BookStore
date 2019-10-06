package smartspace.data.util;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Component
public class EntityFactoryImplementation implements EntityFactory {

	@Override
	public UserEntity createNewUser(String userEmail, String userSmartspace, String username, String avatar,
			UserRole role, long points) {
		UserEntity user = new UserEntity(userEmail, username, avatar, role, points);
		user.setUserSmartspace(userSmartspace);
		return user;
	}

	@Override
	public ElementEntity createNewElement(String name, String type, Location location, Date creationTimestamp,
			String creatorEmail, String creatorSmartspace, boolean expired, Map<String, Object> moreAttributes) {
		ElementEntity element = new ElementEntity(name, type, location, creationTimestamp, creatorEmail, expired, moreAttributes);
		return element;
	}

	@Override
	public ActionEntity createNewAction(String elementId, String elementSmartspace, String actionType,
			Date creationTimestamp, String playerEmail, String playerSmartspace, Map<String, Object> moreAttributes) {
		ActionEntity action = new ActionEntity(elementId, playerEmail, actionType, creationTimestamp, moreAttributes);
		return action;
	}
	
}
