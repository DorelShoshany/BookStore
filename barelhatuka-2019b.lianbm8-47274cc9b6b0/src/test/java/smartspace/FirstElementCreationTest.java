package smartspace;

//import java.util.Date;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.dao.ElementDao;
//import smartspace.data.ElementEntity;
//import smartspace.data.Location;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FirstElementCreationTest {
	private ElementDao<String> bookDao;
	
	@Autowired
	public void setElementDao(ElementDao<String> bookDao) {
		this.bookDao = bookDao;
	}
	
	@After
	public void teardown() {
		bookDao.deleteAll();
	}
	
	@Test
	public void testCreation() {
		// GIVEN nothing
		// WHEN i created a new book
//		ElementEntity book1 = new ElementEntity("The Passage", "Book", "Justin Cronin", new Location(0, 0), new Date(), "gmail.com", false, 900, 100, "English", "awesome fucking book", "Horror");
//		bookDao.create(book1);
//		
//		// THEN i check to see if it got an id
//		System.err.println(bookDao.readById(book1.getKey()) + "\n");
	}
	
	@Test
	public void testMultiplyCreation() {
		// GIVEN nothing
		// WHEN i create more then 1 book
//		ElementEntity book2 = new ElementEntity("The Passage", "Book", "Justin Cronin", new Location(0, 0), new Date(), "gmail.com", false, 900, 100, "English", "awesome fucking book", "Horror");
//		ElementEntity book3 = new ElementEntity("The Passage", "Book", "Justin Cronin", new Location(0, 0), new Date(), "gmail.com", false, 900, 100, "English", "awesome fucking book", "Horror");
//		ElementEntity book4 = new ElementEntity("The Passage", "Book", "Justin Cronin", new Location(0, 0), new Date(), "gmail.com", false, 900, 100, "English", "awesome fucking book", "Horror");
//		bookDao.create(book2);
//		bookDao.create(book3);
//		bookDao.create(book4);
		
		// THEN i want to see them all
//		for (ElementEntity element : bookDao.readAll())
//			System.out.println(element);
	}
	
	//@Test(expected=Exception.class)
	public void testDeleteInvalidElement() {
		// GIVEN nothing
		// WHEN i create 1 books
		// AND try to delete wrong 1 [invalid value]
	//	ElementEntity book2 = new ElementEntity("The Passage", "Book", "Justin Cronin", new Location(0, 0), new Date(), "gmail.com", false, 900, 100, "English", "awesome fucking book", "Horror");
	//	bookDao.create(book2);
	//	bookDao.deleteByKey(book2.getName());
		
		// THEN the method throws exception
	}

}
