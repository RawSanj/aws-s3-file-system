FROM java:8
VOLUME /tmp
ADD aws-s3-file-system-0.1.0.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Deureka.datacenter=cloud -jar /app.jar" ]