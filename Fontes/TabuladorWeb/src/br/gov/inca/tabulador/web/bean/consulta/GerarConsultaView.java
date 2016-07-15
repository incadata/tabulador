package br.gov.inca.tabulador.web.bean.consulta;

import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.hibernate.QueryException;

import br.gov.inca.tabulador.domain.dao.config.CampoConfigDao;
import br.gov.inca.tabulador.domain.dao.config.TabelaConfigDao;
import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.domain.result.bean.ConsultaDinamicaResult;
import br.gov.inca.tabulador.domain.service.ConsultaDinamicaService;
import br.gov.inca.tabulador.domain.sql.StatementBuilder;
import br.gov.inca.tabulador.domain.vo.CampoFiltro;
import br.gov.inca.tabulador.web.bean.ViewBean;

public abstract class GerarConsultaView implements ViewBean {
	private static final long serialVersionUID = 4473731490041477811L;

	private @Inject TabelaConfigDao tabelaConfigDao;
	private @Inject CampoConfigDao campoConfigDao;
	private @Inject StatementBuilder statementBuilder;
	private @Inject ConsultaDinamicaResult resultado;
	private @Inject ConsultaDinamicaService service;
	private transient @Inject PropertyResourceBundle messages;
	

	private TabelaConfig tabelaConfig;
	private List<CampoFiltro> campos;
	private List<CampoConfig> camposFiltro;
	private List<CampoConfig> camposAgrupar;
	private int resultadosPorPagina;

	@PostConstruct
	public void init() {
		setTabelaConfig(new TabelaConfig());
		setCampos(new ArrayList<>());
		addCampo();
		setCamposFiltro(new ArrayList<>());
		setCamposAgrupar(new ArrayList<>());
		addCampoAgrupar();
		setResultadosPorPagina(10);
	}

	public void findById(Integer id) {
		if (id != null) {
			setTabelaConfig(getTabelaConfigDao().findById(id).get());
			final List<CampoConfig> campoIsFiltro = this.tabelaConfig.getCampos().stream().filter(x -> x.isFiltro()).collect(Collectors.toList());
			setCamposWithCampoConfig(campoIsFiltro);
			setCamposFiltro(campoIsFiltro);
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

	public void setCamposWithCampoConfig(List<CampoConfig> campos) {
		this.campos = new ArrayList<>();
		for (CampoConfig campo : campos) {
			getCampos().add(new CampoFiltro(campo));
		}
	}

	public void setCampos(List<CampoFiltro> campos) {
		this.campos = campos;
	}

	public TabelaConfigDao getTabelaConfigDao() {
		return tabelaConfigDao;
	}
	
	public CampoConfigDao getCampoConfigDao() {
		return campoConfigDao;
	}

	public void addCampo() {
		final int nextInt = new Random().nextInt();
		final CampoFiltro campoFiltro = new CampoFiltro();
		campoFiltro.getCampo().getTipoFiltro().setId(nextInt > 0 ? -nextInt : nextInt);
		getCampos().add(campoFiltro);
	}

	public void addCampoAgrupar() {
		getCamposAgrupar().add(new CampoConfig());
	}

	public void removerFiltro(Integer index) {
		getCampos().remove(index.intValue());
	}

	public void removerCampoAgrupar(Integer index) {
		getCamposAgrupar().remove(index.intValue());
	}

	public List<CampoConfig> getCamposFiltro() {
		return camposFiltro;
	}

	public void setCamposFiltro(List<CampoConfig> camposFiltro) {
		this.camposFiltro = new ArrayList<>();
		for (CampoConfig campo : camposFiltro) {
			this.camposFiltro.add(campo);
		}
	}

	public List<CampoConfig> getCamposAgrupar() {
		return camposAgrupar;
	}

	protected ConsultaDinamicaResult getResultado() {
		return resultado;
	}

	protected void setResultado(ConsultaDinamicaResult resultado) {
		this.resultado = resultado;
	}

	public void setCamposAgrupar(List<CampoConfig> campoAgrupar) {
		this.camposAgrupar = new ArrayList<>();
		for (CampoConfig campo : campoAgrupar) {
			this.camposAgrupar.add(campo);
		}
	}

	protected StatementBuilder getStatementBuilder() {
		return statementBuilder;
	}

	public int getResultadosPorPagina() {
		return resultadosPorPagina;
	}

	public void setResultadosPorPagina(int resultadosPorPagina) {
		this.resultadosPorPagina = resultadosPorPagina;
	}

	protected PropertyResourceBundle getMessages() {
		return messages;
	}

	public void tabular() {
		try {
			setResultado(service.executarConsulta(getTabelaConfig(), campos, camposAgrupar));
			showInfo(null, String.format(messages.getString("n_lines_found"), getResultado().getLines().size()));
		} catch (QueryException e) {
			showError(e, messages.getString("insert_data_title"), messages.getString("insert_data_msg"));
		}
	}
	
}
