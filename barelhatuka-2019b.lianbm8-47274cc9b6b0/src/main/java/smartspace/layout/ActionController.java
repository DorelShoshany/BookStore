package smartspace.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import smartspace.dao.ElementNotFoundException;
import smartspace.data.ActionEntity;
import smartspace.logic.ActionService;

@RestController
public class ActionController {
	private ActionService actionService;

	@Autowired
	public ActionController(ActionService actionService) {
		super();
		this.actionService = actionService;
	}

	@RequestMapping(method = RequestMethod.POST,
			path = "/smartspace/admin/actions/{adminSmartspace}/{adminEmail}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] publish(@PathVariable("adminSmartspace") String smartspace,
			@PathVariable("adminEmail") String email, @RequestBody ActionBoundary[] actions) {

		List<ActionEntity> entities = new ArrayList<>();
		for (int i=0 ; i<actions.length ; i++) {
			entities.add(actions[i].toEntity());
		}
		
		entities = this.actionService.publishActionsByAdmin(entities, smartspace, email);
		
		return entities.stream() // ActionEntity Stream
				.map(ActionBoundary::new) // ActionBoundary Stream
				.collect(Collectors.toList()) // ActionBoundary List
				.toArray(new ActionBoundary[0]); // ActionBoundary[]
	}

	@RequestMapping(method = RequestMethod.GET,
			path = "/smartspace/admin/actions/{adminSmartspace}/{adminEmail}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] getActions(
			@PathVariable("adminSmartspace") String smartspace,
			@PathVariable("adminEmail") String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		return this.actionService.getActions(size, page, smartspace, email) // ActionEntity List
				.stream() // ActionEntity Stream
				.map(ActionBoundary::new) // ActionBoundary Stream
				.collect(Collectors.toList()) // ActionBoundary List
				.toArray(new ActionBoundary[0]); // ActionBoundary[]
	}
	
//	@RequestMapping(method = RequestMethod.POST,
//			path = "/smartspace/actions",
//			consumes = MediaType.APPLICATION_JSON_VALUE,
//			produces = MediaType.APPLICATION_JSON_VALUE)
//	public ActionBoundary publish(@RequestBody ActionBoundary action) {
//		
//		ActionEntity actionEntity;
//		
//		if (action.getActionKey() == null) {
//			actionEntity = action.toEntityWithoutKey();
//		}
//		else {
//			actionEntity = action.toEntity();
//		}
//		
//		if (action.getActionKey() == null)
//			return new ActionBoundary(this.actionService.publishNewActionByPlayer(actionEntity));
//			//return new ActionBoundary(this.actionService.publishNewActionWithGeneratedKeyByPlayer(action.toEntityWithoutKey()));
//		else
//			return new ActionBoundary(this.actionService.publishNewActionByPlayer(actionEntity));
//	}
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/smartspace/actions",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public Object performAction (@RequestBody ActionBoundary action) {
		
		ActionEntity actionEntity;
		
		if (action.getActionKey() == null) {
			actionEntity = action.toEntityWithoutKey();
		}
		else {
			actionEntity = action.toEntity();
		}
		
		return 
				this.actionService
					.performAction(
							actionEntity);
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorMessage handleException(ElementNotFoundException e) {
		String message = e.getMessage();
		
		if (message == null || message.trim().isEmpty())
			message = "could not find error message";
		
		return new ErrorMessage(message);
	}
}
