/**
 * 
 */
package com.harmeetsingh13.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Harmeet Singh(Taara)
 *
 */
@Controller
public class UserController {

	@RequestMapping("/")
	public String loginPage(HttpServletRequest httpServletRequest) {
		System.out.println("******************Hello World");
		return "user/login";
	}
	
	@RequestMapping("/user/home")
	public void userHome() {
		System.out.println("User HoME URL");
		
	}
	
	@RequestMapping(value="/api/resp", method=RequestMethod.POST)
	public @ResponseBody String getApiRespnse() {
		System.out.println("API respons");
		return "API response";
	}
}
