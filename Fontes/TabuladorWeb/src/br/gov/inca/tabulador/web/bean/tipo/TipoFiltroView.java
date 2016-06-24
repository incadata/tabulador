package br.gov.inca.tabulador.web.bean.tipo;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.inca.tabulador.domain.dao.tipo.TipoFiltroDao;
import br.gov.inca.tabulador.domain.entity.tipo.TipoFiltro;
import br.gov.inca.tabulador.web.bean.ViewCrudBean;

@Named
@ViewScoped
public class TipoFiltroView extends
		ViewCrudBean<TipoFiltroDao, TipoFiltro, Integer> implements
		Serializable {
	private static final long serialVersionUID = -6539486572898169197L;

	private TipoFiltro tipoFiltro;
	private @Inject TipoFiltroDao tipoFiltroDao;

	public TipoFiltroView() {
		super();
	}

	@Override
	@PostConstruct
	public void init() {
		setEntities(null);
		setEntity(new TipoFiltro());
	}

	@Override
	public TipoFiltroDao getDao() {
		return this.tipoFiltroDao;
	}

	@Override
	public TipoFiltro getEntity() {
		return tipoFiltro;
	}

	@Override
	protected void setEntity(TipoFiltro entity) {
		this.tipoFiltro = entity;
	}

}
