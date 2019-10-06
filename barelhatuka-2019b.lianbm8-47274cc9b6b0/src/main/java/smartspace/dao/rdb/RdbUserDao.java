package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.AdvancedUserDao;
import smartspace.dao.UserNotFoundException;
import smartspace.data.UserEntity;

@Repository
public class RdbUserDao implements AdvancedUserDao {
	private UserCrud userCrud;
	private String smartspace;
	
	
	public RdbUserDao() {

	}
	
	@Value("${smartspace.name:2019B.Lianbm8}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}
	
	@Override
	@Transactional
	public UserEntity create(UserEntity user) {
		if(user.getUserSmartspace() == null)
			user.setUserSmartspace(smartspace);

		user.setKey(user.getUserSmartspace() + "#" + user.getUserEmail());
		
		// SQL: INSERT
		if(!this.userCrud.existsById(user.getKey())) {
			UserEntity ue = this.userCrud.save(user);
			return ue;
		}
		else{
			throw new RuntimeException("user already exists: " + user.getKey());
		}
	}
	@Autowired
	public void setUserCrud(UserCrud userCrud) {
		this.userCrud = userCrud;
	}

	@Override
	@Transactional(readOnly=true)
	public Optional<UserEntity> readById(String key) {
		// SQL: SELECT
		return this.userCrud.findById(key);
	}

	@Override
	@Transactional(readOnly=true)
	public List<UserEntity> readAll() {
		List<UserEntity> list = new ArrayList<>();
		// SQL: SELECT
	    this.userCrud.findAll()
				.forEach(list::add);
	    return list;
	}

	@Override
	@Transactional
	public void update(UserEntity user) {
		// SQL: UPDATE
		
		UserEntity found = readById(user.getKey())
				.orElseThrow(() -> new UserNotFoundException("Invalid user key: " + user.getKey()));
		if(user.getUserSmartspace()!=null) {
			found.setUserSmartspace(user.getUserSmartspace());	
		}
		if(user.getUserEmail()!=null) {
			found.setUserEmail(user.getUserEmail());	
		}
		if(user.getUsername()!=null) {
			found.setUsername(user.getUsername());	

		}
		if(user.getAvatar()!=null) {
			found.setAvatar(user.getAvatar());	
		}
		if(user.getRole()!=null) {
			found.setRole(user.getRole());
		}
		this.userCrud.save(found);
	}
	
	@Override
	@Transactional
	public void updateUserPoints(UserEntity user) {
		this.userCrud.save(user);
	} 

	@Override
	public void deleteAll() {
		// SQL: DELETE
		this.userCrud.deleteAll();
	}

	@Override
	@Transactional(readOnly=true)
	public List<UserEntity> readAll(int size, int page) {
		return this.userCrud
				.findAll(PageRequest.of(page, size))
				.getContent();
	}

	@Override
	@Transactional(readOnly=true)
	public List<UserEntity> readAll(int size, int page, String sortAttr) {
		return this.userCrud
				.findAll(PageRequest.of(page, size, Direction.ASC, sortAttr))
				.getContent();
	}

	@Override
	@Transactional(readOnly=true)
	public Optional<UserEntity> findUserByKey(String key) {
		return this.userCrud.findById(key);
	}

	@Override
	@Transactional
	public UserEntity createFromImport(UserEntity currentUser) {
		currentUser.setKey(currentUser.getUserSmartspace() + "#" + currentUser.getUserEmail());
		
		// SQL: INSERT
		if(!this.userCrud.existsById(currentUser.getKey())) {
			return this.userCrud.save(currentUser);
		}
		else {
			throw new RuntimeException("user already exists");
		}
	}

}
