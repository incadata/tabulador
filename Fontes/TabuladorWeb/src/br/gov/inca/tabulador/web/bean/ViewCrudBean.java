package br.gov.inca.tabulador.web.bean;

import java.util.Collection;

import org.primefaces.model.LazyDataModel;

import br.gov.inca.tabulador.domain.db.DaoAbstract;
import br.gov.inca.tabulador.domain.db.DbResult;
import br.gov.inca.tabulador.domain.entity.Entidade;

public abstract class ViewCrudBean<U extends DaoAbstract<T, K>, T extends Entidade<K>, K> implements ViewBean {
	private DbResult<T> entities;
	private LazyDataModel<T> lazyEntities;

	public abstract U getDao();

	public abstract void init();

	public abstract T getEntity();

	protected abstract void setEntity(T entity);

	public String saveOrUpdate() {
		try {
			getDao().saveOrUpdate(getEntity());
			setEntities(null);
			return "consultar";
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
		if (entities == null) {
			findByExample();
			if (entities.isEmpty()) {
				setEntities(getDao().findAll());
			}
		}
		return entities.getCollection();
	}

	public LazyDataModel<T> getEntitiesLazy() {
		if (entities == null) {
			findByExample();
			if (entities.isEmpty()) {
				setEntities(getDao().findAll());
			}
			lazyEntities = new LazyDataModelDbResult<T>(entities);
		}
		return lazyEntities;
	}

	protected void setEntities(DbResult<T> entities) {
		this.entities = entities;
	}

	public void findByExample() {
		setEntities(getDao().findByExample(getEntity()));
	}

}