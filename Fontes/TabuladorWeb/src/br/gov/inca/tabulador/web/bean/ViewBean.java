package br.gov.inca.tabulador.web.bean;

import java.util.Collection;

import br.gov.inca.tabulador.domain.dao.DaoAbstractGenericId;

public abstract class ViewBean<U extends DaoAbstractGenericId<T, K>, T, K> {
	public abstract U getDao();

	public abstract T getEntity();

	public abstract void init();

	public void persist() {
		getDao().persist(getEntity());
		init();
	}

	public Collection<T> getEntities() {
		return getDao().findAll();
	}

	public Collection<T> findByExample() {
		return getDao().findByExample(getEntity());
	}
}