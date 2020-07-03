FROM java:8
VOLUME /tmp
ADD jeecg-boot-module-system/target/jeecg-boot-module-system-2.1.4.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
