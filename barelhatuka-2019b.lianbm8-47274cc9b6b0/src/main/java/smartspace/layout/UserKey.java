package smartspace.layout;

public class UserKey {
	private String smartspace;
	private String email;
	
	public UserKey(String userSmartspace, String email) {
		this.setSmartspace(userSmartspace);
		this.setEmail(email);
	}
	public UserKey() {
	}
	public String getSmartspace() {
		return smartspace;
	}

	public void setSmartspace(String userSmartspace) {
		this.smartspace = userSmartspace;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}	
