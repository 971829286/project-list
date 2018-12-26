package com.souche.bmgateway.core.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * 参数校验器
 *
 * @since 2018/07/09
 */
public class ParamsValidate {

    public static <T> ParamsValidateResult<T> validate(T t) {
        boolean hasError = false;
        StringBuffer errMsg = new StringBuffer();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Set<ConstraintViolation<T>> errSet = factory.getValidator().validate(t);
        if (!errSet.isEmpty()) {
            hasError = true;
        }
        for (ConstraintViolation<T> constraintViolation : errSet) {
            errMsg.append(constraintViolation.getMessage() + ",");
        }
        return new ParamsValidateResult<T>(t, errMsg.toString(), hasError);
    }

    public static class ParamsValidateResult<T> {
        private T data;
        private String msgError = "";
        private boolean hasError = false;

        public ParamsValidateResult(T t, String msgError, boolean hasError) {
            this.data = t;
            this.msgError = msgError;
            this.hasError = hasError;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public String getMsgError() {
            return msgError;
        }

        public void setMsgError(String msgError) {
            this.msgError = msgError;
        }

        public boolean hasError() {
            return hasError;
        }

        public void isHasError(boolean hasError) {
            this.hasError = hasError;
        }

        @Override
        public String toString() {
            return "ParamsValidateResult [data=" + data + ", msgError=" + msgError + ", hasError=" + hasError + "]";
        }
    }

}
