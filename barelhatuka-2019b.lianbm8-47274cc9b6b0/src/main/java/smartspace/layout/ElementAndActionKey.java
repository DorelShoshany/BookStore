package smartspace.layout;

public class ElementAndActionKey {
	private String smartspace;
	private String id;

	public ElementAndActionKey() {
		
	}
	
	public ElementAndActionKey(String smartspace, String id) {
		this.setId(id);
		this.setSmartspace(smartspace);
	}

	public String getSmartspace() {
		return smartspace;
	}

	public void setSmartspace(String smatspace) {
		this.smartspace = smatspace;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
