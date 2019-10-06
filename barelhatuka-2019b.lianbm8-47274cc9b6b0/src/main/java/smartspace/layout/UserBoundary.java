package smartspace.layout;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;
public class UserBoundary {
	private UserKey	key;
	private String  username;
	private String  avatar;
	private String  role;
	private long    points;
	
	public UserBoundary() {
	}
	
	public UserBoundary(UserEntity user) {
		key = new UserKey(user.getUserSmartspace(), user.getUserEmail());
		username = user.getUsername();
		avatar = user.getAvatar();
		role = user.getRole().name();
		points = user.getPoints();
	}
	
	public UserKey getKey() {
		return key;
	}

	public void setKey(UserKey userKey) {
		this.key = userKey;
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
	
	public long getPoints() {
		return points;
	}
	
	public void setPoints(long points) {
		this.points = points;
	}
	
	public UserEntity toEntity() {
		UserEntity entity = new UserEntity();
		// NOTE: in this demo application, entity default key is null
		if(this.key.getSmartspace() == null || this.getKey().getEmail() == null)
			throw new RuntimeException("Must Enter smartspace and unique key");
		String smartspace = this.key.getSmartspace();
		String email = this.getKey().getEmail();
		
		if(smartspace != null && email != null) {
			entity.setKey(smartspace + "#" + email);
		} else {
			entity.setKey(null);
		}		
		entity.setAvatar(this.avatar);
		//entity.setPoints(0);
		entity.setPoints(this.points);
		entity.setRole(UserRole.valueOf(this.role));
		entity.setUsername(this.username);
		
		return entity;
	}

	@Override
	public String toString() {
		return "UserBoundary [key=" + key + ", username=" + username + ", avatar=" + avatar + ", role=" + role
				+ ", points=" + points + "]";
	}
}
