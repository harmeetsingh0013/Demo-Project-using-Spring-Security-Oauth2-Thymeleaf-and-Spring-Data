/**
 * 
 */
package com.harmeetsingh13.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * @author Harmeet Singh(Taara)
 *
 */
@Order(2)
public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer{

}
