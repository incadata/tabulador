package br.gov.inca.tabulador.web.bean.config;

import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;

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
	private @Inject Instance<EntityManager> entityManager;

	@Override
	@PostConstruct
	public void init() {
		setEntities(null);
		setEntity(new TabelaConfig());

		setCampos(new ArrayList<>(getEntity().getCampos()));
	}
	
	@Override
	public void findById(Integer id) {
		super.findById(id);
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
	
	@Transactional
	public void createTable(int tableId) {
		findById(tableId);
		if (getEntity() != null) {
		}
	}

	@Transactional
	public void dropTable(int tableId) {
		findById(tableId);
		if (getEntity() != null) {
			try {
				final EntityManager em = getEntityManager();
				em.joinTransaction();
				
				final Session session = (Session) em.getDelegate();
				final SessionFactoryImplementor sfi = (SessionFactoryImplementor) session.getSessionFactory();
				final Statement stmt = sfi.getConnectionProvider().getConnection().createStatement();
				final String dropCommand = String.format("DROP TABLE %s", getEntity().getNome());
				System.out.println(dropCommand);
				stmt.executeUpdate(dropCommand);
			} catch (SQLException e) {
				showError(e, "Erro ao excluir tabelas");
			}
		}
	}
	
	private EntityManager getEntityManager() {
		return entityManager.get();
	}
}
