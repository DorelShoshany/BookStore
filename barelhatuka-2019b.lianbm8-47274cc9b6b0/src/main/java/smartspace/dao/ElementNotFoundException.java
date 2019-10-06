package smartspace.dao;

public class ElementNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -8789812433065971296L;

	public ElementNotFoundException() {

	}

	public ElementNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ElementNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ElementNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
