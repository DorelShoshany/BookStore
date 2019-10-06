package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.dao.rdb.RdbUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserDaoIntegrationTest {
	private RdbUserDao rdbUserDao; 
	private EntityFactory factory;
	
	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}
	
	@Autowired
	public void SetUserDao(RdbUserDao userDao) {
		this.rdbUserDao = userDao;
	}
	
	@Before
	public void setup() {
		this.rdbUserDao.deleteAll();
	}
	
	@After
	public void tearDown() {
		this.rdbUserDao.deleteAll();
	}
	
	@Test
	public void testCreateUserWithAttributesOfVariousTypes() throws Exception{
		//GIVEN the database is empty
		
		//WHEN I create a user with various types
		UserEntity user = factory.createNewUser("dorebert@gmail.com", "", "theking1", "Dor", UserRole.MANAGER, 5);
		
		String rv = this.rdbUserDao.create(user).getKey();
		
		//THEN the database contains the inserted value
		assertThat(this.rdbUserDao.readById(rv))
		.isPresent()
		.get()
		.isEqualToIgnoringGivenFields(user, "key");
	}
	
	@Test(expected=Exception.class)
	public void createMultipleUsers() {
		// GIVEN nothing
		// WHEN i create 4 users but 2 is the same
		UserEntity user1 = factory.createNewUser("dorebert@gmail.com", "", "theking1", "Dor", UserRole.MANAGER, 5);
		UserEntity user2 = factory.createNewUser("barel@gmail.com", "", "barel", "B", UserRole.MANAGER, 362);
		UserEntity user3 = factory.createNewUser("dorebert@gmail.com", "", "theking1", "Dor", UserRole.MANAGER, 5);
		UserEntity user4 = factory.createNewUser("DorelSho20@gmail.com", "", "Dorel", "DS", UserRole.MANAGER, 35627);
		user1 = rdbUserDao.create(user1);
		user2 = rdbUserDao.create(user2);
		user3 = rdbUserDao.create(user3);
		user4 = rdbUserDao.create(user4);
		// THEN i want exception
	}

}
