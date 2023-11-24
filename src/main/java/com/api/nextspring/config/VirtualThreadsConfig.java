package com.api.nextspring.config;

import java.util.concurrent.Executors;

import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that provides a bean to configure Tomcat to use virtual
 * threads.
 * 
 * @see TomcatProtocolHandlerCustomizer
 * @see Executors#newVirtualThreadPerTaskExecutor()
 * 
 * @author Athirson Silva
 */
@Configuration
public class VirtualThreadsConfig {

	/**
	 * Bean that configures Tomcat to use virtual threads.
	 *
	 * @return A {@link TomcatProtocolHandlerCustomizer} that configures Tomcat to
	 *         use virtual threads.
	 */
	@Bean
	TomcatProtocolHandlerCustomizer<?> tomcatProtocolHandlerCustomizer() {
		return protocolHandler -> protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

	}
}
