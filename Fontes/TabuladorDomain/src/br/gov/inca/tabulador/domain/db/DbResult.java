package br.gov.inca.tabulador.domain.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public abstract class DbResult<T> implements Serializable {
	private static final long serialVersionUID = 3684552726532906079L;

	private Integer firstResult = 0;
	private Integer maxResults = 10;

	/** @return Quantidade de registros na consulta. */
	public abstract long getCount();
	
	/** @return Resultado na forma de List. */
	public abstract List<T> getList();

	/** @return Resultado na forma de Collection. */
	public Collection<T> getCollection() {
		return getList();
	}

	/** @return Qual será o primeiro registro retornado. */
	public Integer getFirstResult() {
		return firstResult;
	}

	/** @param firstResult Qual será o primeiro registro retornado. */
	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}

	/** @return Quantos registros devem ser buscados. */
	public Integer getMaxResults() {
		return maxResults;
	}

	/** @param maxResults Quantos registros devem ser buscados. */
	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}
	
	/** @return Indica se a consulta não retorna resultados. */
	public boolean isEmpty() {
		return getCount() == 0L;
	}
	
	/**
	 * Para consultas não ordenadas esse resultado pode vairar.
	 * @return Primeiro registro do resultado da consulta
	 */
	public T getFirst() {
		final Integer firstResultTemp = getFirstResult();
		setFirstResult(0);
		final T first = getUniqueResult();
		setFirstResult(firstResultTemp);
		return first;
	}

	/**
	 * Para consultas não ordenadas esse resultado pode vairar.
	 * @return Último registro do resultado da consulta
	 */
	public T getLast() {
		final Integer firstResultTemp = getFirstResult();
		setFirstResult(new Long(getCount() - 1).intValue());
		final T last = getUniqueResult();
		setFirstResult(firstResultTemp);
		return last;
	}
	
	/** @return Consiera que a pesquisa só tem um único resultado. */
	public T getUniqueResult() {
		final Integer maxResultsTemp = getMaxResults();
		setMaxResults(new Long(getCount() - 1).intValue());
		final List<T> resultList = getList();
		setMaxResults(maxResultsTemp);
		return resultList.isEmpty() ? null : resultList.get(0);
	}
}
