/**
 * 
 */
package com.harmeetsingh13.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author Harmeet Singh(Taara)
 *
 */
@Order(1)
@Configuration
@EnableAuthorizationServer
public class APIOauth2SpringSecurityConfig extends AuthorizationServerConfigurerAdapter{

	@Autowired
	private DataSource dataSource;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Bean
	@Autowired
	public ClientDetailsService clientDetailService() {
		JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
		return clientDetailsService;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(11);
	}
	
	@Bean
	@Autowired
	public UserDetailsService userDetailService(ClientDetailsService clientDetailsService) {
		ClientDetailsUserDetailsService userDetailsService =  new ClientDetailsUserDetailsService(clientDetailsService);
		//userDetailsService.setPasswordEncoder(passwordEncoder);
		return userDetailsService;
	}
	
	
	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	}
	
	@Bean
	@Autowired
	public DefaultTokenServices tokenService(TokenStore tokenStore, ClientDetailsService clientDetailsService) {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(tokenStore);
		tokenServices.setClientDetailsService(clientDetailsService);
		return tokenServices;
	}
	
	@Bean
	@Autowired
	public DefaultOAuth2RequestFactory oAuth2RequestFactory(ClientDetailsService clientDetailsService) {
		DefaultOAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);
		return requestFactory;
	}
	
	@Bean
	@Autowired
	public TokenStoreUserApprovalHandler userApprovalHandler(DefaultOAuth2RequestFactory requestFactory, TokenStore tokenStore) {
		TokenStoreUserApprovalHandler userApprovalHandler = new TokenStoreUserApprovalHandler();
		userApprovalHandler.setRequestFactory(requestFactory);
		userApprovalHandler.setTokenStore(tokenStore);
		return userApprovalHandler;
	}
	
	@Bean(name="clientAuthenticationEntryPoint")
	public OAuth2AuthenticationEntryPoint clientAuthenticationEntryPoint() {
		OAuth2AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
		entryPoint.setRealmName("Authorization/client");
		entryPoint.setTypeName("Basic");
		return entryPoint;
	}
	
	@Bean(name="oauthAuthenticationEntryPoint")
	public OAuth2AuthenticationEntryPoint oauthAuthenticationEntryPoint() {
		OAuth2AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
		entryPoint.setTypeName("Bearer");
		return entryPoint;
	}
	
	@Bean
	public OAuth2AccessDeniedHandler oauthAccessDeniedHandler() {
		OAuth2AccessDeniedHandler accessDeniedHandler = new OAuth2AccessDeniedHandler();
		return accessDeniedHandler;
	}

	/*@Bean
	@Autowired
	public AuthenticationManager getAuthenticationManager(ClientDetailsService clientDetailsService,DefaultTokenServices tokenServices) {
		OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager();
		authenticationManager.setClientDetailsService(clientDetailsService);
		authenticationManager.setTokenServices(tokenServices);
		return authenticationManager;
		
	}*/
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients)
			throws Exception {
		clients.jdbc(dataSource);
		clients.withClientDetails(clientDetailService());
	}
	
	//Oauth Have different ways for check user credentials like client_cridentials, password etc. This is for client_cridentials
	@Bean
	@Autowired
	public ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter(AuthenticationManager authenticationManager) {
		ClientCredentialsTokenEndpointFilter accessTokenProvider = new ClientCredentialsTokenEndpointFilter();
		accessTokenProvider.setAuthenticationManager(authenticationManager);
		return accessTokenProvider;
	}
	
	@Bean
	public OAuth2ProtectedResourceDetails protectedResources() {
		OAuth2ProtectedResourceDetails protectedResourceDetails = new ClientCredentialsResourceDetails();
		return protectedResourceDetails;
	}
	
	@Order(2)
	@Configuration
	@EnableAuthorizationServer
	public static class AutorizationEndPoints extends AuthorizationServerConfigurerAdapter{
		@Autowired
		private ClientDetailsService clientDetailService;
		@Autowired 
		private DefaultOAuth2RequestFactory requestFactory;
		@Autowired
		private UserApprovalHandler userApprovalHandler;
		@Autowired
		private DefaultTokenServices tokenService;
		@Autowired
		private AuthenticationManager authenticationManager;
		@Autowired
		private DataSource dataSource;
		
		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endPoints)
				throws Exception {
			endPoints.clientDetailsService(clientDetailService);
			endPoints.requestFactory(requestFactory);
			endPoints.userApprovalHandler(userApprovalHandler);
			endPoints.tokenServices(tokenService);
			endPoints.authenticationManager(authenticationManager);
		}
	}
	
	@Order(3)
	@Configuration
	@EnableResourceServer
	public static class ResurceServerConfig extends ResourceServerConfigurerAdapter{
		
		@Autowired
		private UserDetailsService userDetailsService;
		@Autowired
		private DefaultTokenServices tokenServices;
		@Resource
		private OAuth2AuthenticationEntryPoint oauthAuthenticationEntryPoint;
		@Autowired
		private OAuth2AccessDeniedHandler oauthAccessDeniedHandler;
		
		@Override
		public void configure(ResourceServerSecurityConfigurer resources)
				throws Exception {
			resources.resourceId("rsourceId");
			resources.tokenServices(tokenServices);
		}
		
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
				.anyRequest().authenticated();
		}
		/*We can also secure URL from resource http configuration like
		 * 
		 *http.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/api/**", "/user/**").hasRole("USER");
		 */
	}
	
	@Order(4)
	@Configuration
	@EnableWebMvcSecurity
	public static class OauthWebSecurityConfigureAdapter extends WebSecurityConfigurerAdapter{
		
		@Autowired
		private UserDetailsService userDetailsService;
		@Resource
		private OAuth2AuthenticationEntryPoint clientAuthenticationEntryPoint;
		@Autowired
		private ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter;
		@Autowired
		private OAuth2AccessDeniedHandler oauthAccessDeniedHandler;
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.userDetailsService(userDetailsService)
				.authorizeRequests()
				.antMatchers("/oauth/token").permitAll().anyRequest()
				.fullyAuthenticated()
			.and()
				.httpBasic()
				.authenticationEntryPoint(clientAuthenticationEntryPoint)
				.and()
				.addFilterBefore(clientCredentialsTokenEndpointFilter, BasicAuthenticationFilter.class)
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.exceptionHandling().accessDeniedHandler(oauthAccessDeniedHandler);
		}
	}
	
	@Order(5)
	@Configuration
	@EnableWebMvcSecurity
	public static class urlPatternSecurityConfigureAdapter extends WebSecurityConfigurerAdapter{
		
		@Autowired
		private UserDetailsService userDetailsService;
		@Autowired
		private DefaultTokenServices tokenServices;
		@Resource
		private OAuth2AuthenticationEntryPoint oauthAuthenticationEntryPoint;
		@Autowired
		private OAuth2AccessDeniedHandler oauthAccessDeniedHandler;
		
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.userDetailsService(userDetailsService)
				.anonymous().disable()
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/api/**").hasRole("USER")
				.antMatchers("/user/**").hasRole("USER")
				.antMatchers("/admin/**").hasRole("ADMIN")
				.and()
				.httpBasic()
				.authenticationEntryPoint(oauthAuthenticationEntryPoint)
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.exceptionHandling().accessDeniedHandler(oauthAccessDeniedHandler);
				
		}
	}
	
	@Order(6)
	@Configuration
	@EnableWebMvcSecurity
	public static class WebSecurityConfig extends WebSecurityConfigurerAdapter{
		
		@Autowired
		private UserDetailsService userDetailsService;
		@Autowired
		private PasswordEncoder passwordEncoder;
		
		@Override
		@Bean(name = "authenticationManager")
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth)
				throws Exception {
			auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
		}
	}
}
