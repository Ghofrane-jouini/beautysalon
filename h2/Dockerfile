FROM eclipse-temurin:21-jre-alpine

# Doit etre EXACTEMENT la meme version que le client H2 de l'application
ARG H2_VERSION=2.5.252

# Utilisateur non root + dossier de donnees + telechargement du jar H2
RUN addgroup -S h2 && adduser -S -G h2 h2 \
    && mkdir -p /data /opt/h2 \
    && wget -q -O /opt/h2/h2.jar \
       "https://repo1.maven.org/maven2/com/h2database/h2/${H2_VERSION}/h2-${H2_VERSION}.jar" \
    && chown -R h2:h2 /data

USER h2
WORKDIR /data
EXPOSE 9092

CMD ["java", "-cp", "/opt/h2/h2.jar", "org.h2.tools.Server", \
     "-tcp", "-tcpPort", "9092", "-tcpAllowOthers", \
     "-ifNotExists", "-baseDir", "/data"]