/**
 * 
 */
package com.harmeetsingh13.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.harmeetsingh13.entities.User;

/**
 * @author Harmeet Singh(Taara)
 *
 */
@Transactional(readOnly = true) 
public interface RepoUser extends JpaRepository<User, Long>{

}
