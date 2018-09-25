package cn.ourwill.huiyizhan.aop;

public class UnauthenticatedRedirectException extends RuntimeException {

	private static final long serialVersionUID = 5012095326195145377L;

	public UnauthenticatedRedirectException() {
	}

	public UnauthenticatedRedirectException(String message) {
		super(message);
	}

	public UnauthenticatedRedirectException(Throwable cause) {
		super(cause);
	}

	public UnauthenticatedRedirectException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnauthenticatedRedirectException(String message, Throwable cause,
                                          boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
