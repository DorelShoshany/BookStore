package smartspace.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import smartspace.dao.ElementDao;
import smartspace.dao.UserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Component
public class InitDatabase implements CommandLineRunner {
	private UserDao<String> userDao;
	
	@Autowired
	public InitDatabase(UserDao<String> userDao, ElementDao<String> elementDao) {
		this.userDao = userDao;
	}
	
	@Override
	public void run(String... args) throws Exception {
		UserEntity user = new UserEntity("barel@gmail.com", "barel3220", "BH", UserRole.ADMIN, 34023);
		userDao.create(user);
	}
}
