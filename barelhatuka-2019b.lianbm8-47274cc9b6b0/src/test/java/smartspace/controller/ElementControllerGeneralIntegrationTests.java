package smartspace.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;


import smartspace.dao.AdvancedElementDao;
import smartspace.dao.rdb.RdbElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.util.EntityFactory;
import smartspace.layout.Creator;
import smartspace.layout.ElementBoundary;
import smartspace.layout.Latlng;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties="spring.profiles.active=default")
public class ElementControllerGeneralIntegrationTests {

	private int port; 	
	private String baseUrl;
	private RestTemplate rest;
	private AdvancedElementDao elementDao;
	private RdbElementDao rdbdao;
	private EntityFactory factory;
	
	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}
	
	@Autowired
	public void setUsers(AdvancedElementDao element) {
		this.elementDao = element;
	}
	
	@Autowired
	public void setDao(RdbElementDao dao) {
		this.rdbdao = dao;
	}
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/elementsdemo";
		this.rest = new RestTemplate();
	}

	@After
	public void teardown() {
		this.elementDao
			.deleteAll();
	}
	
	@Test(expected=Exception.class)
	public void testPublishFromThisProject() throws Exception {
		//GIVEN the database is clean and user role is ADMIN barel
		
		//WHEN I post a new element with my smartspace
		String smartspace = "2019B.Lianbm8", email= "barel@gmail.com";
				
		Map < String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("genresList: " ,Arrays.asList("Fantasy"));
		moreAttributes.put("Authorþ: ", "þSuzanne Collins");
		moreAttributes.put("series: ", "yes");
				
	//	ElementEntity element1 = new ElementEntity("Game of Thrones", "book", new Location(0, 0), new Date(),
	//			"barel@gmail.com", smartspace, false, moreAttributes);
		ElementBoundary element1 = new ElementBoundary();
		element1.setName("The Hunger Games");
		element1.setLatlng(new Latlng(0,0));
		element1.setElementType("book");
		element1.setExpired(false);
		element1.setCreator(new Creator());
		element1.setElementProperties(moreAttributes);
		 
		  this.rest
			.postForObject(
					this.baseUrl + "/{smartspace}/{email}",
					element1,
					ElementBoundary.class,
					smartspace,
					email);
		// THEN i want exception: "can't post element from 2019B.Lianbm8"

	}
	
	@Test
	public void testPublishFromOtherProject() throws Exception {
		//GIVEN the database is clean 
		
		//WHEN I post a new element with diffrent smartspace and user role is ADMIN barel
		String smartspace = "2019B.Lianbm8", email= "barel@gmail.com";
				
		Map < String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("genresList: " ,Arrays.asList("Romace" ,"Fantasy"));
		moreAttributes.put("comment: ", "Best Book Ever!");
		moreAttributes.put("series: ", "yes");
		
		//public ElementEntity createNewElement(String name, String type, Location location, Date creationTimestamp
		//, String creatorEmail, String creatorSmartspace, boolean expired, Map <String, Object> moreAttributes);
		
		ElementEntity element1 = factory.createNewElement("Game of Thrones", "book", new Location(0, 0), new Date(),
				email, smartspace, false, moreAttributes); 
		
		//This artirbutes factory not initialize 
		element1.setElementSmartspace("2019B.DOREL");
		element1.setElementId("1");
		element1.setCreatorSmartspace(smartspace);
		ElementBoundary element2 = new ElementBoundary(element1);
		
		ElementBoundary[] arrayElement =  {element2};

		  this.rest
			.postForObject(
					this.baseUrl + "/{smartspace}/{email}",
					arrayElement,
					ElementBoundary[].class,
					smartspace,
					email);
		
		// THEN the database contains a single new element
		assertThat(this.elementDao.readAll())
			.hasSize(1);
	}
	// fdsfsdfsdfsf
	@Test(expected=Exception.class)
	public void tedstPublishFromOtherProjectWithUserNotInDB() throws Exception {
		//GIVEN the database is clean
		
		//WHEN I post a new element with diffrent smartspace and user role is not it the system
		String smartspace = "2019B.DOREL8", email= "dor@gmail.com";
				
		Map < String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("genresList: " ,Arrays.asList("Romace" ,"Fantasy"));
		moreAttributes.put("comment: ", "Best Book Ever!");
		moreAttributes.put("series: ", "yes");
		
		ElementEntity element1 = factory.createNewElement("Game of Thrones", "book", new Location(0, 0), new Date(),
				email, smartspace, false, moreAttributes);
		elementDao.create(element1);
		
		//This artirbutes factory not initialize 
		element1.setElementSmartspace("2019B.DOREL");
		element1.setElementId("1");
		element1.setCreatorSmartspace(smartspace);
		
		ElementBoundary element2 = new ElementBoundary(element1);	

		
		//THEN the database contains a single new element
		  this.rest
			.postForObject(
					this.baseUrl + "/{smartspace}/{email}",
					element2,
					ElementBoundary.class,
					smartspace,
					email);
		// THEN i want exception: "There is not such user"
	}
	
	@Test
	public void testGetElementsWithPagination() throws Exception {
		// GIVEN the database contains element 10
		String smartspace = "2019B.Lianbm8", email= "barel@gmail.com";
		
		int inputSize = 10;
		IntStream.range(1, inputSize + 1)
			.mapToObj(i->factory.createNewElement("Encylopedia volume:" +i, "book", new Location((double)i, (double)i), new Date(),
					email, "2019B.DOREL", false, null))
			.forEach(this.elementDao::create);
		
		// WHEN I get all elements using page size of 3 and page 1
		int page = 1;
		int size = 3;
		ElementBoundary[] actual = 
		  this.rest
			.getForObject(
					this.baseUrl + "/{smartspace}/{email}?page={page}&size={size}", 
					ElementBoundary[].class,
					smartspace, email,
					page, size);
		
		// THEN I get 3 actions
		int expectedSize = 3;
		assertThat(actual)
			.hasSize(expectedSize);	
		
	}
	
}
