package smartspace.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.AdvancedUserDao;
import smartspace.dao.UserNotFoundException;
import smartspace.dao.UserRoleExcpetion;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Service
public class UserSerivceImpl implements UserService {
	private AdvancedUserDao userDao;
	private String smartspace;
	public final static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	@Autowired
	public UserSerivceImpl(AdvancedUserDao users) {
		super();
		this.userDao = users;
	}
	
	@Value("${smartspace.name:2019B.Lianbm8}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}
	
	@Override
	@Transactional	
	public List<UserEntity> publishNewUsersByAdmin(List<UserEntity> entities, String smartspace, String email) {
		checkIfUserAdmin(smartspace, email);
		
		// if we got up to here then user is admin
		List<UserEntity> newUsers = new ArrayList<>();
				
		for (int i = 0 ; i < entities.size() ; i++) {
			UserEntity currentUser = entities.get(i);
			if (currentUser.getUserSmartspace().equals(this.smartspace))
				throw new RuntimeException("Can't post action from 2019B.Lianbm8 project");
			else if (!validate(currentUser)) 
				throw new RuntimeException("invalid user");
			else 
				newUsers.add(this.userDao.createFromImport(currentUser));
		}

		return newUsers;		
	}
	
	@Override
	@Transactional	
	public UserEntity publishNewUser(UserEntity userEntity) {
		userEntity.setPoints(0);	//new user will always get 0 points for start
		userEntity.setUserSmartspace(smartspace);	// new user form will get the project name as its smartspace
		if (!validate(userEntity)) {
			throw new RuntimeException("invalid user");
		} else {
			return this.userDao.create(userEntity);	
		}		
	}
	
	private boolean validate(UserEntity userEntity) {
		return userEntity != null
				&& userEntity.getUserSmartspace()!= null && !userEntity.getUserSmartspace().trim().isEmpty()
				&& userEntity.getUserEmail() != null && !userEntity.getUserEmail().trim().isEmpty()
				&& validateEmail(userEntity.getUserEmail())
				&& userEntity.getUsername() != null && !userEntity.getUsername().trim().isEmpty()
				&& userEntity.getAvatar() != null && !userEntity.getAvatar().trim().isEmpty()
				&& userEntity.getRole() != null;
	}
	
	private boolean validateEmail(String userEmail) {
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(userEmail);
		
		if (!matcher.matches() ) {
			throw new RuntimeException("Email is not valid!");
		}
		return true;
	}

	@Override
	public List<UserEntity> getUsers(int size, int page, String smartspace, String email) {
		checkIfUserAdmin(smartspace, email);
		return this.userDao.readAll(size, page, "userEmail");
	}

	@Override
	@Transactional
	public void updateUser(String smartspace, String email, UserEntity user) throws UserNotFoundException {
		Optional <UserEntity> userFromDb = this.userDao.readById(smartspace + "#" + email);
		if(userFromDb.isPresent()) {
			UserEntity tempUser = userFromDb.get();
			if (tempUser.getPoints() != user.getPoints()) {
				throw new RuntimeException("Changing user points is forbidden");
			}
			else if (!tempUser.getUserEmail().equals(user.getUserEmail()) 
					|| !tempUser.getUserSmartspace().equals(user.getUserSmartspace())) {
				throw new RuntimeException("Changing user key is forbidden");
			}
		}
		else {
			throw new UserNotFoundException("User not found with key: " + smartspace + "#" + email);
		}
		user.setKey(smartspace + "#" + email);
		this.userDao.update(user);
	}
	
	@Override
	public void checkIfUserAdmin(String smartspace, String email) {
		String key = smartspace + "#" + email;
		Optional<UserEntity> user = this.userDao.findUserByKey(key);
		if (!user.isPresent()) throw new UserNotFoundException("Admin Doesn't Exist");
		if(user.get().getRole() !=  UserRole.ADMIN)
			throw new UserRoleExcpetion("User must be Admin!");
	}

	@Override
	public UserEntity getSpecificUser(String userSmartspace, String userEmail) {
		Optional<UserEntity> user = this.userDao.readById(userSmartspace + "#" + userEmail);
		if(user.isPresent())
			return user.get();
		throw new UserNotFoundException("User doesn't exist");
	}

}
