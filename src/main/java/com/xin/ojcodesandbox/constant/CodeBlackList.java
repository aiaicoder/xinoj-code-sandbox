package com.xin.ojcodesandbox.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author 15712
 */
public interface CodeBlackList {
    /**
     * Java代码黑名单
     * 黑名单检测通常用于辅助安全策略，而不是作为唯一的安全手段
     */
    List<String> SENSITIVE_WORD_LIST = Arrays.asList(
            // 文件操作相关
            "Files", "File", "FileInputStream", "FileOutputStream", "RandomAccessFile", "FileReader", "FileWriter", "FileChannel", "FileLock", "Path", "Paths", "File.createTempFile", "File.createTempDirectory", "ZipInputStream", "ZipOutputStream",

            // 网络相关
            "Socket", "ServerSocket", "DatagramSocket", "InetAddress", "URL", "URLConnection", "HttpURLConnection", "SocketChannel", "ServerSocketChannel", "DatagramChannel", "SocketPermission", "ServerSocketPermission",

            // 系统命令执行相关
            "exec", "Runtime.getRuntime().exec", "ProcessBuilder", "SecurityManager", "System.exit", "Runtime.getRuntime().halt", "SecurityManager.checkExec",

            // 反射相关
            "Class.forName", "Method.invoke", "sun.reflect.", "java.lang.reflect.", "Unsafe", "sun.misc.Unsafe", "sun.reflect.Unsafe", "Proxy",

            // 数据库相关
            "Statement", "PreparedStatement", "CallableStatement", "DataSource", "Connection", "ResultSet", "Hibernate", "JPA", // 防止使用 ORM 框架执行不安全的数据库操作
            "createStatement", "prepareStatement", "prepareCall",

            // 不安全的操作
            "Unsafe", "sun.misc.Unsafe", "sun.reflect.Unsafe",

            // 加密解密相关
            "Cipher", "MessageDigest", "KeyGenerator", "KeyPairGenerator", "SecretKeyFactory", "KeyStore", "SecureRandom", "java.security.",

            // 序列化相关
            "ObjectInputStream", "ObjectOutputStream", "Serializable", "Externalizable", "readObject", "writeObject",

            // 线程相关
            "Thread", "Runnable", "Executor", "ExecutorService", "ThreadPoolExecutor", "ThreadGroup", "ThreadLocal", "Thread.sleep", "Thread.yield", "Thread.stop", "Thread.suspend", "Thread.resume", "java.util.concurrent.",

            // 安全管理器相关
            "SecurityManager",

            // 其他可能导致安全问题的操作
            "System.load", "System.loadLibrary", // 防止加载本地库
            "JNI", "Java Native Interface", // 防止使用 JNI 调用本地代码
            "Unsafe.allocateMemory", "Unsafe.freeMemory", // 直接内存操作
            "System.getProperties", "System.setProperty", // 系统属性操作
            "System.getenv", // 获取环境变量
            "System.console", // 控制台访问
            "Runtime.addShutdownHook", // 添加关闭钩子
            "Runtime.load", "Runtime.loadLibrary" // 加载本地库
    );

