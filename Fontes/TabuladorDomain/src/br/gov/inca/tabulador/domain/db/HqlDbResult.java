package br.gov.inca.tabulador.domain.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class HqlDbResult<T> extends DbResult<T> implements Cloneable {
	private static final long serialVersionUID = 4721281387868341339L;

	private Instance<EntityManager> entityManager;
	private String hqlCommand;
	private Map<String, Object> parameters;

	public HqlDbResult(Instance<EntityManager> entityManager, String hqlCommand) {
		this(entityManager, hqlCommand, new HashMap<>());
	}

	public HqlDbResult(Instance<EntityManager> entityManager, String hqlCommand, Map<String, Object> parameters) {
		setEntityManager(entityManager);
		setHqlCommand(hqlCommand);
		setParameters(parameters);
	}

	@Override
	public long getCount() {
		final Query query = getEntityManager().createQuery(String.format("select count(*) from (%s)", getHqlCommand()));
		setQueryParameters(query);
		return (Long) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getList() {
		Query query = getEntityManager().createQuery(getHqlCommand());
		setQueryParameters(query);

		if (getFirstResult() != null) {
			query = query.setFirstResult(getFirstResult());
		}
		if (getMaxResults() != null) {
			query = query.setMaxResults(getMaxResults());
		}
		return query.getResultList();
	}

	private EntityManager getEntityManager() {
		return entityManager.get();
	}

	private void setEntityManager(Instance<EntityManager> entityManager) {
		this.entityManager = entityManager;
	}

	public String getHqlCommand() {
		return hqlCommand;
	}

	private void setHqlCommand(String hqlCommand) {
		this.hqlCommand = hqlCommand;
	}

	private Map<String, Object> getParameters() {
		return parameters;
	}

	private void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	private void setQueryParameters(Query query) {
		for (Map.Entry<String, Object> entry : getParameters().entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
	}

	public Object putParameter(String key, Object value) {
		return getParameters().put(key, value);
	}

	public Object putParameter(String key) {
		return getParameters().remove(key);
	}
}
