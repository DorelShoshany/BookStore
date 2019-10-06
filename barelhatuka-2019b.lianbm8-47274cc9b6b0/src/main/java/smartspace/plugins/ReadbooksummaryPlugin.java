package smartspace.plugins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.dao.AdvancedElementDao;
import smartspace.dao.AdvancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;

@Component
public class ReadbooksummaryPlugin implements Plugin {
	private AdvancedUserDao dao;
	private AdvancedElementDao elementDao;

	@Autowired	
	public ReadbooksummaryPlugin(AdvancedUserDao dao, AdvancedElementDao elementDao) {
		super();
		this.dao = dao;
		this.elementDao = elementDao;
	}

	@Override
	public Object action(ActionEntity action) throws Exception {
		// user is present for sure because the key was already checked back in Publish method
		UserEntity userEntity = dao.findUserByKey(action.getPlayerSmartspace() + "#" + action.getPlayerEmail()).get();
		long points = userEntity.getPoints() + 25;
		userEntity.setPoints(points);
		dao.updateUserPoints(userEntity);
		
		ElementEntity element = elementDao.readById(action.getElementSmartspace() + "#" + action.getElementId()).get();
		String summary = (String)(element.getMoreAttributes().getOrDefault("summary", "Once upon a time..."));
		String bookName = element.getName();

		return new GeneralStringResponse("Summary of " + bookName + " is:\n" + summary +
				"\n\nGood Job at reading book summary! Your score: " + points);
	}

}
