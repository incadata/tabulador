package br.gov.inca.tabulador.web.bean.config;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import br.gov.inca.tabulador.domain.dao.config.CampoConfigDao;
import br.gov.inca.tabulador.domain.dao.config.TabelaConfigDao;
import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.domain.entity.config.ValorCampoConfig;
import br.gov.inca.tabulador.domain.sql.StatementBuilder;
import br.gov.inca.tabulador.web.bean.ViewCrudBean;

@Named(value = "tabelaConfigView")
@ViewScoped
public class TabelaConfigView extends
		ViewCrudBean<TabelaConfigDao, TabelaConfig, Integer> implements
		Serializable {
	private static final long serialVersionUID = -6539486572898169197L;

	private TabelaConfig tabelaConfig;
	private List<CampoConfig> camposParaRemover;
	private @Inject CampoConfigDao campoConfigDao;
	private @Inject TabelaConfigDao tabelaConfigDao;
	private @Inject StatementBuilder statementBuilder;
	private transient @Inject Instance<Connection> connection;

	@Override
	@PostConstruct
	public void init() {
		setEntities(null);
		setEntity(new TabelaConfig());

		setCampos(getEntity().getCampos());
		setCamposParaRemover(new ArrayList<>());
	}
	
	@Override
	public String saveOrUpdate() {
		for (CampoConfig campoParaRemover : getCamposParaRemover()) {
			campoConfigDao.removeById(campoParaRemover.getId());
		}
		return super.saveOrUpdate();
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

	private void setCampos(List<CampoConfig> campos) {
		getEntity().setCampos(new ArrayList<>());
		// Não utiliza PersistenceBag
		for (CampoConfig campo : campos) {
			getEntity().getCampos().add(campo);
			final List<ValorCampoConfig> tempValores = campo.getValores();
			campo.setValores(new ArrayList<>());
			for (ValorCampoConfig valor : tempValores) {
				campo.getValores().add(valor);
			}
		}
	}

	public void addCampo() {
		getEntity().getCampos().add(new CampoConfig());
	}

	public void addValor(int campoIndex) {
		getEntity().getCampos().get(campoIndex).getValores().add(new ValorCampoConfig());
	}

	public void removerCampo(int index) {
		getCamposParaRemover().add(getEntity().getCampos().get(index));
		getEntity().getCampos().remove(index);
	}

	public void removerValor(int campoIndex, int index) {
		getEntity().getCampos().get(campoIndex).getValores().remove(index);
	}

	@Transactional
	public void createTable(int tableId) {
		findById(tableId);
		if (getEntity() != null) {
			try (final Connection connectionLocal = connection.get();
					final StatementBuilder statementBuilderlocal = getStatementBuilder();
					final Statement createStatement = connectionLocal.createStatement()) {
				createStatement.executeUpdate(statementBuilderlocal.createTable(getEntity()));
				showInfo(getMessages().getString("create_table"), String.format(getMessages().getString("create_table_name_success_msg"), getEntity().getNome()));
			} catch (Exception e) {
				showError(e, getMessages().getString("error"), getMessages().getString("create_table_error_msg"));
			}
		}
	}

	@Transactional
	public void dropTable(int tableId) {
		findById(tableId);
		if (getEntity() != null) {
			try (final Connection connectionLocal = connection.get();
					final StatementBuilder statementBuilderlocal = getStatementBuilder();
					final Statement updateStatement = connectionLocal.createStatement()) {
				updateStatement.executeUpdate(statementBuilderlocal.dropTable(getEntity()));
				showInfo(getMessages().getString("remove_table"), String.format(getMessages().getString("remove_table_name_success_msg"), getEntity().getNome()));
			} catch (Exception e) {
				showError(e, getMessages().getString("error"), getMessages().getString("remove_table_error_msg"));
			}
		}
	}

	private List<CampoConfig> getCamposParaRemover() {
		return camposParaRemover;
	}
	
	private void setCamposParaRemover(List<CampoConfig> camposParaRemover) {
		this.camposParaRemover = camposParaRemover;
	}
	
	private StatementBuilder getStatementBuilder() {
		return statementBuilder;
	}
}
