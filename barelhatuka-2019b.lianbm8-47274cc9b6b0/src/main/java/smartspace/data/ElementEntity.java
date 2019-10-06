package smartspace.data;

import java.util.Date;
import java.util.Map;

//import javax.persistence.Column;
//import javax.persistence.Convert;
//import javax.persistence.Embedded;
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
//@Table(name="ELEMENTS")
@Document(collection="ELEMENTS")
public class ElementEntity implements SmartspaceEntity<String> {
	private String key;
	private String elementSmartspace;
	private String elementId;
	private Location location;
	private String name;
	private String type;
	private Date creationTimestamp;	
	private boolean expired;
	private Map <String, Object> moreAttributes;
	private String creatorSmartspace;
	private String creatorEmail;
	
	
	public ElementEntity() {
	}

	public ElementEntity(String name, String type, Location location, Date creationDate, String creatorEmail, boolean expired, Map <String, Object> moreAttributes) {
		super();
		this.name = name;
		this.type = type;
		this.creationTimestamp = creationDate;
		this.creatorEmail = creatorEmail;
		this.expired = expired;
		this.location = location;
		this.moreAttributes = moreAttributes;
	}
	
	public ElementEntity(String name, String type, Location location, Date creationDate, String creatorEmail, 
			String creatorSmartspace, boolean expired, Map <String, Object> moreAttributes) {
		super();
		this.name = name;
		this.type = type;
		this.creationTimestamp = creationDate;
		this.creatorEmail = creatorEmail;
		this.creatorSmartspace=creatorSmartspace;
		this.expired = expired;
		this.location = location;
		this.moreAttributes = moreAttributes;
		
	}

//	@Transient
	public String getElementSmartspace() {
		return elementSmartspace;
	}

	public void setElementSmartspace(String elementSmartspace) {
		this.elementSmartspace = elementSmartspace;
	}

//	@Transient
	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

//	@Embedded
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

//	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public String getCreatorSmartspace() {
		return creatorSmartspace;
	}

	public void setCreatorSmartspace(String creatorSmartspace) {
		this.creatorSmartspace = creatorSmartspace;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
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
//	@Column(name="Element_KEY")
	@Id
	public String getKey() {
		this.key = this.elementSmartspace + "#" + this.elementId;
		return this.key;
	}

	@Override
	public void setKey(String key) {
		this.key = key;
		String[] temporary = this.key.split("#");
		this.elementSmartspace = temporary[0];
		this.elementId = temporary[1];		
	}

	@Override
	public String toString() {
		return "ElementEntity [elementSmartspace=" + elementSmartspace + ", elementId=" + elementId + ", location="
				+ location + ", name=" + name + ", type=" + type + ", creationTimestamp=" + creationTimestamp
				+ ", creatorSmartspace=" + creatorSmartspace + ", creatorEmail=" + creatorEmail + ", expired=" + expired
				+ ", moreAttributes=" + moreAttributes + "]";
	}
}
