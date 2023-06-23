package br.edu.toledoprudente.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;



public abstract class AbstractDao<T, PK extends Serializable> {

	@SuppressWarnings("unchecked")
	private final Class<T> entityClass = 
			(Class<T>) ( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	
	@PersistenceContext
	protected EntityManager entityManager;

	protected EntityManager getEntityManager() {
		return entityManager;
	}
	
	@Transactional
	public void save(T entity) { 

		entityManager.persist(entity);
	}
	
	@Transactional
	public void update(T entity) {
		
		entityManager.merge(entity);
	}
	
	@Transactional
	public void delete(PK id) {
		
		entityManager.remove(entityManager.getReference(entityClass, id));
	}
	
	public T findById(PK id) {
		
		return entityManager.find(entityClass, id);
	}
	
	public List<T> findAll() {
		
		return entityManager
				.createQuery("from " + entityClass.getSimpleName(), entityClass)
				.getResultList();
	}	
	
	protected List<T> createQuery(String jpql, Object... params) {
		TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
		for (int i = 0; i < params.length; i++) {
		    query.setParameter(i+1, params[i]);
        }
    	return query.getResultList();
	}

}
