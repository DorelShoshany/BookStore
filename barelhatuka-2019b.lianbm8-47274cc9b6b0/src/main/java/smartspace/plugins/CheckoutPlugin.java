package smartspace.plugins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.dao.AdvancedElementDao;
import smartspace.dao.AdvancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;

@Component
public class CheckoutPlugin implements Plugin {
	private AdvancedUserDao userDao;
	private AdvancedElementDao elementDao;

	@Autowired	
	public CheckoutPlugin(AdvancedUserDao dao, AdvancedElementDao elements) {
		super();
		this.userDao = dao;
		this.elementDao = elements;
	}

	@Override
	public Object action(ActionEntity action) throws Exception {
		
		ElementEntity element = elementDao.readById(action.getElementSmartspace() + "#" + action.getElementId()).get();
		int counter = (int) element.getMoreAttributes().get("check in");
		
		if (counter % 2 == 1) {
			element.getMoreAttributes().put("check in", counter + 1);
			elementDao.updateElementCheckIn(element);
			
			UserEntity userEntity = userDao.findUserByKey(action.getPlayerSmartspace() + "#" + action.getPlayerEmail()).get();
			long points = userEntity.getPoints() - 10;
			userEntity.setPoints(points);
			userDao.updateUserPoints(userEntity);
			
			String bookName = element.getName();
			
			return new GeneralStringResponse(bookName + " has been successfully removed "
					+ "from your favorites list! Your score: " + points);
		}
		else if (counter == 0) {
			throw new RuntimeException("You should first add this book to your"
					+ " favorites before you try to remove it :)");
		}
		else {
			throw new RuntimeException("This book has already been removed from your favorites list!");
		}
	}
}
