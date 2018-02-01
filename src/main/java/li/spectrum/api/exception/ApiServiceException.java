package li.spectrum.api.exception;


public class ApiServiceException extends RuntimeException {

	private static final long serialVersionUID = -6334411641057809807L;

	private ErrorObject error;

	public ApiServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiServiceException(String message) {
		super(message);
	}

	public ApiServiceException(ErrorObject error) {
		super();
		this.error = error;
	}

	public ErrorObject getError() {
		return error;
	}

	public void setError(ErrorObject error) {
		this.error = error;
	}

}
