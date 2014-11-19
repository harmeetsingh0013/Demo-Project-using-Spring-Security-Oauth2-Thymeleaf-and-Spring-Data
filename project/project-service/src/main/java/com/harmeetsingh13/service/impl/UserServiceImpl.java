/**
 * 
 */
package com.harmeetsingh13.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.harmeetsingh13.entities.User;
import com.harmeetsingh13.repo.RepoUser;
import com.harmeetsingh13.service.UserService;

/**
 * @author james
 *
 */
@Repository
@Transactional(readOnly = true)
public class UserServiceImpl extends GenericServiceImpl<User, RepoUser> implements UserService{

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private RepoUser repoUser;
}
