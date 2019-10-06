package smartspace.layout;

import java.util.Date;
import java.util.Map;

import smartspace.data.ActionEntity;

public class ActionBoundary {
	private ElementAndActionKey actionKey;
	private ElementAndActionKey element;
	private UserKey player;
	private String type;
	private Date created;
	private Map<String, Object> properties;
	
	public ActionBoundary() {
	}
	
	public ActionBoundary(ActionEntity entity) {
		this.actionKey = new ElementAndActionKey(entity.getActionSmartspace(), entity.getActionId());
		this.element = new ElementAndActionKey(entity.getElementSmartspace(), entity.getElementId());
		this.player = new UserKey(entity.getPlayerSmartspace(), entity.getPlayerEmail());
		this.type = entity.getActionType();
		this.created = entity.getCreationTimestamp();
		this.properties = entity.getMoreAttributes();
	}
	
	public ElementAndActionKey getActionKey() {
		return actionKey;
	}

	public void setActionKey(ElementAndActionKey key) {
		this.actionKey = key;
	}
	
	public ElementAndActionKey getElement() {
		return element;
	}

	public void setElement(ElementAndActionKey element) {
		this.element = element;
	}

	public UserKey getPlayer() {
		return player;
	}

	public void setPlayer(UserKey player) {
		this.player = player;
	}

	public String getType() {
		return type;
	}

	public void setType(String actionType) {
		this.type = actionType;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date creationTimestamp) {
		this.created = creationTimestamp;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> moreAttributes) {
		this.properties = moreAttributes;
	}
	
	public ActionEntity toEntity() {
		ActionEntity entity = new ActionEntity();
		// NOTE: in this demo application, entity default key is null
		
		String smartspace = this.actionKey.getSmartspace();
		String id = this.actionKey.getId();
		
		if (smartspace != null && id != null) 
			entity.setKey(smartspace + "#" + id);
		
		entity.setElementSmartspace(this.element.getSmartspace());
		entity.setElementId(this.element.getId());
		entity.setPlayerSmartspace(this.player.getSmartspace());
		entity.setPlayerEmail(this.player.getEmail());
		entity.setActionType(this.type.replaceAll(" ", ""));
		entity.setCreationTimestamp(created);
		entity.setMoreAttributes(this.properties);
		
		return entity;
	}
	
	@Override
	public String toString() {
		return "ActionBoundary [actionKey=" + actionKey + ", element=" + element + ", player=" + player + ", type="
				+ type + ", created=" + created + ", properties=" + properties + "]";
	}

	public ActionEntity toEntityWithoutKey() {
		ActionEntity entity = new ActionEntity();
		
		entity.setElementSmartspace(this.element.getSmartspace());
		entity.setElementId(this.element.getId());
		entity.setPlayerSmartspace(this.player.getSmartspace());
		entity.setPlayerEmail(this.player.getEmail());
		entity.setActionType(this.type.replaceAll(" ", ""));
		entity.setCreationTimestamp(created);
		entity.setMoreAttributes(this.properties);
		
		return entity;
	}
}
