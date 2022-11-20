package xyz.liyouxiu.reggie.common;

/**
 * @author liyouxiu
 * @date 2022/11/20 21:42
 * 自定义业务异常
 */
public class CustomException extends RuntimeException {
    public CustomException(String message){
        super(message);
    }
}
