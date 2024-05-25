package com.xin.ojcodesandbox.security;

/**
 * @author 15712
 */
public class NowSecurityManager extends SecurityManager {
    private static final int MAX_MEMORY = 1024 * 1024 * 1024;

    /**
     * 检测程序是否可执行文件
     *
     * @param cmd
     */
    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("checkExec 权限异常：" + cmd);
    }

    /**
     * 检测程序是否允许连接网络
     *
     * @param host
     * @param port
     */
    @Override
    public void checkConnect(String host, int port)
    {
        throw new SecurityException("checkConnect 权限异常：" + host + ":" + port);
    }


    /**
     * 读权限
     *
     * @param file    the system-dependent filename.
     * @param context a system-dependent security context.
     */
    @Override
    public void checkRead(String file, Object context) {
        // 默认空实现
    }

    @Override
    public void checkWrite(String file) {
        // 默认空实现
        throw new SecurityException("checkWrite 权限异常：" + file);
    }

    /**
     * 删除文件权限
     * @param file   the system-dependent filename.
     */
    @Override
    public void checkDelete(String file) {
        throw new SecurityException("checkDelete 权限异常：" + file);
    }
}
