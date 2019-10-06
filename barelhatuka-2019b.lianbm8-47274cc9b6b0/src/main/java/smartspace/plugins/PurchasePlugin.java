package smartspace.plugins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.dao.AdvancedElementDao;
import smartspace.dao.AdvancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;

@Component
public class PurchasePlugin implements Plugin {
	private AdvancedUserDao userDao;
	private AdvancedElementDao elementDao;

	@Autowired	
	public PurchasePlugin(AdvancedUserDao dao, AdvancedElementDao elements) {
		super();
		this.userDao = dao;
		this.elementDao = elements;
	}
	
	@Override
	public Object action(ActionEntity action) throws Exception {
		ElementEntity element = elementDao.readById(action.getElementSmartspace() + "#" + action.getElementId()).get();
		String bookName = element.getName();
		
		if (!element.isExpired()) {
			UserEntity userEntity = userDao.findUserByKey(action.getPlayerSmartspace() + "#" + action.getPlayerEmail()).get();
			long points = userEntity.getPoints() + 30;
			userEntity.setPoints(points);
			userDao.updateUserPoints(userEntity);
			
			element.setExpired(true);	//purchase means expired is false
			elementDao.update(element);
			
			return new GeneralStringResponse("You've just purchased " + bookName + " book! Your points: " + points);
		}
		else {
			throw new RuntimeException("Sorry, " + bookName + " was already purchased!");
		}
		

	}
	
}
