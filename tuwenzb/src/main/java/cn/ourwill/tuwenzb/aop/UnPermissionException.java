package cn.ourwill.tuwenzb.aop;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/24 0024 10:17
 * @Version1.0
 */
public class UnPermissionException extends RuntimeException {
    private static final long serialVersionUID = 5012095326195145310L;

    public UnPermissionException() {
    }

    public UnPermissionException(String message) {
        super(message);
    }

    public UnPermissionException(Throwable cause) {
        super(cause);
    }

    public UnPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnPermissionException(String message, Throwable cause,
                                    boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
