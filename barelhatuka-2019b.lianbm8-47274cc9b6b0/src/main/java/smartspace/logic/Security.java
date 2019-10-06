package smartspace.logic;

import smartspace.dao.rdb.UserCrud;
import smartspace.data.UserEntity;

public class Security {
	private UserCrud userCrud;
	
	public Security(UserCrud userCrud) {
		this.userCrud = userCrud;
	}
	
	public UserEntity findUserByKey(String key) {
		return this.userCrud.findById(key).get();
	}

}
