package br.gov.inca.tabulador.web.bean.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.gov.inca.tabulador.domain.dao.config.TabelaConfigDao;
import br.gov.inca.tabulador.domain.db.ConnectionFactory;
import br.gov.inca.tabulador.domain.db.StatementBuilder;
import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.CampoImport;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.web.bean.ViewBean;

@Named
@ViewScoped
public class TabelaDadosView implements Serializable, ViewBean {
	private static final long serialVersionUID = 4473731490041477811L;

	private @Inject TabelaConfigDao tabelaConfigDao;
	private @Inject ConnectionFactory connectionFactory;
	private @Inject StatementBuilder statementBuilder;

	private TabelaConfig tabelaConfig;
	private List<CampoImport> campos;
	private String columnSeparator;
	private boolean ignoreFirstLine;
	private List<List<String>> linhas;
	
	@PostConstruct
	public void init() {
		setTabelaConfig(new TabelaConfig());
		setCampos(new ArrayList<>());
		setLinhas(new ArrayList<>());
		addCampo();
		setColumnSeparator(";");
	}

	public void findById(Integer id) {
		if (id != null) {
			setTabelaConfig(getTabelaConfigDao().findById(id));
			setCamposConfig(getTabelaConfig().getCampos());
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

	public List<CampoImport> getCampos() {
		return campos;
	}

	public void setCamposConfig(List<CampoConfig> campos) {
		this.campos = new ArrayList<>();
		for (CampoConfig campo : campos) {
			this.campos.add(new CampoImport(campo));
		}
	}

	public void setCampos(List<CampoImport> campos) {
		this.campos = campos;
	}

	public TabelaConfigDao getTabelaConfigDao() {
		return tabelaConfigDao;
	}

	public ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public StatementBuilder getStatementBuilder() {
		return statementBuilder;
	}

	public void addCampo() {
		this.campos.add(new CampoImport());
	}

	public void removeCampo(Integer index) {
		this.campos.remove(index.intValue());
	}
	
	public String getColumnSeparator() {
		return columnSeparator;
	}
	
	public void setColumnSeparator(String columnSeparator) {
		this.columnSeparator = columnSeparator;
	}
	
	public boolean isIgnoreFirstLine() {
		return ignoreFirstLine;
	}
	
	public void setIgnoreFirstLine(boolean ignoreFirstLine) {
		this.ignoreFirstLine = ignoreFirstLine;
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		final UploadedFile file = event.getFile();
		if (file != null) {
			try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputstream()))) {
				String sCurrentLine;
				while ((sCurrentLine = bufferedReader.readLine()) != null) {
					getLinhas().add(Arrays.asList(sCurrentLine.split(getColumnSeparator())));
				}
				showInfo("Arquivo", String.format("Arquivo '%s' recebido com sucesso.", file.getFileName()));
			} catch (IOException e) {
				showError(e, "Erro no arquivo", "Ocorreu um erro ao ler o arquivo.");
			}
		} else {
			showError("Arquivo não encontrado", "Arquivo ainda não foi submetido ou o upload não terminou.");
		}
    }
	
	public List<List<String>> getLinhas() {
		return linhas;
	}
	
	public void setLinhas(List<List<String>> linhas) {
		this.linhas = linhas;
	}
	
	public void importar() {
		if (!getLinhas().isEmpty()) {
			PreparedStatement insertInto;
			try {
				insertInto = statementBuilder.insertInto(null, getTabelaConfig(), getCampos(), getLinhas());
				showInfo("Arquivo", String.format("%d linhas inseridas com sucesso.", insertInto.executeLargeUpdate()));
			} catch (SQLException e) {
				showError("Erro ao inserir dados", "Ocorreu um erro ao tentar inserir os dados na tabela.");
			}
		} else {
			showError("Vazio", "Nenhuma linha encontrada para ser inserida.");
		}
	}
}
