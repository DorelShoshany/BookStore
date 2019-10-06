package smartspace.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartspace.aop.MangerOrPlayerValidation;
import smartspace.dao.AdvancedElementDao;
import smartspace.dao.AdvancedUserDao;
import smartspace.dao.ElementNotFoundException;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Service
public class ElementServiceImpl implements ElementService {
	private AdvancedElementDao elements;
	private AdvancedUserDao userDao;
	private String smartspcae;

	@Autowired
	public ElementServiceImpl(AdvancedElementDao elements, AdvancedUserDao userDao) {
		super();
		this.elements = elements;
		this.userDao = userDao;
	}

	@Value("${smartspace.name:2019B.Lianbm8}")
	public void setSmartspace(String smartspace) {
		this.smartspcae = smartspace;
	}

	@Override
	@Transactional
	public List<ElementEntity> publishElementsByAdmin(List<ElementEntity> elements, String smartspace, String email) {
		checkIfUserAdmin(smartspace, email);

		// if we got up to here then user is admin
		List<ElementEntity> newElements = new ArrayList<>();

		for (int i = 0 ; i < elements.size() ; i++) {
			ElementEntity current = elements.get(i);
			if (current.getElementSmartspace().equals(this.smartspcae)) {
				throw new RuntimeException("Can't post element from 2019B.Lianbm8 project");
			} 
			else if (checkKey(current.getElementSmartspace(), current.getElementId()))
				throw new RuntimeException("Illegal element key input");
			else if (!validate(current)) {
				throw new RuntimeException("Illegal element input");
			}
			else {
				someSettings(current);
				newElements.add(this.elements.createFromImport(current));
			}
		}
		return newElements;
	}

	@Override
	@Transactional
	public ElementEntity publishNewElementByManager(ElementEntity elementEntity, String role) {
		if (!role.equals("manager")) {
			throw new RuntimeException("User must be Manager!");
		}
		else if (checkKey(elementEntity.getElementSmartspace(), elementEntity.getElementId()))
			throw new RuntimeException("Illegal element key input");
		else if (!validate(elementEntity)) {
			throw new RuntimeException("Illegal element input");
		}
		else {
			someSettings(elementEntity);
			return this.elements.create(elementEntity);
		}
	}

	private boolean checkKey(String smartspace, String id) {
		return smartspace == null || smartspace.trim().isEmpty() || id == null || id.trim().isEmpty();
	}
	
	private void someSettings(ElementEntity elementEntity) {
		elementEntity.setCreationTimestamp(new Date());
		if (elementEntity.getMoreAttributes() == null)
			elementEntity.setMoreAttributes((new HashMap<>()));
		elementEntity.getMoreAttributes().put("check in", 0);
	}

	@Override
	@Transactional
	public ElementEntity publishNewElementWithGeneratedKeyByManager(ElementEntity elementEntity, String role) {
		if (!role.equals("manager")) {
			throw new RuntimeException("User must be Manager!");
		}
		else if (!validate(elementEntity)) {
			throw new RuntimeException("Illegal element input");
		} else {
			someSettings(elementEntity);
			return this.elements.create(elementEntity);
		}
	}

	private boolean validate(ElementEntity elementEntity) {
		return elementEntity != null && elementEntity.getCreatorEmail() != null
				&& !elementEntity.getCreatorEmail().trim().isEmpty() && elementEntity.getCreatorEmail().contains("@")
				&& elementEntity.getLocation() != null && elementEntity.getLocation().getX() >= 0
				&& elementEntity.getLocation().getY() >= 0 && elementEntity.getType() != null
				&& !elementEntity.getType().trim().isEmpty() && elementEntity.getName() != null
				&& !elementEntity.getName().trim().isEmpty() && elementEntity.getCreatorSmartspace() != null
				&& !elementEntity.getCreatorSmartspace().trim().isEmpty();
	}

	@Override
	public List<ElementEntity> getElements(int size, int page, String smartspace, String email) {
		checkIfUserAdmin(smartspace, email);
		System.err.println(smartspace + " " + email);
		return elements.readAll(size, page);
	}

