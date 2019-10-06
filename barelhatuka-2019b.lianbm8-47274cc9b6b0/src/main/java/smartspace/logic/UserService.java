package smartspace.logic;

import java.util.List;

import smartspace.data.UserEntity;

public interface UserService {
	public List<UserEntity> publishNewUsersByAdmin (List<UserEntity> userEntity, String smartspace, String email);
	public UserEntity publishNewUser(UserEntity userEntity);
	public List<UserEntity> getUsers (int size, int page, String smartspace, String email);
	public void checkIfUserAdmin(String smartspace, String email);
	public void updateUser(String smartspace, String email,UserEntity user)throws RuntimeException;
	public UserEntity getSpecificUser(String userSmartspace, String userEmail);
}
