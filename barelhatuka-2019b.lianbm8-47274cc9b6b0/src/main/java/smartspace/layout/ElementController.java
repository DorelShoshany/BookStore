package smartspace.layout;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

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
import smartspace.data.ElementEntity;
import smartspace.logic.ElementService;

@RestController
public class ElementController {
	private ElementService elementService;

	@Autowired
	public ElementController(ElementService elements) {
		super();
		this.elementService = elements;
	}

	@RequestMapping(method = RequestMethod.POST,
			path = "/smartspace/admin/elements/{adminSmartspace}/{adminEmail}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] publish(
			@PathVariable("adminSmartspace") String smartspace,
			@PathVariable("adminEmail") String email,
			@RequestBody ElementBoundary[] elements) {

		List<ElementEntity> entities = new ArrayList<>();
		
		for (int i=0 ; i<elements.length ; i++) {
			entities.add(elements[i].toEntity());
		}
		
		entities = this.elementService.publishElementsByAdmin(entities, smartspace, email);
		
		return entities.stream() // ElementEntity Stream
				.map(ElementBoundary::new) // ElementBoundary Stream
				.collect(Collectors.toList()) // ElementBoundary List
				.toArray(new ElementBoundary[0]); // ElementBoundary[]
	}

	@RequestMapping(method = RequestMethod.GET,
			path = "/smartspace/admin/elements/{adminSmartspace}/{adminEmail}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElements(
			@PathVariable("adminSmartspace") String smartspace,
			@PathVariable("adminEmail") String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		return this.elementService.getElements(size, page, smartspace, email)
				.stream()
				.map(ElementBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ElementBoundary[0]);

	}

	@RequestMapping(method = RequestMethod.POST,
			path = "/smartspace/elements/{managerSmartspace}/{managerEmail}",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary publish(
			@PathVariable("managerSmartspace") String smartspace,
			@PathVariable("managerEmail") String email,
			@RequestBody ElementBoundary element) {
		
		String role = elementService.checkUserRole(smartspace, email, "");
		element.setCreator(new Creator(smartspace, email));

		if (element.getKey() == null) {
			return new ElementBoundary(elementService.publishNewElementWithGeneratedKeyByManager(
					element.toEntityWithoutKey(), role));
		}
		else {
			return new ElementBoundary(elementService.publishNewElementByManager(element.toEntity(), role));
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, path = "/smartspace/elements/{managerSmartspace}/{managerEmail}/{elementSmartspace}/{elementId}", 
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void put(@PathVariable("managerSmartspace") String smartspace,
			@PathVariable("managerEmail") String email,
			@PathVariable("elementSmartspace") String elementSmartspace,
			@PathVariable("elementId") String elementId,
			@RequestBody ElementBoundary update) {
		
		String role = elementService.checkUserRole(smartspace, email, "");
		elementService.updateElementByManager(update.toEntityWithoutKey(), elementSmartspace, elementId, role);
	}
	
	@RequestMapping(method = RequestMethod.GET,
			path = "/smartspace/elements/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}", 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary getElement(
			@PathVariable("userSmartspace") String smartspace,
			@PathVariable("userEmail") String email,
			@PathVariable("elementSmartspace") String elementSmartspace,
			@PathVariable("elementId") String elementId) {
		
		String role = elementService.checkUserRole(smartspace, email, "");

		return new ElementBoundary(elementService.getSpecificElement(elementSmartspace, elementId, role).get());
	}
	
	@RequestMapping(method = RequestMethod.GET,
			path = "/smartspace/elements/{userSmartspace}/{userEmail}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary[] getElements(
			@PathVariable("userSmartspace") String smartspace,
			@PathVariable("userEmail") String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "x", required = false, defaultValue = "") String x,
			@RequestParam(name = "y", required = false, defaultValue = "") String y,
			@RequestParam(name = "distance", required = false, defaultValue = "") String distance,
			@RequestParam(name = "search", required = false, defaultValue = "") String search,
			@RequestParam(name = "value", required = false, defaultValue = "") String value) {

		String role = elementService.checkUserRole(smartspace, email, "");
		
		String query = elementService.checkQueryType(x, y, distance, search, value);

		List<ElementBoundary> elementsBoundaries = new ArrayList<>();
		List<ElementEntity> elementsEntities = new ArrayList<>();
		
		switch(query) {
		case "empty":
			elementsEntities = this.elementService.getAllElements(size, page, role);
			break;
		case "distance":
			elementsEntities = this.elementService.getElementsByDistance(size, page, x, y, distance, role);
			break;
		case "name":
			elementsEntities = this.elementService.getElementsByName(size, page, value, role);
			break;
		case "type":
			elementsEntities = this.elementService.getElementsByType(size, page, value, role);
			break;
		}
		
		elementsBoundaries = elementsEntities
				.stream()
				.map(ElementBoundary::new)
				.collect(Collectors.toList());
		
		return elementsBoundaries.toArray(new ElementBoundary[0]);
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
