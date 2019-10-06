package smartspace.layout;

import org.springframework.beans.factory.annotation.Value;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;
public class UserForm {
	private String	email;
	private String  username;
	private String  avatar;
	private String  role;
	private String smartspcae;
	
	public UserForm() {
	}
	
	public UserForm(UserEntity user) {
		email = user.getUserEmail();
		username = user.getUsername();
		avatar = user.getAvatar();
		role = user.getRole().name();
	}
	
	@Value("${smartspace.name:2019B.Lianbm8}")
	public void setSmartspace(String smartspace) {
		this.smartspcae = smartspace;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getAvatar() {
		return avatar;
	}
	
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	
	public UserEntity toEntity() {
		UserEntity entity = new UserEntity();
		// NOTE: in this demo application, entity default key is null
		String email = this.getEmail();
		entity.setKey(smartspcae + "#" + email);
		entity.setAvatar(this.avatar);
		entity.setPoints(0);
		entity.setRole(UserRole.valueOf(this.role));
		entity.setUsername(this.username);
		return entity;
	}

	@Override
	public String toString() {
		return "UserForm [email=" + email + ", username=" + username + ", avatar=" + avatar + ", role=" + role
				+ ", smartspcae=" + smartspcae + "]";
	}

	
}
