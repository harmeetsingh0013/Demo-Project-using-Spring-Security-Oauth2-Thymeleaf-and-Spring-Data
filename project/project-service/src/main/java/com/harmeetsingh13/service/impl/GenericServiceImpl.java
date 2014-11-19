/**
 * 
 */
package com.harmeetsingh13.service.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.core.GenericTypeResolver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.harmeetsingh13.service.GenericService;

/**
 * @author james
 *
 */
@Repository
@Transactional(readOnly = true)
@SuppressWarnings(value="unchecked")
public class GenericServiceImpl<T, R extends JpaRepository<T, Long>> implements GenericService<T>{

	@PersistenceContext
	private EntityManager entityManager;
	
	private Class<R>[] type;
	private Type genericType;
	private R repository;
	
	public GenericServiceImpl(){
		try{
			type = (Class<R>[]) GenericTypeResolver.resolveTypeArguments(getClass(), GenericServiceImpl.class);
			genericType = type[0];
			repository = type[1].newInstance();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	public T save(T t) {
		return repository.save((T)genericType);
	}

}
