package br.gov.inca.tabulador.domain.sql;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import br.gov.inca.tabulador.domain.ValidationException;
import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.domain.entity.tipo.TipoCampo;
import br.gov.inca.tabulador.domain.entity.tipo.TipoFiltro;
import br.gov.inca.tabulador.domain.vo.CampoFiltro;
import br.gov.inca.tabulador.domain.vo.CampoImport;
import br.gov.inca.tabulador.util.DateUtils;
import br.gov.inca.tabulador.util.StringUtils;

public class StatementBuilder implements Serializable, AutoCloseable {
	private static final long serialVersionUID = 2437552777255361630L;

	private transient Collection<Array> connectionArrays;

	public StatementBuilder() {
		setConnectionArrays(new ArrayList<>());
	}

	public String createTable(TabelaConfig entity) throws SQLException {
		final StringBuilder stringBuilder = new StringBuilder(String.format(
				"CREATE TABLE %s (", getTableName(entity)));
		boolean colocarVirgula = false;
		for (CampoConfig campo : entity.getCampos()) {
			if (colocarVirgula) {
				stringBuilder.append(", ");
			}
			stringBuilder.append(String.format("%s %s", getFieldName(campo), getFieldType(campo)));
			colocarVirgula = true;
		}
		stringBuilder.append(")");
		// TODO Criar índices
		return stringBuilder.toString();
	}

	public String dropTable(TabelaConfig entity) {
		// TODO Apagar índices
		return String.format("DROP TABLE %s", getTableName(entity));
	}

	protected String insertIntoCommand(TabelaConfig entity, List<CampoImport> campos) {
		final StringBuilder stringBuilder = new StringBuilder("INSERT INTO ");
		stringBuilder.append(getTableName(entity));
		stringBuilder.append(" (");
		boolean virgula = false;
		for (CampoImport campo : campos) {
			if (!campo.isIgnore()) {
				if (virgula) {
					stringBuilder.append(", ");
				}
				stringBuilder.append(getFieldName(campo.getCampo()));
				virgula = true;
			}
		}
		stringBuilder.append(") VALUES (");
		virgula = false;
		for (CampoImport campo : campos) {
			if (!campo.isIgnore()) {
				if (virgula) {
					stringBuilder.append(", ");
				}
				stringBuilder.append("?");
				virgula = true;
			}
		}
		stringBuilder.append(")");
		return stringBuilder.toString();
	}

	public PreparedStatement insertInto(Connection connection, TabelaConfig entity, List<CampoImport> campos, List<List<String>> listaDeValores) throws SQLException, ParseException {
		final PreparedStatement prepareStatement = connection.prepareStatement(insertIntoCommand(entity, campos));
		for (List<String> valoresLinha : listaDeValores) {
			final int camposSize = campos.size();
			int indexValor = 0;
			for (int i = 0; i < camposSize; i++) {
				final CampoImport campoImport = campos.get(i);
				if (!campoImport.isIgnore()) {
					final CampoFiltro campoFiltro = new CampoFiltro();
					campoFiltro.setValue(valoresLinha.get(i));
					campoFiltro.setCampo(campoImport.getCampo());
					setValueToStatement(connection, prepareStatement, ++indexValor, campoFiltro, campoImport.getPattern());
				}
			}
			prepareStatement.addBatch();
		}
		return prepareStatement;
	}

	protected String selectTabular(TabelaConfig entity, List<CampoConfig> camposAgrupar, List<CampoFiltro> camposFiltro) {
		final StringBuilder stringBuilder = new StringBuilder("SELECT COUNT(*)");
		for (CampoConfig campoAgrupar : camposAgrupar) {
			stringBuilder.append(", ");
			stringBuilder.append(getFieldName(campoAgrupar));
		}

		stringBuilder.append(" FROM ");
		stringBuilder.append(getTableName(entity));

		if (!camposFiltro.isEmpty()) {
			boolean andWord = false;
			for (CampoFiltro campoFiltro : camposFiltro) {
				if (!campoFiltro.hasValue()) continue;
				
				if (andWord) {
					stringBuilder.append(" AND ");
				} else 
					stringBuilder.append(" WHERE ");
				stringBuilder.append(getFieldName(campoFiltro.getCampo()));
				switch (campoFiltro.getFiltro().getId()) {
					case TipoFiltro.FILTRO_IGUAL:
						stringBuilder.append(" = ?");
						break;
					case TipoFiltro.FILTRO_MAIOR:
						stringBuilder.append(" > ?");
						break;
					case TipoFiltro.FILTRO_MENOR:
						stringBuilder.append(" < ?");
						break;
					case TipoFiltro.FILTRO_MAIOR_IGUAL:
						stringBuilder.append(" >= ?");
						break;
					case TipoFiltro.FILTRO_MENOR_IGUAL:
						stringBuilder.append(" <= ?");
						break;
					case TipoFiltro.FILTRO_CONTEM:
						stringBuilder.append(" LIKE ?");
						break;
					case TipoFiltro.FILTRO_DIFERENTE:
						stringBuilder.append(" <> ?");
						break;
					case TipoFiltro.FILTRO_MULTIPLO:
						stringBuilder.append(" = ANY(?)");
						break;
				}
				andWord = true;
			}
		}

		if (!camposAgrupar.isEmpty()) {
			stringBuilder.append(" GROUP BY ");
			boolean virgula = false;
			for (CampoConfig campoAgrupar : camposAgrupar) {
				if (virgula) {
					stringBuilder.append(", ");
				}
				stringBuilder.append(getFieldName(campoAgrupar));
				virgula = true;
			}
		}
		return stringBuilder.toString();
	}

