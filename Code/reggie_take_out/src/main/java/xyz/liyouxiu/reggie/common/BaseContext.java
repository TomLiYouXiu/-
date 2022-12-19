package xyz.liyouxiu.reggie.common;

/**
 * @author liyouxiu
 * @date 2022/11/20 16:01
 * 基于ThreadLocal封装的工具类，用于保存和获取当前登录用户
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 获取值
     * @return
     */
    public static Long getThreadLocal() {
        return threadLocal.get();
    }

    /**
     * 设置值
     * @param id
     */
    public static void setThreadLocal(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取值
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