	@Override
	public void checkIfUserAdmin(String smartspace, String email) {
		String key = smartspace + "#" + email;
		Optional<UserEntity> user = this.userDao.findUserByKey(key);
		if (!user.isPresent())
			throw new RuntimeException("Admin Doesn't Exist");
		if (user.get().getRole() != UserRole.ADMIN)
			throw new RuntimeException("User must be Admin!");
	}

	@Override
	@MangerOrPlayerValidation
	public String checkUserRole(String smartspace, String email, String userFlag) {
		return userFlag;
	}

	@Override
	@Transactional
	public Optional<ElementEntity> getSpecificElement(String smartspace, String id, String role) {

		Optional<ElementEntity> tempElement = elements.readById(smartspace + "#" + id);

		if (tempElement.isPresent()) {
			if (role.equals("manager")) {
				return tempElement;
			}
			else if (role.equals("player")) {
				if (!tempElement.get().isExpired())
					return tempElement;
			}
			else {	// nothing or admin
				throw new RuntimeException("Admin can't get element");
			}
		}		
		throw new ElementNotFoundException("element doesn't exist");
	}

	@Override
	public String checkQueryType(String x, String y, String distance, String search, String value) {
		String name = "", type = "";
		if (search.equals("name"))
			name = value;
		else if (search.equals("type"))
			type = value;

		if (x.trim().isEmpty() && y.trim().isEmpty() && distance.trim().isEmpty() && name.trim().isEmpty()
				&& type.trim().isEmpty())
			return "empty";
		else if (!x.trim().isEmpty() && !y.trim().isEmpty() && !distance.trim().isEmpty())
			return "distance";
		else if (!name.trim().isEmpty())
			return "name";
		else if (!type.trim().isEmpty())
			return "type";
		else
			throw new RuntimeException("Invalid Query");
	}

	@Override
	public void updateElementByManager(ElementEntity update, String elementSmartspace, String elementId, String role) {
		if (role.equals("manager")) {
			update.setKey(elementSmartspace + "#" + elementId);
			elements.update(update);
		}
		else {
			throw new RuntimeException("User Must be MANGER!!!");
		}
	}

	@Override
	public List<ElementEntity> filterExpired(int size, int page) {
		List<ElementEntity> filtered = new ArrayList<>();
		filtered = this.elements.readAll(size, page).stream().filter(e -> !e.isExpired()).collect(Collectors.toList());
		return filtered;
	}

	@Override
	@Transactional
	public List<ElementEntity> getElementsByDistance(int size, int page, String x, String y, String distance,
			String role) {
		if (role.equals("player"))
			return this.elements
					.readAllByLocation_XBetweenAndLocation_YBetween(Double.parseDouble(x), Double.parseDouble(y),
							Double.parseDouble(distance), page, size)
					.stream().filter(e -> !e.isExpired()).collect(Collectors.toList());
		else if (role.equals("manager"))
			return this.elements.readAllByLocation_XBetweenAndLocation_YBetween(Double.parseDouble(x),
					Double.parseDouble(y), Double.parseDouble(distance), page, size);
		else
			throw new RuntimeException("User must be Player or Manager");
	}

	@Override
	@Transactional
	public List<ElementEntity> getElementsByName(int size, int page, String name, String role) {
		if (role.equals("player"))
			return this.elements.readAllByName(size, page, name).stream().filter(e -> !e.isExpired())
					.collect(Collectors.toList());
		else if (role.equals("manager"))
			return this.elements.readAllByName(size, page, name);
		else
			throw new RuntimeException("User must be Player or Manager");
	}

	@Override
	@Transactional
	public List<ElementEntity> getElementsByType(int size, int page, String type, String role) {
		if (role.equals("player"))
			return this.elements.readAllByType(size, page, type).stream().filter(e -> !e.isExpired())
					.collect(Collectors.toList());
		else if (role.equals("manager"))
			return this.elements.readAllByType(size, page, type);
		else
			throw new RuntimeException("User must be Player or Manager");
	}

	@Override
	@Transactional
	public List<ElementEntity> getAllElements(int size, int page, String role) {
		if (role.equals("player"))
			return this.elements.readAll(size, page).stream().filter(e -> !e.isExpired()).collect(Collectors.toList());
		else if (role.equals("manager"))
			return this.elements.readAll(size, page);
		else
			throw new RuntimeException("User must be Player or Manager");
	}

}
