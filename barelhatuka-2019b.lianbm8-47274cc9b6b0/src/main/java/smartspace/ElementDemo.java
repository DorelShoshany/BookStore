package smartspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

//import org.springframework.stereotype.Repository;

import smartspace.dao.ElementDao;
import smartspace.data.ElementEntity;


//@Repository
public class ElementDemo implements ElementDao<String> {

	private List<ElementEntity> elements;
	private AtomicLong ID;
	
	public ElementDemo() {
		this.elements = Collections.synchronizedList(new ArrayList<>());
		this.ID = new AtomicLong(1L);
	}
	
	@Override
	public ElementEntity create(ElementEntity element) {
		element.setKey(element.getElementSmartspace() + "#" + this.ID.getAndIncrement());
		this.elements.add(element);
		return element;
	}

	@Override
	public Optional<ElementEntity> readById(String key) {
		return this.elements.stream().filter(book->book.getKey().equals(key)).findFirst();
	}

	@Override
	public List<ElementEntity> readAll() {
		// TODO Auto-generated method stub
		return this.elements;
	}

	@Override
	public void update(ElementEntity element) {
		ElementEntity found = 
				readById(element.getKey())
					.orElseThrow(()->new RuntimeException("Invalid book key: " + element.getKey()));
			
		found.setCreationTimestamp(element.getCreationTimestamp());
		found.setCreatorEmail(element.getCreatorEmail());
		found.setCreatorSmartspace(element.getCreatorSmartspace());
		found.setElementId(element.getElementId());
		found.setExpired(element.isExpired());
		found.setLocation(element.getLocation());
		found.setKey(element.getKey());
		found.setType(element.getType());

		if (element.getName() != null)
			found.setName(element.getName());
		if (element.getMoreAttributes() != null)
			found.setMoreAttributes(element.getMoreAttributes());
		
	}

	@Override
	public void deleteByKey(String key) {
		// TODO Auto-generated method stub
		ElementEntity found = 
				readById(key)
					.orElseThrow(()->new RuntimeException("Invalid book key: " + key));
		this.elements.remove(found);
	}

	@Override
	public void delete(ElementEntity element) {
		// TODO Auto-generated method stub
		this.elements.remove(element);
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		this.elements.clear();
	}


}
