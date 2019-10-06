package smartspace;

//import java.util.Date;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.dao.ActionDao;
import smartspace.dao.ElementDao;
//import smartspace.data.ActionEntity;
//import smartspace.data.ElementEntity;
//import smartspace.data.Location;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FirstActionCreationTest {
	private ActionDao actionDao;
	private ElementDao<String> bookDao;
	
	@Autowired
	public void setElementDao(ElementDao<String> bookDao) {
		this.bookDao = bookDao;
	}
	
	@Autowired
	public void setActionDao(ActionDao actionDao) {
		this.actionDao = actionDao;
	}
	
	@After
	public void teardown() {
		actionDao.deleteAll();
		bookDao.deleteAll();
	}
	
	@Test
	public void testCreationBookAndAction() {
		// GIVEN creation of book
//		ElementEntity book1 = new ElementEntity("The Passage", "Book", "Justin Cronin", new Location(0, 0), new Date(),
//				"gmail.com", false, 900, 100, "English", "awesome fucking book", "Horror");
//		bookDao.create(book1);
//		// WHEN I create actions
//		ActionEntity action1 = new ActionEntity(book1.getElementId(), "gmail.com", "turning page", new Date());
//		ActionEntity action2 = new ActionEntity(book1.getElementId(), "gmail.com", "turning page", new Date());
//		ActionEntity action3 = new ActionEntity(book1.getElementId(), "gmail.com", "marking page", new Date());
//		actionDao.create(action1);
//		actionDao.create(action2);
//		actionDao.create(action3);
//		
//		// THEN I check to see if it got an id
//		for (ActionEntity action : actionDao.readAll()) {
//			System.out.println(action);
//		}
	}
}
