package pl.codecity.main.exception;

public class ServiceException extends RuntimeException {

	public ServiceException() {
		super();
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
