package pl.codecity.main.exception;

public class EmptyCodeException extends ServiceException {

	public EmptyCodeException() {
		super();
	}

	public EmptyCodeException(Throwable cause) {
		super(cause);
	}

	public EmptyCodeException(String message) {
		super(message);
	}

	public EmptyCodeException(String message, Throwable cause) {
		super(message, cause);
	}
}
