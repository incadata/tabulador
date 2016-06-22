package br.gov.inca.tabulador.web.bean.consulta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.inca.tabulador.domain.dao.config.CampoConfigDao;
import br.gov.inca.tabulador.domain.dao.config.TabelaConfigDao;
import br.gov.inca.tabulador.domain.db.StatementBuilder;
import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.CampoFiltro;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.domain.entity.config.ValorCampoConfig;
import br.gov.inca.tabulador.web.bean.ViewBean;

@Named
@ViewScoped
public class GerarConsultaView implements ViewBean {
	private static final long serialVersionUID = 4473731490041477811L;

	private @Inject TabelaConfigDao tabelaConfigDao;
	private @Inject CampoConfigDao campoConfigDao;
	private @Inject StatementBuilder statementBuilder;
	private @Inject GerarConsultaResultado resultado;
	private transient @Inject Instance<Connection> connection;

	private TabelaConfig tabelaConfig;
	private List<CampoFiltro> campos;
	private List<CampoFiltro> camposFiltro;
	private List<CampoConfig> camposAgrupar;

	@PostConstruct
	public void init() {
		setTabelaConfig(new TabelaConfig());
		setCampos(new ArrayList<>());
		addCampo();
		setCamposFiltro(new ArrayList<>());
		setCamposAgrupar(new ArrayList<>());
		addCampoAgrupar();
	}

	public void findById(Integer id) {
		if (id != null) {
			setTabelaConfig(getTabelaConfigDao().findById(id));
			setCamposConfig(getTabelaConfig().getCampos().stream().filter(x -> x.getFiltro()).collect(Collectors.toList()));
			setCamposFiltro(getCampos());
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
		getCampos().add(new CampoFiltro());
	}

	public void addCampoAgrupar() {
		getCamposAgrupar().add(new CampoConfig());
	}

	public void atualizarTipoCampo(Integer index) {
		final CampoFiltro campoAtualizado = getCampos().get(index);
		tabelaConfig.getCampos().stream()
				.filter(x -> x.getId().equals(campoAtualizado.getId()))
				.findAny().map(x -> x.getTipoCampo())
				.ifPresent(x -> campoAtualizado.setTipoCampo(x));
	}

	public void removerCampo(Integer index) {
		getCampos().remove(index.intValue());
	}

	public void removerCampoAgrupar(Integer index) {
		getCamposAgrupar().remove(index.intValue());
	}

	public List<CampoFiltro> getCamposFiltro() {
		return camposFiltro;
	}

	public void setCamposFiltro(List<CampoFiltro> camposFiltro) {
		this.camposFiltro = new ArrayList<>();
		for (CampoFiltro campo : camposFiltro) {
			this.camposFiltro.add(campo);
		}
	}

	public List<CampoConfig> getCamposAgrupar() {
		return camposAgrupar;
	}
	
	public GerarConsultaResultado getResultado() {
		return resultado;
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
		for (CampoFiltro campo : getCamposFiltro()) {
			final CampoConfig campoDb = getCampoConfigDao().findById(campo.getId());
			campo.setNome(campoDb.getNome());
			campo.setTipoFiltro(campoDb.getTipoFiltro());
		}
		try (final Connection connectionLocal = connection.get()) {
			final PreparedStatement insertInto = getStatementBuilder().selectTabular(connectionLocal, getTabelaConfig(), getCamposAgrupar(), getCampos());
			final ResultSet executeQuery = insertInto.executeQuery();

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
			showInfo(null, String.format("%d linhas encontradas", contador));
		} catch (SQLException | ParseException e) {
			showError(e, "Erro ao inserir dados", "Ocorreu um erro ao tentar inserir os dados na tabela.");
		}
	}
}
