package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.dao.rdb.RdbActionDao;
import smartspace.dao.rdb.RdbElementDao;
import smartspace.dao.rdb.RdbUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ActionDaoIntegrationTest {
	private RdbActionDao rdbActionDao;
	private RdbElementDao rdbElementDao;
	private RdbUserDao rdbUserDao;
	private EntityFactory factory;
	
	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}
	
	@Autowired
	public void setRdbUserDao(RdbUserDao rdbUserDao) {
		this.rdbUserDao = rdbUserDao;
	}
	
	@Autowired
	public void setRdbActionDao(RdbActionDao rdbActionDao) {
		this.rdbActionDao = rdbActionDao;
	}
	
	@Autowired
	public void setRdbElementDao(RdbElementDao rdbElementDao) {
		this.rdbElementDao = rdbElementDao;
	}
	
	// cleanup action dao before each test
	@Before
	public void setup() {
		this.rdbActionDao.deleteAll();
		this.rdbElementDao.deleteAll();
		this.rdbUserDao.deleteAll();
	}
	
	// cleanup action dao after each test
	@After
	public void teardown () {
		this.rdbActionDao.deleteAll();
		this.rdbElementDao.deleteAll();
		this.rdbUserDao.deleteAll();
	}
	
	@Test
	public void testCreateAndDeleteAll() {
		//GIVEN user is created
		//AND a map
		//AND an element
		UserEntity user = factory.createNewUser("barel@gmail.com", "", "BAREL", "BS", UserRole.MANAGER, 3220);
		rdbUserDao.create(user);
		
		Map < String, Object> elementMoreAttributes = new HashMap<>();
		elementMoreAttributes.put("genresList" ,Arrays.asList("Romance" ,"Fantasy", "Horror", "Adventure"));
		elementMoreAttributes.put("summery", "Best Book Ever!");
		elementMoreAttributes.put("series", "yes");
		
		ElementEntity element = factory.createNewElement("The Passage", "book", new Location(0, 9), new Date(), 
				user.getUserEmail(), user.getUserSmartspace(), false, elementMoreAttributes);
		rdbElementDao.create(element);
		
		Map < String, Object> actionMoreAttributes = new HashMap<>();
		
		// WHEN I create 3 actions
		// AND I delete all actions
		ActionEntity turnPageAction = factory.createNewAction(element.getElementId(), element.getElementSmartspace(), 
				element.getType(), new Date(), user.getUserEmail(), user.getUserSmartspace(), actionMoreAttributes);
		turnPageAction.getMoreAttributes().put("page_turn", 100);
		rdbActionDao.create(turnPageAction);
		
		ActionEntity bookmarkAction = factory.createNewAction(element.getElementId(), element.getElementSmartspace(), 
				element.getType(), new Date(), user.getUserEmail(), user.getUserSmartspace(), actionMoreAttributes);
		bookmarkAction.getMoreAttributes().put("bookmark_page", 200);
		rdbActionDao.create(bookmarkAction);
		
		ActionEntity readAction = factory.createNewAction(element.getElementId(), element.getElementSmartspace(), 
				element.getType(), new Date(), user.getUserEmail(), user.getUserSmartspace(), actionMoreAttributes);
		readAction.getMoreAttributes().put("page_start_reading", 99);
		readAction.getMoreAttributes().put("page_end_reading", 200);
		
		// delete
		this.rdbActionDao.deleteAll();
		
		// THEN the dao contains no actions
		assertThat(this.rdbActionDao.readAll()).isEmpty();
	}
	
	@Test
	public void createUserAndBookAndDoActions() {
		//GIVEN user is created
		//AND a map
		//AND an element
		UserEntity user = factory.createNewUser("barel@gmail.com", "", "BAREL", "BS", UserRole.MANAGER, 3220);
		rdbUserDao.create(user);
		
		Map < String, Object> elementMoreAttributes = new HashMap<>();
		elementMoreAttributes.put("genresList" ,Arrays.asList("Romance" ,"Fantasy", "Horror", "Adventure"));
		elementMoreAttributes.put("summery", "Best Book Ever!");
		elementMoreAttributes.put("series", "yes");
		
		ElementEntity element = factory.createNewElement("The Passage", "book", new Location(0, 9), new Date(), 
				user.getUserEmail(), user.getUserSmartspace(), false, elementMoreAttributes);
		rdbElementDao.create(element);
		
		Map < String, Object> actionMoreAttributes = new HashMap<>();
		
		// WHEN I create 3 actions
		// AND I delete all actions
		ActionEntity turnPageAction = factory.createNewAction(element.getElementId(), element.getElementSmartspace(), 
				"turn page", new Date(), user.getUserEmail(), user.getUserSmartspace(), actionMoreAttributes);
		turnPageAction.getMoreAttributes().put("page_turn", 100);
		rdbActionDao.create(turnPageAction);
		
		ActionEntity bookmarkAction = factory.createNewAction(element.getElementId(), element.getElementSmartspace(), 
				"bookmark page", new Date(), user.getUserEmail(), user.getUserSmartspace(), actionMoreAttributes);
		bookmarkAction.getMoreAttributes().put("bookmark_page", 200);
		rdbActionDao.create(bookmarkAction);
		
		ActionEntity readAction = factory.createNewAction(element.getElementId(), element.getElementSmartspace(), 
				"reading", new Date(), user.getUserEmail(), user.getUserSmartspace(), actionMoreAttributes);
		readAction.getMoreAttributes().put("page_start_reading", 99);
		readAction.getMoreAttributes().put("page_end_reading", 200);
		rdbActionDao.create(readAction);
		
		// THEN i want to see them
		assertThat(rdbActionDao.readAll()).hasSize(3);
	}

}
