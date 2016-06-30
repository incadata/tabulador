package br.gov.inca.tabulador.web.bean.consulta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.inca.tabulador.domain.dao.config.CampoConfigDao;
import br.gov.inca.tabulador.domain.dao.config.TabelaConfigDao;
import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.domain.entity.config.ValorCampoConfig;
import br.gov.inca.tabulador.web.bean.ViewBean;
import br.gov.inca.tabulador.web.entity.CampoFiltro;
import br.gov.inca.tabulador.web.entity.StatementBuilder;

@Named
@ViewScoped
public class GerarConsultaView implements ViewBean {
	private static final long serialVersionUID = 4473731490041477811L;

	private @Inject TabelaConfigDao tabelaConfigDao;
	private @Inject CampoConfigDao campoConfigDao;
	private @Inject StatementBuilder statementBuilder;
	private @Inject GerarConsultaResultado resultado;
	private transient @Inject PropertyResourceBundle messages;
	private transient @Inject Instance<Connection> connection;

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
			setTabelaConfig(getTabelaConfigDao().findById(id));
			final List<CampoConfig> campoIsFiltro = getTabelaConfig().getCampos().stream().filter(x -> x.isFiltro()).collect(Collectors.toList());
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

	protected GerarConsultaResultado getResultado() {
		return resultado;
	}

	protected void setResultado(GerarConsultaResultado resultado) {
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
		final int sizeCamposAgrupar = getCamposAgrupar().size();
		for (int i = 0; i < sizeCamposAgrupar; i++) {
			CampoConfig campoAgrupar = getCamposAgrupar().get(i);
			getCamposAgrupar().set(i, getCampoConfigDao().findById(campoAgrupar.getId()));
			campoAgrupar = getCamposAgrupar().get(i);
			final List<ValorCampoConfig> valores = campoAgrupar.getValores();
			campoAgrupar.setValores(new ArrayList<>());
			for (ValorCampoConfig valor : valores) {
				campoAgrupar.getValores().add(valor);
			}
		}
		for (CampoFiltro campo : getCampos()) {
			final CampoConfig campoDb = getCampoConfigDao().findById(campo.getCampo().getId());
			campo.getCampo().setNome(campoDb.getNome());
			campo.getCampo().setTipoFiltro(campoDb.getTipoFiltro());
		}
		try (final Connection connectionLocal = connection.get();
				final StatementBuilder statementBuilderLocal = getStatementBuilder();
				final PreparedStatement insertInto = statementBuilderLocal.selectTabular(connectionLocal, getTabelaConfig(), getCamposAgrupar(), getCampos());
				final ResultSet executeQuery = insertInto.executeQuery()) {
			final List<CampoConfig> resultColumns = getResultado().getColumns();
			resultColumns.clear();
			final CampoConfig countAsterisco = new CampoConfig();
			countAsterisco.setId(0);
			countAsterisco.setNome("_count(*)__");
			countAsterisco.setLabel("Total");
			resultColumns.addAll(getCamposAgrupar());
			final List<Map<CampoConfig, Object>> resultLines = getResultado().getLines();
			resultLines.clear();
			int contador = 0;
			while (executeQuery.next()) {
				final HashMap<CampoConfig, Object> hashMap = new HashMap<>();
				hashMap.put(countAsterisco, executeQuery.getLong(1));

				for (CampoConfig campo : getCamposAgrupar()) {
					hashMap.put(campo, executeQuery.getObject(campo.getNome()));
				}

				resultLines.add(hashMap);
				contador++;
			}
			resultColumns.add(countAsterisco);
			showInfo(null, String.format(getMessages().getString("n_lines_found"), contador));
		} catch (SQLFeatureNotSupportedException e) {
			if (e.getMessage().contains("free()")) {
				Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e.getLocalizedMessage());
			} else {
				showError(e, getMessages().getString("insert_data_title"), getMessages().getString("insert_data_msg"));
			}
		} catch (Exception e) {
			showError(e, getMessages().getString("insert_data_title"), getMessages().getString("insert_data_msg"));
		}
	}
	
}
