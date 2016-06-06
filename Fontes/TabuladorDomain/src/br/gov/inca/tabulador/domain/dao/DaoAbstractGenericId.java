package br.gov.inca.tabulador.domain.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.criterion.Example;

import br.gov.inca.tabulador.util.ReflectUtil;

public abstract class DaoAbstractGenericId<T, K> {
	@PersistenceContext
	private EntityManager entityManager;
	private Class<?> genericType;

	public void persist(T entity) {
		getEntityManager().persist(entity);
	}

	public T merge(T entity) {
		return getEntityManager().merge(entity);
	}

	public void remove(T entity) {
		getEntityManager().remove(entity);
	}

	protected EntityManager getEntityManager() {
		return entityManager;
	}

	protected Class<?> getGenericType() {
		if (genericType == null) {
			genericType = ReflectUtil.getGenericType(this.getClass());
		}
		return genericType;
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return (List<T>) getEntityManager().createQuery(
				String.format("SELECT g FROM %s g", getGenericType().getSimpleName())).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByExample(T entity) {
		final Example example = Example.create(entity);
		return ((Session) getEntityManager().getDelegate())
				.createCriteria(getGenericType()).add(example).list();
	}

	@SuppressWarnings("unchecked")
	public T findById(K id) {
		return (T) getEntityManager().find(getGenericType(), id);
	}
}
