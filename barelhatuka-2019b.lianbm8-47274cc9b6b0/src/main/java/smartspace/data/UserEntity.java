package smartspace.data;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import javax.persistence.Transient;

import org.springframework.data.annotation.Id;
//import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

//@Entity  
//@Table(name="USERS")
@Document(collection="USERS")
public class UserEntity implements SmartspaceEntity<String> {
	private String key;
	private String userSmartspace;
	private String userEmail;
	private String username;
	private String avatar;
	private UserRole role;
	private long points;
	
	public UserEntity() {	
	}
	
	public UserEntity(String userEmail, String username, String avatar, UserRole role, long points) {
		this.userEmail = userEmail;
		this.username = username;
		this.avatar = avatar;
		this.role = role;
		this.points = points;
	}
	
//	@Transient
	public String getUserSmartspace() {
		return userSmartspace;
	}
	
	public void setUserSmartspace(String userSmartspace) {
		this.userSmartspace = userSmartspace;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	public String getUsername() {
		return username;
	}
	
//	@Transient
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getAvatar() {
		return avatar;
	}
	
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
//	@Enumerated(EnumType.STRING)
	public UserRole getRole() {
		return role;
	}
	
	public void setRole(UserRole role) {
		this.role = role;
	}
	
	public long getPoints() {
		return points;
	}
	
	public void setPoints(long points) {
		this.points = points;
	}
	
	@Override
//	@Column(name="USER_KEY")
	@Id
	public String getKey() {
		key = this.userSmartspace + "#" + this.userEmail;
		return key;
	}
	
	@Override
	public void setKey(String key) {
		this.key = key;
		String[] temporary = this.key.split("#");
		this.userSmartspace = temporary[0];
		this.userEmail = temporary[1];
	}
}
