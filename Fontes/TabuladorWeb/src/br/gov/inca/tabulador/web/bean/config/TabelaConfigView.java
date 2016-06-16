package br.gov.inca.tabulador.web.bean.config;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import br.gov.inca.tabulador.domain.dao.config.TabelaConfigDao;
import br.gov.inca.tabulador.domain.db.ConnectionFactory;
import br.gov.inca.tabulador.domain.db.StatementBuilder;
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
	private @Inject ConnectionFactory connectionFactory;
	private @Inject StatementBuilder statementBuilder;

	@Override
	@PostConstruct
	public void init() {
		setEntities(null);
		setEntity(new TabelaConfig());

		setCampos(getEntity().getCampos());
	}
	
	@Override
	public void findById(Integer id) {
		if (id != null) {
			super.findById(id);
			if (getEntity() != null) {
				setCampos(getEntity().getCampos());
			} else {
				setEntity(new TabelaConfig());
			}
		}
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
		setCampos(getEntity().getCampos());
		return result;
	}

	public List<CampoConfig> getCampos() {
		return campos;
	}

	public void setCampos(List<CampoConfig> campos) {
		this.campos = new ArrayList<>();
		// Não utiliza PersistenceBag
		for (CampoConfig campo : campos) {
			this.campos.add(campo);
			final List<ValorCampoConfig> tempValores = campo.getValores();
			campo.setValores(new ArrayList<>());
			for (ValorCampoConfig valor : tempValores) {
				campo.getValores().add(valor);
			}
		}
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

	@Transactional
	public void createTable(int tableId) {
		findById(tableId);
		if (getEntity() != null) {
			try {
				createConnection().createStatement().executeUpdate(statementBuilder.createTable(getEntity()));
				showInfo("Criar tabela", String.format("Tabela '%s' criada com sucesso.", getEntity().getNome()));
			} catch (Exception e) {
				showError(e, "Erro", "Erro ao criar tabela");
			}
		}
	}

	@Transactional
	public void dropTable(int tableId) {
		findById(tableId);
		if (getEntity() != null) {
			try {
				createConnection().createStatement().executeUpdate(statementBuilder.dropTable(getEntity()));
				showInfo("Remover tabela", String.format("Tabela '%s' removida com sucesso.", getEntity().getNome()));
			} catch (Exception e) {
				showError(e, "Erro", "Erro ao remover tabela");
			}
		}
	}

	private Connection createConnection() throws Exception {
		// TODO Melhorar a obteção da conexão
		return connectionFactory.createConnection("org.postgresql.Driver",
				"jdbc:postgresql://it-des14:5433/tabulador",
				"postgres",
				"postgres");
	}

}
