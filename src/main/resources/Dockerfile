# 使用 openjdk:8-jdk-alpine 作为基础镜像
FROM openjdk:8-jdk-alpine

# 更换Alpine镜像源，加快软件包安装速度
RUN echo "http://mirrors.aliyun.com/alpine/v3.12/main" > /etc/apk/repositories && \
    echo "http://mirrors.aliyun.com/alpine/v3.12/community" >> /etc/apk/repositories && \
    apk update && \
    apk add --no-cache python3 gcc g++ musl-dev

# 定义构建参数、环境变量 JAVA_OPTS 和 PARAMS
ARG VERSION=""
ENV JAVA_OPTS=""
ENV PARAMS=""

# 将JAR文件复制到镜像中
COPY ./xinoj-code-sandbox-0.0.1-SNAPSHOT.jar /app.jar

# 定义容器启动时执行的入口点
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS $PARAMS -jar /app.jar $PARAMS"]