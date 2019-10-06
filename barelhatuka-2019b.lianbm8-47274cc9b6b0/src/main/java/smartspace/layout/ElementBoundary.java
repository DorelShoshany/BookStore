package smartspace.layout;

import java.util.Date;
import java.util.Map;

import smartspace.data.ElementEntity;
import smartspace.data.Location;

public class ElementBoundary {

	private ElementAndActionKey key;
	private Creator creator;
	private Latlng latlng;
	private String elementType;
	private String name;

	// the spring can handle with:
	private boolean expired;
	private Map<String, Object> elementProperties;
	private Date created;

	public ElementBoundary() {

	}

	// marshal:
	public ElementBoundary(ElementEntity entity) {
		this.key = new ElementAndActionKey(entity.getElementSmartspace(), entity.getElementId());
		this.creator = new Creator( entity.getCreatorSmartspace(), entity.getCreatorEmail());
		this.latlng = new Latlng(entity.getLocation().getX(), entity.getLocation().getY());
		this.elementType = entity.getType();
		this.name = entity.getName();
		this.expired = entity.isExpired();
		this.elementProperties = entity.getMoreAttributes();
		this.created = entity.getCreationTimestamp();
	}

	public ElementAndActionKey getKey() {
		return key;
	}

	public void setKey(ElementAndActionKey elementKey) {
		this.key = elementKey;
	}

	public Creator getCreator() {
		return creator;
	}

	public void setCreator(Creator creator) {
		if (this.creator == null)
			this.creator = new Creator();
		this.creator = creator;
	}

	public Latlng getLatlng() {
		return latlng;
	}

	public void setLatlng(Latlng latLng) {
		this.latlng = latLng;
	}

	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public Map<String, Object> getElementProperties() {
		return elementProperties;
	}

	public void setElementProperties(Map<String, Object> elementProperties) {
		this.elementProperties = elementProperties;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	// Unmarshal:
	public ElementEntity toEntity() {
		ElementEntity entity = new ElementEntity();

		String smartspace = this.key.getSmartspace();
		String id = this.key.getId();

		if (smartspace != null && id != null) 
			entity.setKey(smartspace + "#" + id);

		entity.setName(this.name);
		entity.setType(this.elementType);
		entity.setExpired(this.expired);
		
		if (latlng == null) {
			this.latlng = new Latlng(0.0, 0.0);
		}
		entity.setLocation(new Location(latlng.getLat(), latlng.getLng()));
		entity.setCreationTimestamp(this.created);
		entity.setMoreAttributes(this.elementProperties);
		if (this.creator == null) {
			this.creator = new Creator("John Doe", "John@gmail.com");
		}
		entity.setCreatorEmail(this.creator.getEmail());
		entity.setCreatorSmartspace(this.creator.getSmartspace());
		
		return entity;
	}
	
	public ElementEntity toEntityWithoutKey() {
		ElementEntity entity = new ElementEntity();

		entity.setName(this.name);
		entity.setType(this.elementType);
		entity.setExpired(this.expired);
		
		if (latlng == null) {
			this.latlng = new Latlng(0.0, 0.0);
		}
		entity.setLocation(new Location(latlng.getLat(), latlng.getLng()));
		entity.setCreationTimestamp(this.created);
		entity.setMoreAttributes(this.elementProperties);
		if (this.creator == null) {
			this.creator = new Creator("John Doe", "John@gmail.com");
		}
		entity.setCreatorEmail(this.creator.getEmail());
		entity.setCreatorSmartspace(this.creator.getSmartspace());
		
		return entity;
	}

	@Override
	public String toString() {
		return "ElementBoundary [elementSmartspace=" + this.key.getSmartspace() + ", elementId=" + this.key.getId()
				+ ", location=x" + this.latlng.getLat() + " y:" + this.latlng.getLng() + "name=" + this.name + ", type="
				+ this.elementType + ", " + "creationTimestamp=" + this.created + ", creatorSmartspace="
				+ this.creator.getSmartspace() + ", creatorEmail=" + this.creator.getEmail() + ", expired="
				+ this.expired + ", moreAttributes=" + this.elementProperties + "]";
	}

}
