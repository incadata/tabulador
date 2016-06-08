package br.gov.inca.tabulador.domain.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public abstract class DbResult<T> implements Serializable {
	private static final long serialVersionUID = 3684552726532906079L;

	private Integer firstResult = 0;
	private Integer maxResults = 10;

	/**
	 * @return Quantidade de registros na busca.
	 */
	public abstract long getCount();

	public abstract List<T> getList();

	public Collection<T> getCollection() {
		return getList();
	}

	public Integer getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}
	
	public boolean isEmpty() {
		return getCount() == 0L;
	}
}
