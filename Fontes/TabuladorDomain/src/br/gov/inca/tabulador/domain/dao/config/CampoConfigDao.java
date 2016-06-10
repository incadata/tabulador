package br.gov.inca.tabulador.domain.dao.config;

import java.util.Collection;

import javax.inject.Inject;

import br.gov.inca.tabulador.domain.cdi.qualifier.Transactional;
import br.gov.inca.tabulador.domain.db.DaoAbstract;
import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.ValorCampoConfig;

public class CampoConfigDao extends DaoAbstract<CampoConfig, Integer> {
	private static final long serialVersionUID = 3651220214258945079L;
	private @Inject ValorCampoConfigDao valorCampoConfigDao;
	
	@Override
	@Transactional
	public CampoConfig saveOrUpdate(CampoConfig entity) {
		if (entity.getTipoFiltro() != null && (Boolean.FALSE.equals(entity.getFiltro()) || entity.getTipoFiltro().getId() == null)) {
			entity.setTipoFiltro(null);
		}
		final CampoConfig resultado = super.saveOrUpdate(entity);
		saveOrUpdate(entity, entity.getValores());
		return resultado;
	}
	
	protected void saveOrUpdate(CampoConfig entity, Collection<ValorCampoConfig> valores) {
		for (ValorCampoConfig valor : valores) {
			valor.setCampoConfig(entity);
			valorCampoConfigDao.saveOrUpdate(valor);
		}
	}
}
