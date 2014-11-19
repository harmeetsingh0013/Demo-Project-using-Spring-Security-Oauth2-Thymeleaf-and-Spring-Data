/**
 * 
 */
package com.harmeetsingh13.passwordgen;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Harmeet Singh(Taara)
 *
 */
public class BcryptPasswordGen {

	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);
		System.out.println(encoder.encode("12345678"));
	}
}
