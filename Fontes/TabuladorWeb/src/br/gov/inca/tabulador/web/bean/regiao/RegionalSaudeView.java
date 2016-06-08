package br.gov.inca.tabulador.web.bean.regiao;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.inca.tabulador.domain.dao.regiao.RegionalSaudeDao;
import br.gov.inca.tabulador.domain.entity.regiao.RegionalSaude;
import br.gov.inca.tabulador.web.bean.ViewBean;

@Named(value = "regionalSaudeView")
@ViewScoped
public class RegionalSaudeView extends
		ViewBean<RegionalSaudeDao, RegionalSaude, Integer> implements
		Serializable {
	private static final long serialVersionUID = -6539486572898169197L;

	private RegionalSaude regionalSaude;
	private @Inject RegionalSaudeDao regionalSaudeDao;

	public RegionalSaudeView() {
		setEntity(new RegionalSaude());
	}

	@Override
	@PostConstruct
	public void init() {
		setRegionalSaude(new RegionalSaude());
		setEntities(null);
	}

	@Override
	public RegionalSaudeDao getDao() {
		return this.regionalSaudeDao;
	}

	@Override
	public RegionalSaude getEntity() {
		return regionalSaude;
	}

	public void setRegionalSaude(RegionalSaude regionalSaude) {
		this.regionalSaude = regionalSaude;
	}

	@Override
	protected void setEntity(RegionalSaude entity) {
		this.regionalSaude = entity;
	}
}
