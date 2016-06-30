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
import java.util.PropertyResourceBundle;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.gov.inca.tabulador.domain.dao.config.TabelaConfigDao;
import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.web.bean.ViewBean;
import br.gov.inca.tabulador.web.entity.CampoImport;
import br.gov.inca.tabulador.web.entity.StatementBuilder;

@Named
@ViewScoped
public class TabelaImportarView implements Serializable, ViewBean {
	private static final long serialVersionUID = 4473731490041477811L;

	private @Inject TabelaConfigDao tabelaConfigDao;
	private @Inject StatementBuilder statementBuilder;
	private transient @Inject PropertyResourceBundle messages;
	private transient @Inject Instance<Connection> connection;

	private TabelaConfig tabelaConfig;
	private List<CampoImport> campos;
	private List<CampoConfig> camposBusca;
	private String columnSeparator;
	private boolean ignoreFirstLine;
	private List<String> linhas;
	
	@PostConstruct
	public void init() {
		setTabelaConfig(new TabelaConfig());
		setCampos(new ArrayList<>());
		setCamposBusca(new ArrayList<>());
		addCampo();
		setColumnSeparator(";");
	}

	public void findById(Integer id) {
		if (id != null) {
			setTabelaConfig(getTabelaConfigDao().findById(id));
			setCampos(convertCampoConfig(getTabelaConfig().getCampos()));
			setCamposBusca(new ArrayList<>(getTabelaConfig().getCampos()));
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

	private List<CampoImport> convertCampoConfig(List<CampoConfig> converter) {
		final ArrayList<CampoImport> convertido = new ArrayList<CampoImport>();
		for (CampoConfig campo : converter) {
			convertido.add(new CampoImport(campo));
		}
		return convertido;
	}

	public List<CampoImport> getCampos() {
		return campos;
	}
	
	public void setCampos(List<CampoImport> campos) {
		this.campos = campos;
	}

	public List<CampoConfig> getCamposBusca() {
		return camposBusca;
	}

	private void setCamposBusca(List<CampoConfig> camposBusca) {
		this.camposBusca = camposBusca;
	}

	public TabelaConfigDao getTabelaConfigDao() {
		return tabelaConfigDao;
	}

	public StatementBuilder getStatementBuilder() {
		return statementBuilder;
	}

	public void addCampo() {
		getCampos().add(new CampoImport());
	}

	public void removerCampo(Integer index) {
		getCampos().remove(index.intValue());
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

	protected PropertyResourceBundle getMessages() {
		return messages;
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
				
				showInfo(getMessages().getString("file_received_title"), String.format(getMessages().getString("file_received_msg"), file.getFileName()));
			} catch (IOException e) {
				showError(e, getMessages().getString("file_read_title"), getMessages().getString("file_read_msg"));
			}
		} else {
			setLinhas(null);
			showError(getMessages().getString("file_not_found_title"), getMessages().getString("file_not_found_msg"));
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
			showError(getMessages().getString("file_received_title"), getMessages().getString("no_file_received_msg"));
		} else if (!getLinhas().isEmpty()) {
			try {
				final List<List<String>> linhasColunas = new ArrayList<>(getLinhas().size());
				List<String> cabecalho = null;
				for (String linha : getLinhas()) {
					linhasColunas.add(Arrays.asList(linha.split(getColumnSeparator())));
				}
				if (isIgnoreFirstLine()) {
					cabecalho = linhasColunas.get(0);
					linhasColunas.remove(0);
				}
				try (final Connection connectionLocal = connection.get();
					final StatementBuilder statementBuilderLocal = getStatementBuilder()) {
					final int camposSize = getCampos().size();
					for (int i = 0; i < camposSize; i++) {
						final Integer campoId = getCampos().get(i).getCampo().getId();
						final CampoImport campoImport = new CampoImport(getCampos().get(i));
						campoImport.setCampo(getCamposBusca().stream().filter(x -> x.getId().equals(campoId)).findAny().get());
						
						campoImport.setPositionInFile(cabecalho != null ? cabecalho.indexOf(campoImport.getCampo().getNome().trim().toUpperCase()) : i);
						getCampos().set(i, campoImport);
					}
					final PreparedStatement insertInto = statementBuilderLocal.insertInto(connectionLocal, getTabelaConfig(), getCampos(), linhasColunas);
					showInfo(getMessages().getString("file_received_title"), String.format(getMessages().getString("n_m_lines_received_msg"), IntStream.of(insertInto.executeBatch()).sum(), linhasColunas.size()));
					insertInto.close();
				}
			} catch (SQLException | ParseException e) {
				showError(e, getMessages().getString("insert_data_title"), getMessages().getString("insert_data_msg"));
			}
		} else {
			showError(getMessages().getString("table_empty_title"), getMessages().getString("table_empty_msg"));
		}
	}
}
