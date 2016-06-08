package br.gov.inca.tabulador.web.bean;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.gov.inca.tabulador.domain.db.DbResult;

public class LazyDataModelDbResult<T> extends LazyDataModel<T> {
	private static final long serialVersionUID = -6802182641042091200L;
	private DbResult<T> dbResult;

	public LazyDataModelDbResult(DbResult<T> dbResult) {
		setDbResult(dbResult);
	}
	
	@Override
	public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
		getDbResult().setFirstResult(first);
		getDbResult().setMaxResults(pageSize);
		setRowCount((int) getDbResult().getCount()); 
		return getDbResult().getList();
	}
	
	public DbResult<T> getDbResult() {
		return dbResult;
	}
	
	public void setDbResult(DbResult<T> dbResult) {
		this.dbResult = dbResult;
	}
	
	@Override
	public void setPageSize(int pageSize) {
		super.setPageSize(pageSize);
		getDbResult().setMaxResults(pageSize);
	}
	
	@Override
	public void setRowIndex(int rowIndex) {
		super.setRowIndex(rowIndex);
		getDbResult().setFirstResult(rowIndex);
	}
}
