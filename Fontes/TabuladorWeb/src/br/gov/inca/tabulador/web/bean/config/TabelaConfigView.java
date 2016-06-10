package br.gov.inca.tabulador.web.bean.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.inca.tabulador.domain.dao.config.TabelaConfigDao;
import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.domain.entity.config.ValorCampoConfig;
import br.gov.inca.tabulador.web.bean.ViewBean;

@Named(value = "tabelaConfigView")
@ViewScoped
public class TabelaConfigView extends
		ViewBean<TabelaConfigDao, TabelaConfig, Integer> implements
		Serializable {
	private static final long serialVersionUID = -6539486572898169197L;

	private TabelaConfig tabelaConfig;
	private List<CampoConfig> campos;
	private @Inject TabelaConfigDao tabelaConfigDao;

	@Override
	@PostConstruct
	public void init() {
		setEntities(null);

		final TabelaConfig first = getDao().findAll().getUniqueResult();
		setEntity(first != null ? first : new TabelaConfig());

		setCampos(new ArrayList<>(getEntity().getCampos()));
	}

	@Override
	public TabelaConfigDao getDao() {
		return this.tabelaConfigDao;
	}

	@Override
	public TabelaConfig getEntity() {
		return tabelaConfig;
	}

	@Override
	protected void setEntity(TabelaConfig entity) {
		this.tabelaConfig = entity;
	}
	
	@Override
	public String saveOrUpdate() {
		getEntity().setCampos(getCampos());
		final String result = super.saveOrUpdate();
		setCampos(new ArrayList<>(getEntity().getCampos()));
		return result;
	}
	
	public List<CampoConfig> getCampos() {
		return campos;
	}
	
	public void setCampos(List<CampoConfig> campos) {
		this.campos = campos;
	}
	
	public void addCampo() {
		getCampos().add(new CampoConfig());
	}

	public void addValor(int campoIndex) {
		getCampos().get(campoIndex).getValores().add(new ValorCampoConfig());
	}
	
	public void removerCampo(int index) {
		getCampos().remove(index);
	}

	public void removerValor(int campoIndex, int index) {
		getCampos().get(campoIndex).getValores().remove(index);
	}
}
