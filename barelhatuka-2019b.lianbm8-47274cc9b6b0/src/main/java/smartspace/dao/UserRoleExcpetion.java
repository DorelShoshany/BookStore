package smartspace.dao;

public class UserRoleExcpetion extends RuntimeException{
	private static final long serialVersionUID = 2930562196455266604L;

	public UserRoleExcpetion() {
	}

	public UserRoleExcpetion(String message) {
		super(message);
	}

	public UserRoleExcpetion(Throwable cause) {
		super(cause);
	}

	public UserRoleExcpetion(String message, Throwable cause) {
		super(message, cause);
	}

}
