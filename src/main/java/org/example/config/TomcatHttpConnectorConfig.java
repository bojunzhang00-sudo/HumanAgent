package org.example.config;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 在 HTTPS 主端口之外，再额外开一个 HTTP 端口。
 * 主端口（server.port）由 application.yml 配置，目前是 9443 HTTPS。
 * 这里追加 9900 HTTP 端口，方便内网调试 / 老客户端兼容。
 *
 * 注意：HTTP 端口同样会暴露所有 API，生产环境请只在内网开放或加鉴权。
 */
@Configuration
public class TomcatHttpConnectorConfig {

    @Value("${server.http.port:9900}")
    private int httpPort;

    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(httpConnector());
        return tomcat;
    }

    private Connector httpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(httpPort);
        connector.setSecure(false);
        return connector;
    }
}
