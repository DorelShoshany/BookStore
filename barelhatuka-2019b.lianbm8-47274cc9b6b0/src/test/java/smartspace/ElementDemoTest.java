package smartspace;

//import static org.assertj.core.api.Assertions.assertThat;

//import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//import smartspace.data.ElementEntity;
//import smartspace.data.Location;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ElementDemoTest {

	@Test
	public void unitTestElementDemo() {
		// GIVEN i created a new ElementDemo
	//	ElementDemo demo = new ElementDemo();
		// WHEN we call create method with new book
	//	ElementEntity book1 = new ElementEntity("The Passage", "Book", "Justin Cronin", new Location(0, 0), new Date(), "gmail.com", false, 900, 100, "English", "awesome fucking book", "Horror");
	//	book1 = demo.create(book1);

		// THEN it contains the message
	//	assertThat(demo.readAll()).usingElementComparator((b1, b2) -> b1.getType().compareTo(b2.getType()))
		//		.contains(new ElementEntity("Book"));
	}

	//@Test(expected = Exception.class)
	public void unitTestCreateNullBook() {
		// GIVEN i created a new ElementDemo
	//	ElementDemo demo = new ElementDemo();
		// WHEN we call create method with null
	//	demo.create(null);
		// THEN the method throws exception
	}

	@Test
	public void unitTestCreateBookAndValidateTotalSize() {
		// GIVEN i created a new ElementDemo
	//	ElementDemo demo = new ElementDemo();
		// WHEN we call create method with new book
	//	ElementEntity book1 = new ElementEntity("The Passage", "Book", "Justin Cronin", new Location(0, 0), new Date(), "gmail.com", false, 900, 100, "English", "awesome fucking book", "Horror");
	//	book1 = demo.create(book1);
		// THEN the internal list size is 1
	//	assertThat(demo.readAll()).hasSize(1);
	}
	
	@Test
	public void unitTestUpdateLocation() {
		// GIVEN i created a new ElementDemo
	//	ElementDemo demo = new ElementDemo();
		// WHEN we call create method with new book
	//	ElementEntity book1 = new ElementEntity("The Passage", "Book", "Justin Cronin", new Location(0, 0), new Date(), "gmail.com", false, 900, 100, "English", "awesome fucking book", "Horror");
	//	book1 = demo.create(book1);
	//	System.out.println(book1);
		// THEN i want to see it changed
	//	book1.setLocation(new Location(1, 1));
	//	demo.update(book1);
	//	System.out.println(book1);
	}

}
