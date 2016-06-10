package br.gov.inca.tabulador.domain.dao.config;

import br.gov.inca.tabulador.domain.db.DaoAbstract;
import br.gov.inca.tabulador.domain.entity.config.CampoConfig;

public class CampoConfigDao extends DaoAbstract<CampoConfig, Integer> {
	private static final long serialVersionUID = 3651220214258945079L;
	
	@Override
	public CampoConfig saveOrUpdate(CampoConfig entity) {
		if (Boolean.FALSE.equals(entity.getFiltro()) || entity.getTipoFiltro().getId() == null) {
			entity.setTipoFiltro(null);
		}
		return super.saveOrUpdate(entity);
	}
}
