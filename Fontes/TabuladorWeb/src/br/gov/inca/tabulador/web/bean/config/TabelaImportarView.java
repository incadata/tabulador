package br.gov.inca.tabulador.web.bean.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.gov.inca.tabulador.domain.dao.config.TabelaConfigDao;
import br.gov.inca.tabulador.domain.db.StatementBuilder;
import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.CampoImport;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.web.bean.ViewBean;

@Named
@ViewScoped
public class TabelaImportarView implements Serializable, ViewBean {
	private static final long serialVersionUID = 4473731490041477811L;

	private @Inject TabelaConfigDao tabelaConfigDao;
	private @Inject StatementBuilder statementBuilder;
	private transient @Inject Instance<Connection> connection;

	private TabelaConfig tabelaConfig;
	private List<CampoImport> campos;
	private String columnSeparator;
	private boolean ignoreFirstLine;
	private List<String> linhas;
	
	@PostConstruct
	public void init() {
		setTabelaConfig(new TabelaConfig());
		setCampos(new ArrayList<>());
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

	public StatementBuilder getStatementBuilder() {
		return statementBuilder;
	}

	public void addCampo() {
		this.campos.add(new CampoImport());
	}

	public void atualizarTipoCampo(Integer index) {
		final CampoImport campoAtualizado = getCampos().get(index);
		tabelaConfig.getCampos().stream()
				.filter(x -> x.getId().equals(campoAtualizado.getId()))
				.findAny().map(x -> x.getTipoCampo())
				.ifPresent(x -> campoAtualizado.setTipoCampo(x));
	}

	public void removerCampo(Integer index) {
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
				setLinhas(new ArrayList<>());
				String sCurrentLine;
				while ((sCurrentLine = bufferedReader.readLine()) != null) {
					getLinhas().add(sCurrentLine);
				}
				showInfo("Arquivo", String.format("Arquivo '%s' recebido com sucesso.", file.getFileName()));
			} catch (IOException e) {
				showError(e, "Erro no arquivo", "Ocorreu um erro ao ler o arquivo.");
			}
		} else {
			setLinhas(null);
			showError("Arquivo não encontrado", "Arquivo ainda não foi submetido ou o upload não terminou.");
		}
    }
	
	private List<String> getLinhas() {
		return linhas;
	}
	
	public void setLinhas(List<String> linhas) {
		this.linhas = linhas;
	}
	
	public void importar() {
		if (getLinhas() == null) {
			showError("Arquivo", "Nenhum arquivo recebido.");
		} else if (!getLinhas().isEmpty()) {
			try {
				final List<List<String>> linhasColunas = new ArrayList<>(getLinhas().size());
				for (String linha : getLinhas()) {
					linhasColunas.add(Arrays.asList(linha.split(getColumnSeparator())));
				}
				if (isIgnoreFirstLine()) {
					linhasColunas.remove(0);
				}
				try (final Connection connectionLocal = connection.get()) {
					final PreparedStatement insertInto = getStatementBuilder().insertInto(connectionLocal, getTabelaConfig(), getCampos(), linhasColunas);
					showInfo("Arquivo", String.format("%d/%d linhas inseridas com sucesso.", IntStream.of(insertInto.executeBatch()).sum(), linhasColunas.size()));
				}
			} catch (SQLException | ParseException e) {
				showError(e, "Erro ao inserir dados", "Ocorreu um erro ao tentar inserir os dados na tabela.");
			}
		} else {
			showError("Vazio", "Nenhuma linha encontrada para ser inserida.");
		}
	}
}
