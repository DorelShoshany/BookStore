package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.AdvancedElementDao;
import smartspace.data.ElementEntity;

@Repository
public class RdbElementDao implements AdvancedElementDao {
	private ElementCrud elementCrud;
	private IdCreatorCrud idCreatorCrud;
	private String smartspace;

	public RdbElementDao() {
	}

	@Autowired
	public RdbElementDao(ElementCrud elementCrud, IdCreatorCrud idCretorCrud) {
		this.elementCrud = elementCrud;
		this.idCreatorCrud = idCretorCrud;
	}
	
	@Value("${smartspace.name:2019B.Lianbm8}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

	@Override
	@Transactional
	public ElementEntity create(ElementEntity element) {
		// TODO - replace by key generated using DB
		element.setElementSmartspace(smartspace);
		element.setCreatorSmartspace(smartspace);
		IdCreator idCreator = this.idCreatorCrud.save(new IdCreator());
		element.setKey(element.getElementSmartspace() + "#" + idCreator.getId());
		
		// SQL INSERT
		if (!this.elementCrud.existsById(element.getKey())) {
			ElementEntity rv = this.elementCrud.save(element);
			this.idCreatorCrud.delete(idCreator);
			return rv;
		} else {
			throw new RuntimeException("Element already exists id: " + element.getKey());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ElementEntity> readById(String key) {
		// SQL SELECT
		return this.elementCrud.findById(key);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> readAll() {
		List<ElementEntity> list = new ArrayList<ElementEntity>();
		// SQL SELECT
		this.elementCrud.findAll().forEach(list::add);
		return list;
	}

	@Override
	@Transactional
	public void update(ElementEntity element) {
		ElementEntity found = this.readById(element.getKey())
				.orElseThrow(() -> new RuntimeException("Invalid element key:  " + element.getKey()));

		if (element.getElementSmartspace() != null) {
			found.setElementSmartspace(element.getElementSmartspace());
		}

		if (element.getLocation() != null) {
			found.setLocation(element.getLocation());
		}
		
		found.setExpired(element.isExpired());

		if (element.getName() != null) {
			found.setName(element.getName());
		}

		if (element.getType() != null) {
			found.setType(element.getType());
		}

		if (found.getCreatorEmail() != element.getCreatorEmail()) {
			found.setCreatorEmail(element.getCreatorEmail());
		}
		
		if (found.getMoreAttributes() != element.getMoreAttributes()) {
			int checkInCounter = (int) found.getMoreAttributes().get("check in");
			found.setMoreAttributes(element.getMoreAttributes());
			found.getMoreAttributes().put("check in", checkInCounter);
		}

		// SQL UPDATE
		this.elementCrud.save(found);
	}

	@Override
	@Transactional
	public void deleteByKey(String key) {
		// SQL DELETE
		ElementEntity found = readById(key).orElseThrow(() -> new RuntimeException("Invalid element key: " + key));
		this.elementCrud.delete(found);
	}

	@Override
	@Transactional
	public void delete(ElementEntity element) {
		// SQL DELETE
		this.elementCrud.delete(element);

	}

	@Override
	public void deleteAll() {
		// SQL DELETE
		this.elementCrud.deleteAll();
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAll(int size, int page) {
		return this.elementCrud
				.findAll(PageRequest.of(page, size))
				.getContent();
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAll(int size, int page, String sortAttr) {
		return this.elementCrud
				.findAll(PageRequest.of(page, size, Direction.ASC, sortAttr))
				.getContent();
	}

	@Override
	@Transactional
	public ElementEntity createFromImport(ElementEntity element) {
		element.setKey(element.getElementSmartspace() + "#" + element.getElementId());
		
		// SQL INSERT
		if (!this.elementCrud.existsById(element.getKey())) {
			ElementEntity rv = this.elementCrud.save(element);
			return rv;
		} else {
			throw new RuntimeException("Element already exists id: " + element.getKey());
		}
	}

	@Override
	public List<ElementEntity> readAllByLocation_XBetweenAndLocation_YBetween(double x, double y, double distance, int page, int size) {
		return this.elementCrud.findAllByLocation_XBetweenAndLocation_YBetween(x - distance, x + distance, y - distance, y + distance,
				PageRequest.of(page, size, Direction.ASC, "creationTimestamp"));
	}

	@Override
	public List<ElementEntity> readAllByType(int size, int page, String type) {
		return this.elementCrud.findAllByType(type, PageRequest.of(page, size, Direction.ASC, "creationTimestamp"));
	}

	@Override
	public List<ElementEntity> readAllByName(int size, int page, String name) {
		return this.elementCrud.findAllByName(name, PageRequest.of(page, size, Direction.ASC, "creationTimestamp"));
	}

	@Override
	public List<ElementEntity> readAllByMoreAttributesLike(String string, int page, int size) {
		return this.elementCrud.findAllByMoreAttributesLike(string, 
				PageRequest.of(page, size, Direction.ASC, "creationTimestamp"));
	}

	@Override
	public void updateElementCheckIn(ElementEntity element) {
		ElementEntity found = this.readById(element.getKey())
				.orElseThrow(() -> new RuntimeException("Invalid element key:  " + element.getKey()));
		
		found.getMoreAttributes().put("check in", element.getMoreAttributes().get("check in"));
		
		// SQL UPDATE
		this.elementCrud.save(found);
	}

}
