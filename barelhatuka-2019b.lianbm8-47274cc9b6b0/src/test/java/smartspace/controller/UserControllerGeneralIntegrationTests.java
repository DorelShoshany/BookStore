package smartspace.controller;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.stream.IntStream;

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

import smartspace.dao.AdvancedUserDao;
import smartspace.dao.rdb.RdbUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;
import smartspace.layout.UserBoundary;
import smartspace.layout.UserKey;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties="spring.profiles.active=default")
public class UserControllerGeneralIntegrationTests {
	private int port;
	private String baseUrl;
	private RestTemplate rest;
	private AdvancedUserDao userdao;
	private RdbUserDao rdbdao;
	private EntityFactory factory;
	
	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}
	
	@Autowired
	public void setUsers(AdvancedUserDao user) {
		this.userdao = user;
	}
	
	@Autowired
	public void setDao(RdbUserDao dao) {
		this.rdbdao = dao;
	}
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/users";
		this.rest = new RestTemplate();
	}

	@After
	public void teardown() {
		this.userdao
			.deleteAll();
	}
	
	@Test(expected=Exception.class)
	public void testGet() throws Exception {
		// GIVEN the database is clean
		
		// WHEN I post a new USER as admin
		String smartspace = "2019B.Lianbm8", email= "barel@gmail.com";
			
		UserEntity userEntity = factory.createNewUser("test@gmail.com","201s9B.Lianbm8", "baasels3220", "BaasH", UserRole.PLAYER, 1285);
		rdbdao.create(userEntity);
		
		UserEntity ac = factory.createNewUser(userEntity.getUserEmail(), userEntity.getUserSmartspace(),
				"TEST2", "TS2",UserRole.MANAGER, 14564);
		userdao.create(ac);
		
		this.rest
		.getForObject(this.baseUrl + "/{smartspace}/{email}",
				UserBoundary[].class,
				smartspace, email);
		
		// THEN the database contains a single new user
		assertThat(this.userdao
			.readAll())
			.hasSize(1);
	}
	
	@Test
	public void testGetUsersWithPagination() throws Exception {
		// GIVEN the database contains element and 10 users
		String smartspace = "2019B.Lianbm8", email= "barel@gmail.com";
		
		
		UserEntity userEntity = factory.createNewUser("test@gmail.com", "2019B.Lianbm8", "test", "TS", UserRole.PLAYER, 1285);
		rdbdao.create(userEntity);
		
		int inputSize = 10;
		IntStream.range(1, inputSize + 1)
			.mapToObj(i->factory.createNewUser(userEntity.getUserEmail() + i, userEntity.getUserSmartspace(),
					"TEST2", "TS2",UserRole.MANAGER, 14564))
			.forEach(this.userdao::create);
		
		// WHEN I get all users using pagination: size of 3 and page 1
		int page = 1;
		int size = 3;
		UserBoundary[] actual = 
		  this.rest
			.getForObject(
					this.baseUrl + "/{smartspace}/{email}?page={page}&size={size}", 
					UserBoundary[].class,
					smartspace, email,
					page, size);
		
		// THEN I get 3 userss
		int expectedSize = 3;
		assertThat(actual)
			.hasSize(expectedSize);	
		
	}
	
	
	
   @Test
	public void testPublishFromOtherProject() throws Exception {
	   this.rdbdao.deleteAll();
	   String smartspace =  "2019B.DanielleGiladi"; 
	   String email= "barel@gmail.com";
		// GIVEN the database contain 1 user as Admin
	   UserEntity user = new UserEntity(email, "barel3220", "BH", UserRole.ADMIN, 34023);
	   user.setUserSmartspace(smartspace);
       rdbdao.create(user);
		// WHEN I post a new user as admin from other project
	
		UserEntity userEntity2 = factory.createNewUser("DOR@gmail.com", "DorE", "tesst", "TaS", UserRole.MANAGER, 12855);		
		
		UserBoundary[] newUser2 = new UserBoundary[1];
		newUser2[0] = new UserBoundary();
		newUser2[0].setAvatar(userEntity2.getAvatar());
		newUser2[0].setUsername(userEntity2.getUsername());
		newUser2[0].setPoints(userEntity2.getPoints());
		newUser2[0].setKey(new UserKey(userEntity2.getUserSmartspace(),userEntity2.getUserEmail()));
		newUser2[0].setRole(userEntity2.getRole().toString());
		
		this.rest
			.postForObject(
					this.baseUrl + "/{smartspace}/{email}",
					newUser2,
					UserBoundary[].class,
					smartspace,
					email);
		
		// THEN the database contains a 2 news user
		assertThat(this.userdao.readAll())
			.hasSize(2);
		
		
	}
	
	@Test(expected=Exception.class)
	public void testNoAdmin() {
		  String smartspace ="2019B.Lianbm" ,// "2019B.DanielleGiladi", 
				   email= "barAel@gmail.com";
			// GIVEN the database contain 1 user as NOT Admin
		   UserEntity user = new UserEntity(email, "barel3220", "BH", UserRole.PLAYER, 34023);
		   user.setUserSmartspace(smartspace);
	       rdbdao.create(user);
			// WHEN I post a new user as admin from other project
			
		
			UserEntity userEntity = factory.createNewUser("sdq2sdasx@gmail.com", "2019B.DanielleGiladi", "test", "TS", UserRole.PLAYER, 12855);		
			//dao.create(userEntity);
			
			UserBoundary newUser = new UserBoundary();
			newUser.setAvatar(userEntity.getAvatar());
			newUser.setUsername(userEntity.getUsername());
			newUser.setPoints(userEntity.getPoints());
			newUser.setKey(new UserKey(userEntity.getUserSmartspace(),userEntity.getUserEmail()));
			newUser.setRole(userEntity.getRole().toString());
			
			this.rest
				.postForObject(
						this.baseUrl + "/{smartspace}/{email}",
						newUser,
						UserBoundary.class,
						smartspace,
						email);
			
		
		// THEN an exception is thrown
	}
}
