package smartspace.dao;

import java.util.List;
import java.util.Optional;

import smartspace.data.UserEntity;

public interface AdvancedUserDao extends UserDao<String> {
	public List<UserEntity> readAll(int size, int page);
	public List<UserEntity> readAll(int size, int page, String sortAttr);
	public Optional<UserEntity> findUserByKey(String key);
	public UserEntity createFromImport(UserEntity currentUser);
	public void updateUserPoints(UserEntity currentUser);
}
