package cn.ourwill.huiyizhan.aop;

/**
 * @version 1.0
 * @Author jixuan.lin@ourwill.com.cn
 * @Time 2018/5/21 11:51
 * @Description
 */
public class BlacklistInException extends RuntimeException {
    private static final long serialVersionUID = 5012095326195145343L;

    public BlacklistInException() {
    }

    public BlacklistInException(String message) {
        super(message);
    }

    public BlacklistInException(Throwable cause) {
        super(cause);
    }

    public BlacklistInException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlacklistInException(String message, Throwable cause,
                                 boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
