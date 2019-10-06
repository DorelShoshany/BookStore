package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import smartspace.dao.rdb.RdbElementDao;
import smartspace.dao.rdb.RdbUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;

@SpringBootTest
@RunWith(SpringRunner.class)
//@TestPropertySource(properties= {"spring.profiles.active"})
public class ElementDaoIntegrationTest {
	private RdbElementDao rdbElementDao;
	private RdbUserDao rdbUserDao;
	private EntityFactory factory;

	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}

	@Autowired
	public void setRdbElementDao(RdbElementDao rdbElementDao) {
		this.rdbElementDao = rdbElementDao;
	} 
	
	@Autowired
	public void setRdbUserDao(RdbUserDao rdbUserDao) {
		this.rdbUserDao = rdbUserDao;
	}
	
	
	@Before
	public void Setup() {
		this.rdbElementDao.deleteAll();
		this.rdbUserDao.deleteAll();
	}
	
	@After
	public void tearDown() {
		this.rdbElementDao.deleteAll();
		this.rdbUserDao.deleteAll();
	}
	
	@Test
	public void testCreateElementWithAttributesOfVariousTypes() throws Exception{
		//GIVEN user is created
		UserEntity user = factory.createNewUser("Dorelsho20@gmail.com", "", "DorelSho", "DS", UserRole.MANAGER, 3402);
		rdbUserDao.create(user);
		
		//WHEN I create a element with various types
		Map < String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("genresList: " ,Arrays.asList("Romace" ,"Fantasy"));
		moreAttributes.put("comment: ", "Best Book Ever!");
		moreAttributes.put("series: ", "yes");
		
		
		
		
		ElementEntity element = factory.createNewElement("Game of Thrones", "book", new Location(0, 0), new Date(),
				user.getUserEmail(), user.getUserSmartspace(), false, moreAttributes);
		
		String pk = this.rdbElementDao.create(element).getKey();
		
		//THEN the database contains the inserted value
		ObjectMapper jackson = new ObjectMapper();
		Map <String , Object> expectedMoreAttributes =
				jackson.readValue(
						jackson.writeValueAsString(moreAttributes),
						Map.class);
		
		assertThat(this.rdbElementDao.readById(pk))
		.isPresent()
		.get()
		.extracting("moreAttributes")
		.usingRecursiveFieldByFieldElementComparator()
		.containsExactly(expectedMoreAttributes);
	}
	
	@Test
	public void createMultipleElements() throws Exception {
		// GIVEN nothing
		Map < String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("genresList" ,Arrays.asList("Romance" ,"Fantasy", "Horror", "Adventure"));
		moreAttributes.put("summery", "Best Book Ever!");
		moreAttributes.put("series", "yes");
		
		UserEntity user = factory.createNewUser("barel@gmail.com", "", "Barel3220", "BH", UserRole.MANAGER, 2531);
		rdbUserDao.create(user);
		
		// WHEN i create 3 elements but 2 is the same
		ElementEntity element = factory.createNewElement("The Passage", "book", new Location(0, 9), new Date(), 
				user.getUserEmail(), user.getUserSmartspace(), false, moreAttributes);
		rdbElementDao.create(element);

		// THEN i want exception
		
		ObjectMapper jackson = new ObjectMapper();
		Map <String , Object> expectedMoreAttributes =
				jackson.readValue(
						jackson.writeValueAsString(moreAttributes),
						Map.class);
		
		assertThat(this.rdbElementDao.readById(element.getKey()))
		.isPresent()
		.get()
		.extracting("moreAttributes")
		.usingRecursiveFieldByFieldElementComparator()
		.containsExactly(expectedMoreAttributes);
	}
	
	@Test
	public void createAndUpdateAnElement() {
		//GIVEN creation of a book
		//AND user
		UserEntity user = factory.createNewUser("barel@gmail.com", "", "BAREL", "BS", UserRole.MANAGER, 3220);
		rdbUserDao.create(user);
		
		Map < String, Object> elementMoreAttributes = new HashMap<>();
		elementMoreAttributes.put("genresList" ,Arrays.asList("Romance" ,"Fantasy", "Horror", "Adventure"));
		elementMoreAttributes.put("summery", "Best Book Ever!");
		elementMoreAttributes.put("series", "yes");
		
		ElementEntity element = factory.createNewElement("The Passage", "book", new Location(0, 9), new Date(), 
				user.getUserEmail(), user.getUserSmartspace(), false, elementMoreAttributes);
		element = rdbElementDao.create(element);
		
		//WHEN i update the element
		element.getLocation().setX(0);
		element.getLocation().setY(0);
		element.setExpired(true);
		rdbElementDao.update(element);
		
		//THEN i want to check the changes
		assertThat(this.rdbElementDao.readById(element.getKey()))
		.isPresent()
		.get()
		.extracting("expired", "location.x", "location.y")
		.containsExactly(true, 0.0, 0.0);	
	}
	
	
	

}
