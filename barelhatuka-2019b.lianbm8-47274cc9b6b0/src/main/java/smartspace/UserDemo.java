package smartspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

//import org.springframework.stereotype.Repository;

import smartspace.dao.UserDao;
import smartspace.data.UserEntity;

//@Repository
public class UserDemo implements UserDao<String> { // finish

	private List<UserEntity> users;
	
	public UserDemo() {
		this.users = Collections.synchronizedList(new ArrayList<>());
	}
	
	@Override
	public UserEntity create(UserEntity user) {
		user.setKey(user.getUserSmartspace() + "#" + user.getUsername());
		this.users.add(user);
		return user;
	}

	@Override
	public Optional<UserEntity> readById(String key) {
		return this.users.stream().filter(book->book.getKey().equals(key)).findFirst();
	}

	@Override
	public List<UserEntity> readAll() {
		return users;
	}

	@Override
	public void update(UserEntity user) {
		UserEntity found = 
				readById(user.getKey())
					.orElseThrow(()->new RuntimeException("Invalid book key: " + user.getKey()));
		found.setAvatar(user.getAvatar());
		found.setKey(user.getKey());
		found.setPoints(user.getPoints());
		found.setRole(user.getRole());
		found.setUserSmartspace(user.getUserSmartspace());
		if (user.getUserEmail() != null)
			found.setUserEmail(user.getUserEmail());
		if (user.getUsername() != null)
			found.setUsername(user.getUsername());
	}

	@Override
	public void deleteAll() {
		this.users.clear();
	}

}
