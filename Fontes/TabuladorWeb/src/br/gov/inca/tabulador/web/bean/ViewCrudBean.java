package br.gov.inca.tabulador.web.bean;

import java.util.Collection;
import java.util.Collections;

import org.primefaces.model.LazyDataModel;

import br.gov.inca.tabulador.domain.db.DaoAbstract;
import br.gov.inca.tabulador.domain.db.DbResult;
import br.gov.inca.tabulador.domain.entity.Entidade;
import br.gov.inca.tabulador.web.LazyDataModelDbResult;

public abstract class ViewCrudBean<U extends DaoAbstract<T, K>, T extends Entidade<K>, K> implements ViewBean {
	private static final long serialVersionUID = -630850690048051284L;

	public static final String CONSULTAR = "consultar";

	private boolean cached;
	private Collection<T> entitiesCache;
	private DbResult<T> entities;
	private LazyDataModel<T> lazyEntities;

	/** @return DAO para acesso aos dados. */
	public abstract U getDao();
	/** Inicia o bean. */
	public abstract void init();
	/** @return Entidade utilizada na busca. */
	public abstract T getEntity();
	/** @param entity Entidade utilizada na busca.*/
	protected abstract void setEntity(T entity);

	public ViewCrudBean() {
		this(false);
	}

	/** @param cached Indica se deve fazer cache dos resultados. */
	public ViewCrudBean(boolean cached) {
		setCached(cached);
		setEntitiesCache(Collections.emptyList());
	}

	public String saveOrUpdate() {
		try {
			getDao().saveOrUpdate(getEntity());
			setEntities(null);
			return CONSULTAR;
		} catch (RuntimeException e) {
			showError(e, "Erro", "Problema ao salvar.");
			return null;
		}
	}

	public void findById(K id) {
		setEntity(getDao().findById(id));
		if (getEntity() == null) {
			showError("Erro", "Não foi encontrado nenhum registro para o identificador: " + id);
		}
	}

	public void remove(K id) {
		try {
			getDao().removeById(id);
			setEntities(null);
		} catch (RuntimeException e) {
			showError(e, "Erro", "Problema ao remover.");
		}
	}

	public Collection<T> getEntities() {
		updateEntities();
		if (!isCached()) {
			return entities.getCollection();
		} else if (getEntitiesCache().isEmpty()) {
			setEntitiesCache(getEntitiesDbResult().getCollection());
		}
		return getEntitiesCache();
	}

	public LazyDataModel<T> getEntitiesLazy() {
		if (updateEntities()) {
			setEntitiesLazy(new LazyDataModelDbResult<T>(getEntitiesDbResult()));
		}
		return this.lazyEntities;
	}

	protected void setEntities(DbResult<T> entities) {
		this.entities = entities;
	}

	public void findByExample() {
		setEntities(getDao().findByExample(getEntity()));
		setEntitiesCache(Collections.emptyList());
	}

	public boolean isCached() {
		return cached;
	}
	
	public void setCached(boolean cached) {
		this.cached = cached;
	}
	
	private boolean updateEntities() {
		if (getEntitiesDbResult() == null) {
			findByExample();
			if (getEntitiesDbResult().isEmpty()) {
				setEntities(getDao().findAll());
			}
			return true;
		}
		return false;
	}
	
	private  DbResult<T> getEntitiesDbResult() {
		return entities;
	}
	
	private void setEntitiesLazy(LazyDataModel<T> entities) {
		this.lazyEntities = entities;
	}
	
	private Collection<T> getEntitiesCache() {
		return entitiesCache;
	}
	
	private void setEntitiesCache(Collection<T> entitiesCache) {
		this.entitiesCache = entitiesCache;
	}
}