package com.sln.bshop.config;

/*
 * This will make Tomcat start on 2 ports: 8080 and 8443
 * Not using it because I'm not sure if Heroku allows using 2 ports
 * (Btw, static content is not accessible with this config - still need to sort this out; maybe check: https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-web-applications.html#boot-features-spring-mvc-static-content)    
 * 
 * Generated self-signed SSL keystore.p12 using these instructions: https://drissamri.be/blog/java/enable-https-in-spring-boot/
 * The config in this file is according to this: https://docs.spring.io/spring-boot/docs/current/reference/html/howto-embedded-servlet-containers.html#howto-enable-multiple-connectors-in-tomcat
 * This config is in separate config file because puting it into SecurityConfig.java produces 
 * 		org.springframework.beans.factory.BeanCreationException: A ServletContext is required to configure default servlet handling
 * see: https://stackoverflow.com/questions/33832792/a-servletcontext-is-required-to-configure-default-servlet-handling-error-when
 */

/*
import java.io.File;
import java.io.IOException;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
//	@Override
//	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//		configurer.enable();
//	}
	
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
	    TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
	    tomcat.addAdditionalTomcatConnectors(createSslConnector());
	    return tomcat;
	}

	private Connector createSslConnector() {
	    Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
	    Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
	    try {
	        File keystore = new ClassPathResource("keystore.p12").getFile();
	        //File truststore = new ClassPathResource("keystore.p12").getFile();
	        connector.setScheme("https");
	        connector.setSecure(true);
	        connector.setPort(8443);
	        protocol.setSSLEnabled(true);
	        protocol.setKeystoreFile(keystore.getAbsolutePath());
	        protocol.setKeystorePass("aaaaaa");
	        //protocol.setTruststoreFile(truststore.getAbsolutePath());
	        //protocol.setTruststorePass("aaaaaa");
	        protocol.setKeyAlias("tomcat");
	        return connector;
	    }
	    catch (IOException ex) {
	        throw new IllegalStateException("can't access keystore: [" + "keystore"
	                + "] or truststore: [" + "keystore" + "]", ex);
	    }
	}	
	
}
*/
