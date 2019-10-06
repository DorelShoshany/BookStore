package smartspace.logic;

import java.util.List;
import java.util.Optional;

import smartspace.data.ElementEntity;

public interface ElementService {
	public ElementEntity publishNewElementByManager (ElementEntity elementEntity, String smartspace);
	public List<ElementEntity> getElements (int size, int page, String smartspace, String email);
	public void checkIfUserAdmin(String smartspace, String email);
	public String checkUserRole(String smartspace, String email, String userFlag);
	public Optional<ElementEntity> getSpecificElement(String smartspace, String id, String role);
	public String checkQueryType(String x, String y, String distance, String search, String value);
	public void updateElementByManager(ElementEntity update, String elementSmartspace, String elementId, String role);
	public List<ElementEntity> filterExpired(int size, int page);
	public List<ElementEntity> getElementsByDistance(int size, int page, String x, String y, String distance, String role);
	public List<ElementEntity> getElementsByName(int size, int page, String name, String role);
	public List<ElementEntity> getElementsByType(int size, int page, String type, String role);
	public List<ElementEntity> getAllElements (int size, int page, String role);
	public ElementEntity publishNewElementWithGeneratedKeyByManager(ElementEntity elementEntity, String smartspace);
	List<ElementEntity> publishElementsByAdmin(List<ElementEntity> elements, String smartspace, String email);
}
