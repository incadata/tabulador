package br.gov.inca.tabulador.domain.dao.config;

import java.util.Collection;

import javax.inject.Inject;
import javax.transaction.Transactional;

import br.gov.inca.tabulador.domain.db.DaoAbstract;
import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;

public class TabelaConfigDao extends DaoAbstract<TabelaConfig, Integer> {
	private static final long serialVersionUID = -1356263787253270743L;

	private @Inject CampoConfigDao campoConfigDao;

	@Override
	@Transactional
	public TabelaConfig saveOrUpdate(TabelaConfig entity) {
		setRelationship(entity, entity.getCampos());
		return super.saveOrUpdate(entity);
	}

	protected void setRelationship(TabelaConfig entity, Collection<CampoConfig> campos) {
		for (CampoConfig campo : campos) {
			campo.setTabelaConfig(entity);
			getCampoConfigDao().setRelationship(campo, campo.getValores());
		}
	}
	
	protected CampoConfigDao getCampoConfigDao() {
		return campoConfigDao;
	}
}
