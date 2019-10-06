package smartspace.controller;

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
public class UserControllerGeneralPostIntegrationTests {
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
	public void testPublishFromThisProject() throws Exception {
		String smartspace ="2019B.Lianbm8" ,// "2019B.DanielleGiladi", 
				   email= "baressl@gmail.com";
			// GIVEN the database contain 1 user as Admin
		   UserEntity user = new UserEntity(email, "barel3220", "BH", UserRole.ADMIN, 34023);
		   user.setUserSmartspace(smartspace);
	       rdbdao.create(user);
			// WHEN I post a new user as admin from other project
			
		
			UserEntity userEntity = factory.createNewUser("ttttttt@gmail.com", smartspace, "test", "TS", UserRole.PLAYER, 12855);		
			//dao.create(userEntity);
			
			UserBoundary[] newUser = new UserBoundary[1];
			newUser[0] = new UserBoundary();
			newUser[0].setAvatar(userEntity.getAvatar());
			newUser[0].setUsername(userEntity.getUsername());
			newUser[0].setPoints(userEntity.getPoints());
			newUser[0].setKey(new UserKey(userEntity.getUserSmartspace(),userEntity.getUserEmail()));
			newUser[0].setRole(userEntity.getRole().toString());
			
			UserBoundary userBoundary = this.rest
				.postForObject(
						this.baseUrl + "/{smartspace}/{email}",
						newUser,
						UserBoundary.class,
						smartspace,
						email);
		
		// THEN exception is thrown
	}
}
