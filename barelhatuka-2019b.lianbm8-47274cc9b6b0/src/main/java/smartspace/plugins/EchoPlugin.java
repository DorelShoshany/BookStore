package smartspace.plugins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.dao.AdvancedUserDao;
import smartspace.data.ActionEntity;
import smartspace.data.UserEntity;

@Component
public class EchoPlugin implements Plugin {
	private AdvancedUserDao userDao;

	@Autowired	
	public EchoPlugin(AdvancedUserDao dao) {
		super();
		this.userDao = dao;
	}
	
	@Override
	public Object action(ActionEntity action) throws Exception {
		UserEntity userEntity = userDao.findUserByKey(action.getPlayerSmartspace() + "#" + action.getPlayerEmail()).get();
		long points = userEntity.getPoints() - 10;
		userEntity.setPoints(points);
		userDao.updateUserPoints(userEntity);
		
		return new GeneralStringResponse("Echo operation is absolutely meaningful!"
				+ " Therefore, you're losing 10 points! Your current points: " +  points);
	}
}
