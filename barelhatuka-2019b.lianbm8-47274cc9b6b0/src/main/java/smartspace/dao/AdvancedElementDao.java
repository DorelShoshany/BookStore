package smartspace.dao;
import java.util.List;
import smartspace.data.ElementEntity;


public interface AdvancedElementDao extends ElementDao<String> {
	public ElementEntity createFromImport(ElementEntity element);
	public List<ElementEntity> readAll(int size, int page);
	public List<ElementEntity> readAll(int size, int page, String sortAttr);
	public List<ElementEntity> readAllByLocation_XBetweenAndLocation_YBetween(double x, double y, double distance, int page, int size);
	public List<ElementEntity> readAllByType(int size, int page, String type);
	public List<ElementEntity> readAllByName(int size, int page, String name);
	public void updateElementCheckIn(ElementEntity element);
	
	//added for MarkPagePlugin action
	public List<ElementEntity> readAllByMoreAttributesLike(String string, int page, int size);
}