    /**
     * Python代码黑名单
     * 黑名单检测通常用于辅助安全策略，而不是作为唯一的安全手段
     */
    List<String> PYTHON_BLACK_LIST = Arrays.asList(
            // 文件操作相关
            "open", "os.system", "os.popen", "os.fdopen", "shutil.copy", "shutil.move", "shutil.rmtree",

            // 网络相关
            "socket", "http.client.HTTPConnection", "http.client.HTTPSConnection", "urllib.request.urlopen", "urllib.request.urlretrieve",

            // 系统命令执行相关
            "subprocess.run", "subprocess.Popen",

            // 反射相关
            "__import__", "eval", "exec",

            // 数据库相关
            "sqlite3", "MySQLdb",

            // 加密解密相关
            "cryptography",

            // 序列化相关
            "pickle",

            // 线程相关
            "threading.Thread", "multiprocessing.Process",

            // 安全管理器相关
            "java.lang.SecurityManager",

            // 其他可能导致安全问题的操作
            "ctypes.CDLL", "ctypes.WinDLL", "ctypes.CFUNCTYPE", "os.environ", "os.putenv", "atexit.register",

            // 与操作系统交互
            "os.chmod", "os.chown",

            // 文件权限控制
            "os.access", "os.setuid", "os.setgid",

            // 环境变量操作
            "os.environ['SOME_VAR']", "os.putenv('SOME_VAR', 'value')",

            // 不安全的输入
            "input", "raw_input",

            // 不安全的字符串拼接
            "eval(f'expr {var}')", "var = var1 + var2",

            // 定时器相关
            "time.sleep",

            // 定时任务
            "schedule",

            // 本地文件包含
            "exec(open('filename').read())",

            // 不安全的网站访问
            "urllib.urlopen",

            // 系统退出
            "exit",

            // 其他危险操作
            "os.remove", "os.unlink", "os.rmdir", "os.removedirs", "os.rename", "os.execvp", "os.execlp",

            // 不安全的随机数生成
            "random",

            // 不安全的正则表达式
            "re.compile",

            // 使用 eval 解析 JSON
            "eval(json_string)",

            // 使用 pickle 处理不受信任的数据
            "pickle.loads",

            // 使用 exec 执行不受信任的代码
            "exec(code)",

            // 不安全的 HTML 解析
            "BeautifulSoup",

            // 不安全的 XML 解析
            "xml.etree.ElementTree",

            // 使用自定义反序列化
            "pickle.Unpickler", "marshal.loads",

            // 在代码中直接拼接 SQL
            "sqlalchemy.text",

            // 不安全的图像处理
            "PIL.Image",

            // 使用 ctypes 执行外部 C 代码
            "ctypes.CDLL",

            // 不安全的邮件操作
            "smtplib", "poplib",

            // 不安全的 URL 拼接
            "urllib.parse.urljoin",

            // 使用 eval 执行 JavaScript 代码
            "execjs.eval",

            // 不安全的 Web 框架设置
            "Flask.app.secret_key",

            // 不安全的 API 请求
            "requests.get", "requests.post",

            // 不安全的模板引擎
            "Jinja2.Template",

            // 不安全的数据反序列化
            "pickle.loads", "marshal.loads",

            // 不安全的文件上传
            "werkzeug.FileStorage",

            // 不安全的命令行参数解析
            "argparse.ArgumentParser");

    /**
     * C语言相关敏感词
     */
    List<String> C_SENSITIVE_WORD_LIST = Arrays.asList(
            // 文件操作相关
            "fopen", "fclose", "fread", "fwrite", "fscanf", "fprintf", "fseek", "ftell", "rewind", "remove", "rename", "tmpfile", "mkstemp",

            // 网络相关
            "socket", "bind", "connect", "listen", "accept", "send", "recv", "setsockopt", "getsockopt", "inet_addr", "htons", "htonl", "ntohs", "ntohl",

            // 系统命令执行相关
            "system", "execvp", "execv", "execl", "execlp", "popen", "pclose",

            // 数据库相关
            // C语言中通常通过库如 MySQL C API, SQLite等实现数据库操作，以下为一些示例函数
            "mysql_query", "sqlite3_exec",

            // 加密解密相关
            // C语言使用 libcrypto (OpenSSL)等库进行加密解密操作
            "EVP_EncryptInit", "EVP_DecryptInit",
            // 线程相关
            "pthread_create", "pthread_join", "pthread_detach", "pthread_exit",
            // 其他可能导致安全问题的操作
            "dlopen", "dlsym", // 动态加载库
            "getenv", // 环境变量操作
            "atexit", // 添加退出时执行的函数
            "mmap", "munmap", // 直接内存映射操作
            "system" // 执行外部命令
    );
}
