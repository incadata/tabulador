package br.gov.inca.tabulador.web.bean;

import java.util.Collection;

import br.gov.inca.tabulador.domain.dao.DaoAbstractGenericId;

public abstract class ViewBean<U extends DaoAbstractGenericId<T, K>, T, K> {
	private Collection<T> entities;

	public abstract U getDao();
	public abstract T getEntity();
	public abstract void init();

	public void persist() {
		getDao().persist(getEntity());
		init();
	}

	public Collection<T> getEntities() {
		if (entities == null) {
			setEntities(getDao().findAll());
		}
		return entities;
	}
	
	protected void setEntities(Collection<T> entities) {
		this.entities = entities;
	}

	public void findByExample() {
		setEntities(getDao().findByExample(getEntity()));
	}
}