	public PreparedStatement selectTabular(Connection connection, TabelaConfig entity, List<CampoConfig> camposAgrupar, List<CampoFiltro> camposFiltro) throws SQLException, NumberFormatException, ParseException {
		final PreparedStatement prepareStatement = connection.prepareStatement(selectTabular(entity, camposAgrupar, camposFiltro));
		int index = 0;
		for (CampoFiltro campoFiltro : camposFiltro) {
			if (campoFiltro.hasValue())
				setValueToStatement(connection, prepareStatement, ++index, campoFiltro, CampoImport.DEFAULT_PATTERN);
		}
		return prepareStatement;
	}

	protected String getTableName(TabelaConfig entity) {
		return String.format("value_table_%d", entity.getId());
	}

	protected String getFieldName(CampoConfig campo) {
		return campo.getNome();
	}

	protected String getFieldType(CampoConfig campo) {
		switch (campo.getTipoCampo().getId()) {
			case TipoCampo.TIPO_INTEIRO:
				return "bigint";
			case TipoCampo.TIPO_TEXTO:
				return "character varying(255)";
			case TipoCampo.TIPO_DATA:
				return "date";
		}
		throw new RuntimeException("Tipo inválido para o campo: " + campo.getNome());
	}

	private void setValueToStatement(Connection connection, PreparedStatement prepareStatement, int index, CampoFiltro campoFiltro, String datePattern) throws NumberFormatException, SQLException, ParseException {
		final CampoConfig campo = campoFiltro.getCampo();
		if (TipoFiltro.FILTRO_MULTIPLO == campoFiltro.getFiltro().getId()) {
			final Array array = connection.createArrayOf(getFieldType(campoFiltro.getCampo()), campoFiltro.getValores().getTarget().stream().map(x -> x.getId()).collect(Collectors.toList()).toArray());
			
			prepareStatement.setArray(index, array);
			getConnectionArrays().add(array);
		} else {
			final String value = campoFiltro.getValueAsString();
			try {
				if (StringUtils.isNotBlank(value))
					switch (campo.getTipoCampo().getId()) {
						case TipoCampo.TIPO_INTEIRO:
							/*if (value == null || value.isEmpty()) {
								throw new ValidationException(String.format("Filtro do campo '%s' do tipo número não pode ser vazio.", campo.getLabelOrNome()));
							}*/
							prepareStatement.setLong(index, Long.parseLong(value));
							break;
						case TipoCampo.TIPO_TEXTO:
							prepareStatement.setString(index, value);
							break;
						case TipoCampo.TIPO_DATA:
							/*if (value == null || value.isEmpty()) {
								throw new ValidationException(String.format("Filtro do campo '%' do tipo data não pode ser vazio.", campo.getLabelOrNome()));
							} else 	{*/
							prepareStatement.setTimestamp(index, new Timestamp(DateUtils.toCalendar(datePattern, value).getTimeInMillis()));
							break;
	//						}
						default:
							prepareStatement.setObject(index, value);
							break;
					}
			} catch (NumberFormatException e) {
				throw new ValidationException(String.format("Filtro do campo '%s' do tipo número possui um valor inválido: '%s'.", campo.getLabelOrNome(), value));
			}
		}
	}

	@Override
	public void close() throws SQLException {
		for (Array array : getConnectionArrays()) {
			array.free();
		}
	}

	private Collection<Array> getConnectionArrays() {
		return connectionArrays;
	}

	private void setConnectionArrays(Collection<Array> connectionArrays) {
		this.connectionArrays = connectionArrays;
	}
}