package br.gov.inca.tabulador.web.bean.regiao;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.inca.tabulador.domain.dao.regiao.RegionalSaudeDao;
import br.gov.inca.tabulador.domain.entity.regiao.RegionalSaude;
import br.gov.inca.tabulador.web.bean.ViewBean;

@Named
@ViewScoped
public class RegionalSaudeView extends ViewBean<RegionalSaudeDao, RegionalSaude, Integer> {
	private RegionalSaude regionalSaude;
	@Inject
	private RegionalSaudeDao regionalSaudeDao;

	public RegionalSaudeView() {
		regionalSaude = new RegionalSaude();
	}

	@Override
	@PostConstruct
	public void init() {
	}

	@Override
	public RegionalSaudeDao getDao() {
		return this.regionalSaudeDao;
	}

	@Override
	public RegionalSaude getEntity() {
		return regionalSaude;
	}

}
