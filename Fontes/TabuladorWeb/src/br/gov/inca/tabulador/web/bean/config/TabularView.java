package br.gov.inca.tabulador.web.bean.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.inca.tabulador.domain.dao.config.TabelaConfigDao;
import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.CampoFiltro;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.web.bean.ViewBean;

@Named
@ViewScoped
public class TabularView implements Serializable, ViewBean {
	private static final long serialVersionUID = 4473731490041477811L;

	private @Inject TabelaConfigDao tabelaConfigDao;

	private TabelaConfig tabelaConfig;
	private List<CampoFiltro> campos;
	private List<CampoFiltro> camposFiltro;

	@PostConstruct
	public void init() {
		setTabelaConfig(new TabelaConfig());
		setCampos(new ArrayList<>());
		addCampo();
	}

	public void findById(Integer id) {
		if (id != null) {
			setTabelaConfig(getTabelaConfigDao().findById(id));
			setCamposConfig(getTabelaConfig().getCampos().stream().filter(x -> x.getFiltro()).collect(Collectors.toList()));
			setCamposFiltro(new ArrayList<>(getCampos()));
		}
	}

	public TabelaConfig getTabelaConfig() {
		return tabelaConfig;
	}

	public void setTabelaConfig(TabelaConfig tabelaConfig) {
		final List<CampoConfig> camposBag = tabelaConfig.getCampos();
		this.tabelaConfig = tabelaConfig;
		this.tabelaConfig.setCampos(new ArrayList<>());
		// Não utilizar PersistenceBag
		for (CampoConfig campo : camposBag) {
			this.tabelaConfig.getCampos().add(campo);
		}
	}

	public List<CampoFiltro> getCampos() {
		return campos;
	}

	public void setCamposConfig(List<CampoConfig> campos) {
		this.campos = new ArrayList<>();
		for (CampoConfig campo : campos) {
			this.campos.add(new CampoFiltro(campo));
		}
	}

	public void setCampos(List<CampoFiltro> campos) {
		this.campos = campos;
	}

	public TabelaConfigDao getTabelaConfigDao() {
		return tabelaConfigDao;
	}

	public void addCampo() {
		this.campos.add(new CampoFiltro());
	}

	public void atualizarTipoCampo(Integer index) {
		final CampoFiltro campoAtualizado = getCampos().get(index);
		tabelaConfig.getCampos().stream()
				.filter(x -> x.getId().equals(campoAtualizado.getId()))
				.findAny().map(x -> x.getTipoCampo())
				.ifPresent(x -> campoAtualizado.setTipoCampo(x));
	}

	public void removerCampo(Integer index) {
		this.campos.remove(index.intValue());
	}

	public List<CampoFiltro> getCamposFiltro() {
		return camposFiltro;
	}

	public void setCamposFiltro(List<CampoFiltro> camposFiltro) {
		this.camposFiltro = camposFiltro;
	}

	public void tabular() {
	}
}
