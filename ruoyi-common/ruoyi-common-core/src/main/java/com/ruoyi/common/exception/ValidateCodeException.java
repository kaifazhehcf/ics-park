package com.ruoyi.common.exception;

/**
 * 验证异常
 *
 * @author jack
 */
public class ValidateCodeException extends Exception {
    private static final long serialVersionUID = 3887472968823615091L;

    public ValidateCodeException() {
    }

    public ValidateCodeException(String msg) {
        super(msg);
    }
}