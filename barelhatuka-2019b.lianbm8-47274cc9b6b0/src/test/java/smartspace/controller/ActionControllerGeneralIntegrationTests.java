package smartspace.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import smartspace.dao.AdvancedActionDao;
import smartspace.dao.rdb.RdbElementDao;
import smartspace.dao.rdb.RdbUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;
import smartspace.layout.ActionBoundary;
import smartspace.layout.ElementAndActionKey;
import smartspace.layout.UserKey;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties="spring.profiles.active=default")
public class ActionControllerGeneralIntegrationTests {
	private int port;
	private String baseUrl;
	private RestTemplate rest;
	private RdbUserDao rdbUserDao;
	private AdvancedActionDao actionsDao;
	private RdbElementDao elementDao;
	private EntityFactory factory;
	
	@Autowired
	public void setRdbdao(RdbUserDao rdbdao) {
		this.rdbUserDao = rdbdao;
	}
	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}
	
	@Autowired
	public void setActionsDao(AdvancedActionDao actions) {
		this.actionsDao = actions;
	}
	
	@Autowired
	public void setElementDao(RdbElementDao dao) {
		this.elementDao = dao;
	}
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/actions";
		this.rest = new RestTemplate();
	}

	@After
	public void teardown() {
		this.actionsDao.deleteAll();
		this.elementDao.deleteAll();
	}
	@Before
	public void before() {
		rdbUserDao.deleteAll();
		String smartspace = "2019B.Lianbm8", email= "barel@gmail.com";
		UserEntity user = new UserEntity(email, "barel3220", "BH", UserRole.ADMIN, 34023);
		user.setUserSmartspace(smartspace);
	    rdbUserDao.create(user);
	}
