package smartspace.layout;

public class Creator {
	private String creatorSmartspace;
	private String creatorEmail;
	
	public Creator(String creatorSmartspace, String creatorEmail) {
		this.creatorSmartspace=creatorSmartspace;
		this.creatorEmail=creatorEmail;
	}
	public Creator() {

	}
	
	public String getEmail() {
		return creatorEmail;
	}
	public void setEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}
	public String getSmartspace() {
		return creatorSmartspace;
	}
	public void setSmartspace(String creatorSmartspace) {
		this.creatorSmartspace = creatorSmartspace;
	}
}
