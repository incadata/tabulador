package br.gov.inca.tabulador.domain.db;

import java.io.Serializable;
import java.util.Optional;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;

import br.gov.inca.tabulador.domain.entity.Entidade;
import br.gov.inca.tabulador.util.ReflectUtil;

public abstract class DaoAbstract<T extends Entidade<K>, K> implements Serializable {
	private static final long serialVersionUID = -1872260145488857097L;

	private @Inject Instance<EntityManager> entityManager;
	private Class<?> genericType;

	@Transactional
	public void persist(T entity) {
		getEntityManager().persist(entity);
	}

	@Transactional
	public T merge(T entity) {
		return getEntityManager().merge(entity);
	}

	@Transactional
	public void remove(T entity) {
		getEntityManager().remove(entity);
	}

	@Transactional
	public T saveOrUpdate(T entity) {
		final EntityManager em = getEntityManager();
		em.persist(em.contains(entity) ? entity : em.merge(entity));
		return entity;
	}

	@Transactional
	public void removeById(K id) {
		findById(id).ifPresent(x -> remove(x));
	}

	protected EntityManager getEntityManager() {
		return entityManager.get();
	}

	protected Class<?> getGenericType() {
		if (genericType == null) {
			Class<?> clazz = this.getClass();
			while (!clazz.getSuperclass().equals(DaoAbstract.class) && !clazz.getSuperclass().equals(Object.class)) {
				clazz = clazz.getSuperclass();
			}
			genericType = ReflectUtil.getGenericType(clazz);
		}
		return genericType;
	}

	public DbResult<T> findAll() {
        return new DetachedCriteriaDbResult<>(entityManager, createCriteria());
	}

	public DbResult<T> findByExample(T entity) {
		return new DetachedCriteriaDbResult<>(entityManager, createCriteria().add(Example.create(entity).enableLike(MatchMode.ANYWHERE).excludeNone().ignoreCase()));
	}
	
	protected DetachedCriteria createCriteria() {
		return DetachedCriteria.forClass(getGenericType());
	}

	@SuppressWarnings("unchecked")
	public Optional<T> findById(K id) {
		return Optional.ofNullable((T) getEntityManager().find(getGenericType(), id));
	}
	
}
