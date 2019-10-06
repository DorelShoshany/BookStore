package smartspace.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartspace.dao.UserRoleExcpetion;
import smartspace.dao.UserNotFoundException;
import smartspace.data.UserEntity;
import smartspace.logic.UserService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
@RestController
public class UserController {
	private UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		super();
		this.userService = userService;	
	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/smartspace/admin/users/{adminSmartspace}/{adminEmail}",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] publish (
			@PathVariable("adminSmartspace") String smartspace, 
			@PathVariable("adminEmail") String email,
			@RequestBody UserBoundary[] users) {
		
		List<UserEntity> entities = new ArrayList<>();
		for (int i=0 ; i<users.length ; i++) {
			entities.add(users[i].toEntity());
		}
		
		entities = this.userService.publishNewUsersByAdmin(entities, smartspace, email);
		
		return entities.stream() // UserEntity Stream
				.map(UserBoundary::new) // UserBoundary Stream
				.collect(Collectors.toList()) // UserBoundary List
				.toArray(new UserBoundary[0]); // UserBoundary[]
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path= "/smartspace/admin/users/{adminSmartspace}/{adminEmail}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] getUsers (
			@PathVariable("adminSmartspace") String smartspace, 
			@PathVariable("adminEmail") String email,
			@RequestParam(name="size", required=false, defaultValue="10") int size, 
			@RequestParam(name="page", required=false, defaultValue="0") int page) {
		
		return 
			this.userService
				.getUsers(size, page, smartspace, email) // UserEntity List	
			.stream() // UserEntity Stream
			.map(UserBoundary::new) // UserBoundary Stream
			.collect(Collectors.toList()) // UserBoundary List
			.toArray(new UserBoundary[0]); // UserBoundary[]

	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/smartspace/users",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary publish (
			@RequestBody UserForm user) {
		return new UserBoundary(
				this.userService
					.publishNewUser(
							user.toEntity()));
	}
	
	@RequestMapping(
			method=RequestMethod.GET,
			path= "/smartspace/users/login/{userSmartspace}/{userEmail}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary login (
			@PathVariable("userSmartspace") String userSmartspace, 
			@PathVariable("userEmail") String userEmail) {
		
		return new UserBoundary(this.userService.getSpecificUser(userSmartspace, userEmail));
	}
	
	@RequestMapping(
			method=RequestMethod.PUT,
			path= "/smartspace/users/login/{userSmartspace}/{userEmail}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public void updateUser(
			@PathVariable("userSmartspace") String userSmartspace, 
			@PathVariable("userEmail") String userEmail,
			@RequestBody UserBoundary update) {
		
		UserEntity user = update.toEntity();
		this.userService.updateUser(userSmartspace, userEmail, user);
	}
	

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorMessage handleException(UserNotFoundException e){
		String message = e.getMessage();
		if (message == null || message.trim().isEmpty()) {
			message = "could not find user";
		}
		return new ErrorMessage(message);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorMessage handleException(UserRoleExcpetion e){
		String message = e.getMessage();
		if (message == null || message.trim().isEmpty()) {
			message = "Admin does not have authority";
		}
		return new ErrorMessage(message);
	}
}
