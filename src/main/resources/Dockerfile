# 使用 openjdk 镜像的 8-jdk 版本作为基础镜像
FROM openjdk:8-jdk

# 在镜像中运行命令，更新软件包列表并安装 Python3
RUN apt-get update && apt-get install -y python3

# 定义一个构建参数 VERSION（未使用）、环境变量 JAVA_OPTS 和 PARAMS
ARG VERSION=""
ENV JAVA_OPTS=""
ENV PARAMS=""

# 设置容器的时区为 Asia/Shanghai，并将其复制到 /etc/localtime 文件，同时设置时区信息到 /etc/timezone 文件
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

# 将当前目录下的 sspuoj-code-sandbox-0.0.1-SNAPSHOT.jar 文件复制到容器的根目录，并命名为 app.jar
ADD ./xinoj-code-sandbox-0.0.1-SNAPSHOT.jar /app.jar

# 定义容器启动时执行的入口点，以执行 Java 命令来运行应用程序
ENTRYPOINT ["sh","-c","java $JAVA_OPTS $PARAMS -jar /app.jar $PARAMS"]
