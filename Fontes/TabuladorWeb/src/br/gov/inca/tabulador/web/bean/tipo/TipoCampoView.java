package br.gov.inca.tabulador.web.bean.tipo;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.inca.tabulador.domain.dao.tipo.TipoCampoDao;
import br.gov.inca.tabulador.domain.entity.tipo.TipoCampo;
import br.gov.inca.tabulador.web.bean.ViewBean;

@Named
@ViewScoped
public class TipoCampoView extends
		ViewBean<TipoCampoDao, TipoCampo, Integer> implements
		Serializable {
	private static final long serialVersionUID = -6539486572898169197L;

	private TipoCampo tipoCampo;
	private @Inject TipoCampoDao tipoCampoDao;

	@Override
	@PostConstruct
	public void init() {
		setEntities(null);
		setEntity(new TipoCampo());
	}

	@Override
	public TipoCampoDao getDao() {
		return this.tipoCampoDao;
	}

	@Override
	public TipoCampo getEntity() {
		return tipoCampo;
	}

	@Override
	protected void setEntity(TipoCampo entity) {
		this.tipoCampo = entity;
	}
	
}