//	@Test
//	public void testGet() throws Exception {
//		// GIVEN the database caontains 1 action and its element
//		
//		String smartspace = "2019B.Lianbm8", email= "barel@gmail.com";
//		
//		Map < String, Object> moreAttributes = new HashMap<>();
//		moreAttributes.put("genresList: " ,Arrays.asList("Romace" ,"Fantasy"));
//		moreAttributes.put("comment: ", "Best Book Ever!");
//		moreAttributes.put("series: ", "yes");
//		
//		ElementEntity element = factory.createNewElement("Game of Thrones", "book", new Location(0, 0), new Date(),
//				email, smartspace, false, moreAttributes);
//		elementDao.create(element);
//		
//		ActionEntity ac = factory.createNewAction(element.getElementId(), element.getElementSmartspace(),
//				"bookmark", new Date(), email, smartspace, moreAttributes);
//		actions.create(ac);
//		
//		// WHEN I get all actions
//		this.rest
//		.getForObject(this.baseUrl + "/{smartspace}/{email}",
//				ActionBoundary[].class,
//				smartspace, email);
//		
//		// THEN the database contains a single new action
//		assertThat(this.actions
//			.readAll())
//			.hasSize(1);
//	}
	
	@Test
	public void testGetActionsWithPagination() throws Exception {
		// GIVEN the database contains element and 10 actions
		
		String smartspace = "2019B.Lianbm8", email= "barel@gmail.com";
		
		Map < String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("genresList: " ,Arrays.asList("Romace" ,"Fantasy"));
		moreAttributes.put("comment: ", "Best Book Ever!");
		moreAttributes.put("series: ", "yes");
		
		ElementEntity element = factory.createNewElement("Game of Thrones", "book", new Location(0, 0), new Date(),
				"dani@gmail.com", "2019.Dani", false, moreAttributes);
		elementDao.create(element);
		
		int inputSize = 10;
		IntStream.range(1, inputSize + 1)
			.mapToObj(i->factory.createNewAction(element.getElementId()+i, element.getElementSmartspace(),
					String.valueOf(i), new Date(), "dani@gmail.com", "2019.Dani", moreAttributes))
			.forEach(this.actionsDao::create);
		
		// WHEN I get all actions using page size of 3 and page 1
		int page = 1;
		int size = 3;
		ActionBoundary[] actual = 
		  this.rest
			.getForObject(
					this.baseUrl + "/{smartspace}/{email}?page={page}&size={size}", 
					ActionBoundary[].class,
					smartspace, email,
					page, size);
		
		// THEN I get 3 actions
		int expectedSize = 3;
		assertThat(actual)
			.hasSize(expectedSize);	
		
	}
	
	@Test(expected=Exception.class)
	public void testPublishFromThisProject() throws Exception {
		// GIVEN the database is clean
		
		// WHEN I post a new action as admin from 2019B.Lianbm8 project
		String smartspace = "2019B.Lianbm8", email= "barel@gmail.com";
		
		Map < String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("genresList: " ,Arrays.asList("Romace" ,"Fantasy"));
		moreAttributes.put("comment: ", "Best Book Ever!");
		moreAttributes.put("series: ", "yes");
		
		ElementEntity element = factory.createNewElement("Game of Thrones", "book", new Location(0, 0), new Date(),
				email, smartspace, false, moreAttributes);
		elementDao.create(element);
		
		ActionBoundary newAction = new ActionBoundary();
		newAction.setActionKey(new ElementAndActionKey(smartspace, "1"));
		newAction.setType("markbook");
		newAction.setCreated(new Date());
		newAction.setElement(new ElementAndActionKey(element.getElementSmartspace(), element.getElementId()));
		newAction.setProperties(new HashMap<>());
		newAction.setPlayer(new UserKey(smartspace, email));
		
		  this.rest
			.postForObject(
					this.baseUrl + "/{smartspace}/{email}",
					newAction,
					ActionBoundary.class,
					smartspace,
					email);
		
		// THEN exception is thrown
	}
	
	/*@Test
	public void testPublishFromOtherProject() throws Exception {
		// GIVEN the database is clean
		// WHEN I post a new action as admin from other project
		String smartspace = "2019B.Lianbm8", email= "barel@gmail.com";
		
		Map <String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("genresList: " ,Arrays.asList("Romace" ,"Fantasy"));
		moreAttributes.put("comment: ", "Best Book Ever!");
		moreAttributes.put("series: ", "yes");
		
		ElementEntity element = factory.createNewElement("Game of Thrones", "book", new Location(0, 0), new Date(),
				"dani@gmail.com", "2019B.DanielleGiladi", false, moreAttributes);
		elementDao.create(element);
		
		ActionBoundary[] newAction = new ActionBoundary[1];
		newAction[0] = new ActionBoundary();
		newAction[0].setActionKey(new ElementAndActionKey(element.getCreatorSmartspace(), element.getElementId()));
		newAction[0].setType("markbook");
		newAction[0].setCreated(new Date());
		newAction[0].setElement(new ElementAndActionKey(element.getCreatorSmartspace(), element.getElementId()));
		newAction[0].setPlayer(new UserKey(element.getCreatorEmail(), element.getCreatorSmartspace()));
		
		Map <String, Object> map = new HashMap<>();
		map.put("page", 91);
		newAction[0].setProperties(map);
		
		  this.rest
			.postForObject(
					this.baseUrl + "/{smartspace}/{email}",
					newAction,
					ActionBoundary[].class,
					smartspace,
					email);
		
		// THEN the database contains a single new action
		assertThat(this.actionsDao.readAll())
			.hasSize(1);
	}*/
	
	@Test(expected=Exception.class)
	public void testNoAdmin() {
		// GIVEN the database is clean
		
		// WHEN I post a new action NOT as admin
		String smartspace = "2019B.Lianbm8", email= "lian@gmail.com";
		
		Map < String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("genresList: " ,Arrays.asList("Romace" ,"Fantasy"));
		moreAttributes.put("comment: ", "Best Book Ever!");
		moreAttributes.put("series: ", "yes");
		
		ElementEntity element = factory.createNewElement("Game of Thrones", "book", new Location(0, 0), new Date(),
				email, smartspace, false, moreAttributes);
		elementDao.create(element);
		
		ActionBoundary newAction = new ActionBoundary();
		newAction.setActionKey(new ElementAndActionKey(smartspace, "1"));
		newAction.setType("markbook");
		newAction.setCreated(new Date());
		newAction.setElement(new ElementAndActionKey(element.getElementSmartspace(), element.getElementId()));
		newAction.setProperties(new HashMap<>());
		newAction.setPlayer(new UserKey(smartspace, email));
		
		  this.rest
			.postForObject(
					this.baseUrl + "/{smartspace}/{email}",
					newAction,
					ActionBoundary.class,
					smartspace,
					email);
		
		// THEN an exception is thrown
	}
	
	@Test(expected=Exception.class)
	public void testActionBeforeElement() {
		// GIVEN the database is clean
		
		// WHEN I post a new action with no element
		String smartspace = "2019B.Lianbm8", email= "lian@gmail.com";
		
		Map < String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("genresList: " ,Arrays.asList("Romace" ,"Fantasy"));
		moreAttributes.put("comment: ", "Best Book Ever!");
		moreAttributes.put("series: ", "yes");
		
		ElementEntity element = factory.createNewElement("Game of Thrones", "book", new Location(0, 0), new Date(),
				email, smartspace, false, moreAttributes);
		elementDao.create(element);
		
		ActionBoundary newAction = new ActionBoundary();
		newAction.setActionKey(new ElementAndActionKey(smartspace, "1"));
		newAction.setType("markbook");
		newAction.setCreated(new Date());
		newAction.setElement(new ElementAndActionKey(element.getElementSmartspace(), element.getElementId()));
		newAction.setProperties(new HashMap<>());
		newAction.setPlayer(new UserKey(smartspace, email));
		
		  this.rest
			.postForObject(
					this.baseUrl + "/{smartspace}/{email}",
					newAction,
					ActionBoundary.class,
					smartspace,
					email);
		  
		// THEN an exception is thrown 
	}

}
