package li.spectrum.api.exception;

public class ErrorObject {

	public static ErrorObject UNEXPECTED_ERROR = new ErrorObject(1,
			"An unexpected error occurred.  Please see the log files.");
	public static ErrorObject METHOD_NOT_IMPLEMENTED = new ErrorObject(2, "The method has not been implemented");

	public static ErrorObject UNKNOWN_SESSION_IDENTIFIER = new ErrorObject(201, "Unknown session identifier");
	public static ErrorObject EXPIRED_SESSION = new ErrorObject(202, "The specified session has expired");
	public static ErrorObject NON_EXISTENT_SESSION = new ErrorObject(203, "The specified session does not exist");

	public static ErrorObject TRANSACTION_CONTENT_NOT_PROVIDED = new ErrorObject(303,
			"Transaction data must be provided");

	private int code;
	private String message;

	public ErrorObject() {
	}

	public ErrorObject(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toString() {
		return "Code: " + this.getCode() + " Message: " + this.getMessage();
	}

}
