/**
 * 
 */
package com.harmeetsingh13.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Harmeet Singh(Taara)
 *
 */
@Configuration
@EnableWebMvc
@Import(value={ThymeleafConfig.class})
@ComponentScan(value={"com.harmeetsingh13.controllers"})
public class ApplicationContextConfig extends WebMvcConfigurerAdapter{

	// Maps resources path to webapp/assets
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
	}
	
	// Only needed if we are using @Value and ${...} when referencing properties
	/*@Bean
	public static PropertyPlaceholderConfigurer properties() {
		PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
		Resource[] resources = new ClassPathResource[]{new ClassPathResource("classpath:i18n/message.properties")};
		configurer.setLocations(resources);
		configurer.setIgnoreUnresolvablePlaceholders(true);
		return configurer;
	}*/
	
	// Provides internationalization of messages
	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasenames(new String[]{"i18n/message"});
		return source;
	}
}
