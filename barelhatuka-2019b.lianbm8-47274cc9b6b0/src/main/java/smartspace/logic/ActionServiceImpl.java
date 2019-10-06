package smartspace.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartspace.aop.MangerOrPlayerValidation;
import smartspace.dao.AdvancedActionDao;
import smartspace.dao.AdvancedElementDao;
import smartspace.dao.AdvancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.plugins.Plugin;

@Service
public class ActionServiceImpl implements ActionService {
	private AdvancedActionDao actions;
	private AdvancedUserDao userDao;
	private AdvancedElementDao elementDao;
	private String smartspcae;
	private ConfigurableApplicationContext ctx;

	@Autowired
	public ActionServiceImpl(AdvancedActionDao actions, AdvancedUserDao userDao, AdvancedElementDao elementDao,
			ConfigurableApplicationContext ctx) {
		this.actions = actions;
		this.userDao = userDao;
		this.elementDao = elementDao;
		this.ctx = ctx;
	}

	@Value("${smartspace.name:2019B.Lianbm8}")
	public void setSmartspace(String smartspace) {
		this.smartspcae = smartspace;
	}

	@Override
	@Transactional
	public List<ActionEntity> publishActionsByAdmin(List<ActionEntity> entities, String smartspace, String email) {
		checkIfUserAdmin(smartspace, email);

		// if we got up to here then user is admin
		List<ActionEntity> newActions = new ArrayList<>();

		for (int i = 0 ; i < entities.size() ; i++) {
			ActionEntity current = entities.get(i);
			if (current.getActionSmartspace().equals(this.smartspcae))
				throw new RuntimeException("Can't post action from 2019B.Lianbm8 project");
			else if (checkKey(current.getActionSmartspace(), current.getActionId()))
				throw new RuntimeException("Illegal action key input");
			else if (isValid(current)) {
				someSettings(current);
				newActions.add(this.actions.createFromImport(current));
			}
			else {
				throw new RuntimeException("publishNewAction error");				
			}
		}
		return newActions;
	}

	@Override
	@Transactional
	public boolean publishNewActionByPlayer(ActionEntity actionEntity) {
		if (checkIfUserIsPlayer(actionEntity)) {
			if (isValid(actionEntity)) {
				if (!isElementExpired(actionEntity.getElementSmartspace(), actionEntity.getElementId())) {
					//someSettings(actionEntity);
					return true;
				}
				else {
					throw new RuntimeException("Sorry, this item is no longer available!");
				}
			}
			else {
				throw new RuntimeException("Element is not valid!");
			}
		}
		else {
			throw new RuntimeException("User must be Player!");			
		}
	}

	private boolean checkIfUserIsPlayer(ActionEntity actionEntity) {
		Optional<UserEntity> user = this.userDao.readById(actionEntity.getPlayerSmartspace() + "#" + actionEntity.getPlayerEmail());
		if (user.isPresent() && user.get().getRole() == UserRole.PLAYER) {
			return true;
		}
		throw new RuntimeException("User wasn't found. User must be PLAYER!");
	}

	@Override
	public List<ActionEntity> getActions(int size, int page, String smartspace, String email) {
		checkIfUserAdmin(smartspace, email);
		return this.actions.readAll(size, page);
	}

	private void someSettings(ActionEntity actionEntity) {
		//		if (isElementExpired(actionEntity.getElementSmartspace(), actionEntity.getElementId()))
		//			throw new ElementNotFoundException(
		//					"Couldn't find element with id " + actionEntity.getElementId());
		actionEntity.setCreationTimestamp(new Date());
		if (actionEntity.getMoreAttributes().isEmpty()) {
			actionEntity.getMoreAttributes().put("valid", true);
		}
	}

	private boolean validateAction(ActionEntity actionEntity) {
		return actionEntity != null && actionEntity.getActionType() != null
				&& !actionEntity.getActionType().trim().isEmpty() && actionEntity.getPlayerEmail() != null
				&& !actionEntity.getPlayerEmail().trim().isEmpty() && actionEntity.getPlayerEmail().contains("@")
				&& actionEntity.getPlayerSmartspace() != null && !actionEntity.getPlayerSmartspace().trim().isEmpty();
	}

	public boolean checkKey(String smartspace, String id) {
		return smartspace == null || smartspace.trim().isEmpty() || id == null || id.trim().isEmpty();
	}

	private boolean isElementCreatedBeforeAction(ActionEntity actionEntity) {
		return this.elementDao.readById(actionEntity.getElementSmartspace() + "#" + actionEntity.getElementId())
				.isPresent();
	}

	private boolean isElementExpired(String smartspace, String id) {
		return this.elementDao.readById(smartspace + "#" + id).get().isExpired();
	}

	//	private boolean checkIfActionIsEcho(ActionEntity actionEntity) {
	//		return actionEntity.getActionType().equals("echo");
	//	}

	private boolean isValid(ActionEntity actionEntity) {
		if (!isElementCreatedBeforeAction(actionEntity)) {
			throw new RuntimeException("Action created BEFORE element! : " + actionEntity.getElementId() + " "
					+ actionEntity.getElementSmartspace());
		} else if (!validateAction(actionEntity)) {
			throw new RuntimeException("Illegal action input");
		}
		return true;
	}

	@Override
	public void checkIfUserAdmin(String smartspace, String email) {
		String key = smartspace + "#" + email;
		Optional<UserEntity> user = this.userDao.findUserByKey(key);
		if (!user.isPresent())
			throw new RuntimeException("Admin Doesn't Exist");
		if (user.get().getRole() != UserRole.ADMIN)
			throw new RuntimeException("User must be Admin!");
	}

	@Override
	@MangerOrPlayerValidation
	public String checkUserRole(String smartspace, String email, String userFlag) {
		System.err.println("USER FLAG FROM ACTION SERVICE: " + userFlag);
		return userFlag;
	}

	@Override
	@Transactional
	public Object performAction(ActionEntity entity) {
		Object response = null;
		try {
			String operation = "";
			operation = entity.getActionType();

			// echo | read book summary | check in | check out | purchase
			// smartspace.plugins.EhcoPlugin |

			if (publishNewActionByPlayer(entity)) {
				if (!operation.trim().isEmpty()) {
					operation = operation.toLowerCase().replaceAll(" ", ""); // in case the input includes other capital letters
					
					//System.err.println("opearion: " + operation);
					
					String pluginClassName = "smartspace.plugins." + operation.toUpperCase().charAt(0)
							+ operation.substring(1, operation.length()) + "Plugin";
					
					//System.err.println("Plugin class name is: " + pluginClassName);
					
					Class<?> pluginClass = Class.forName(pluginClassName);
					
					Plugin actionPlugin = (Plugin) ctx.getBean(pluginClass);
					response = actionPlugin.action(entity);
					entity.getMoreAttributes().put("response", response);
				}
				someSettings(entity);
				this.actions.create(entity);
				return response;
			}
			else {
				throw new RuntimeException("publish failed");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
