package cn.ourwill.willcenter.aop;

public class UnauthenticatedException extends RuntimeException {

	private static final long serialVersionUID = 5012095326195145398L;

	public UnauthenticatedException() {
	}

	public UnauthenticatedException(String message) {
		super(message);
	}

	public UnauthenticatedException(Throwable cause) {
		super(cause);
	}

	public UnauthenticatedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnauthenticatedException(String message, Throwable cause,
                                    boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
