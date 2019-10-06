package smartspace.aop;

import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import smartspace.dao.AdvancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Component
@Aspect
public class ManagerOrPlayerValidationAspect {
	private AdvancedUserDao userDao;

	@Autowired
	public ManagerOrPlayerValidationAspect(AdvancedUserDao userDao) {
		this.userDao = userDao;
	}

	@Around("@annotation(smartspace.aop.MangerOrPlayerValidation) && args(smartspace, email, userFlag, ..)")
	public Object validateRole(ProceedingJoinPoint pjp, String smartspace, String email, String userFlag) throws Throwable {
		
		Optional<UserEntity> realEntity = this.userDao.findUserByKey(smartspace + "#" + email);
		Object[] arguments = pjp.getArgs();
		
		if (!realEntity.isPresent())  
			arguments[2] = "null";
		else if (realEntity.get().getRole() == UserRole.ADMIN)
			arguments[2] = "admin";
		else if (realEntity.get().getRole() == UserRole.MANAGER)
			arguments[2] = "manager";
		else if (realEntity.get().getRole() == UserRole.PLAYER)
			arguments[2] = "player";
		else
			arguments[2] = "nothing";

		try {
			Object rv = pjp.proceed(arguments);
			return rv;
		} catch (Throwable e) {
			throw e;
		}
		
	}
}
