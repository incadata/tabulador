package br.gov.inca.tabulador.domain.db;

import java.util.List;

import javax.enterprise.inject.Instance;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;

public class DetachedCriteriaDbResult<T> extends DbResult<T> implements Cloneable {
	private static final long serialVersionUID = 4721281387868341339L;

	private Instance<EntityManager> entityManager;
	private DetachedCriteria detachedCriteria;

	public DetachedCriteriaDbResult(Instance<EntityManager> entityManager, DetachedCriteria criteria) {
		this.entityManager = entityManager;
		setCriteria(criteria);
	}

	@Override
	public long getCount() {
		Criteria criteria = getCriteria();
		final long longValue = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).longValue();
		criteria = criteria.setProjection(null);
		criteria = criteria.setResultTransformer(Criteria.ROOT_ENTITY);
		return longValue;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getList() {
		Criteria criteria = getCriteria();
		if (getFirstResult() != null) {
			criteria = criteria.setFirstResult(getFirstResult());
		}
		if (getMaxResults() != null) {
			criteria = criteria.setMaxResults(getMaxResults());
		}
		return criteria.list();
	}

	public DetachedCriteria getDetachedCriteria() {
		return detachedCriteria;
	}

	public Criteria getCriteria() {
		return getDetachedCriteria().getExecutableCriteria((Session) entityManager.get().getDelegate());
	}

	public void setCriteria(DetachedCriteria criteria) {
		this.detachedCriteria = criteria;
	}

}
