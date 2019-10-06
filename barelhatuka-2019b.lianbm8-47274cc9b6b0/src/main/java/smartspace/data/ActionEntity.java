package smartspace.data;

import java.util.Date;
import java.util.Map;

//import javax.persistence.Column;
//import javax.persistence.Convert;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Lob;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import javax.persistence.Transient;

import org.springframework.data.annotation.Id;
//import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

//import smartspace.dao.rdb.MapAttributesConverter;

//@Entity  
//@Table(name="ACTIONS")
@Document(collection="ACTIONS")
public class ActionEntity implements SmartspaceEntity<String> {
	private String key;
	private String actionSmartspace;
	private String actionId;
	private String elementSmartspace;
	private String elementId;
	private String playerSmartspace;
	private String playerEmail;
	private String actionType;
	private Date creationTimestamp;
	private Map<String, Object> moreAttributes;

	public ActionEntity() {

	}

	public ActionEntity(String elementId, String playerEmail, String actionType, Date creationDate, Map<String, Object> moreAttributes) {
		this.elementId = elementId;
		this.playerEmail = playerEmail;
		this.actionType = actionType;
		this.creationTimestamp = creationDate;
		this.moreAttributes = moreAttributes;
	}
	
//	@Transient
	public String getActionSmartspace() {
		return actionSmartspace;
	}

	public void setActionSmartspace(String actionSmartspace) {
		this.actionSmartspace = actionSmartspace;
	}
	
//	@Transient
	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getElementSmartspace() {
		return elementSmartspace;
	}

	public void setElementSmartspace(String elementSmartspace) {
		this.elementSmartspace = elementSmartspace;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getPlayerSmartspace() {
		return playerSmartspace;
	}

	public void setPlayerSmartspace(String playerSmartspace) {
		this.playerSmartspace = playerSmartspace;
	}

	public String getPlayerEmail() {
		return playerEmail;
	}

	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

//	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

//	@Lob
//	@Convert(converter=MapAttributesConverter.class)
	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}

	@Override
//	@Column(name="ACTION_KEY")
	@Id
	public String getKey() {
		this.key = this.actionSmartspace + "#" + this.actionId; 
		return key;
	}

	@Override
	public void setKey(String key) {
		this.key = key;
		String[] temporary = this.key.split("#");
		this.actionSmartspace = temporary[0];
		this.actionId = temporary[1];
	}
	
	@Override
	public String toString() {
		return "ActionEntity [actionSmartspace=" + actionSmartspace + ", actionId=" + actionId + ", elementSmartspace="
				+ elementSmartspace + ", elementId=" + elementId + ", playerSmartspace=" + playerSmartspace
				+ ", playerEmail=" + playerEmail + ", actionType=" + actionType + ", creationTimestamp="
				+ creationTimestamp + ", moreAttributes=" + moreAttributes + "]";
	}
}
