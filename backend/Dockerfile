FROM quay.io/wildfly/wildfly:32.0.1.Final-jdk21
COPY target/ipa-undanganku-backend-1.0.0.war /opt/jboss/wildfly/standalone/deployments/ipa-undanganku.war
EXPOSE 8080
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
