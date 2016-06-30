package br.gov.inca.tabulador.domain.db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import br.gov.inca.tabulador.domain.ValidationException;
import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.CampoFiltro;
import br.gov.inca.tabulador.domain.entity.config.CampoImport;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.domain.entity.tipo.TipoCampo;
import br.gov.inca.tabulador.domain.entity.tipo.TipoFiltro;
import br.gov.inca.tabulador.util.StringUtils;

public class StatementBuilder implements Serializable {
	private static final long serialVersionUID = 2437552777255361630L;

	public String createTable(TabelaConfig entity) throws SQLException {
		String tableName = getTableName(entity);
		final StringBuilder stringBuilder = new StringBuilder(String.format("CREATE SEQUENCE %s_seq;\n", getTableName(entity)));
		stringBuilder.append(String.format("CREATE TABLE IF NOT EXISTS %s (", tableName));
		stringBuilder.append(String.format("id_%1$s bigint not null default nextval('%1$s_seq')", tableName));
		for (CampoConfig campo : entity.getCampos()) {
			stringBuilder.append(", ").append(String.format("%s %s", getFieldName(campo), getFieldType(campo)));
		}
		stringBuilder.append(")");
		// TODO Criar índices
		return stringBuilder.toString();
	}

	public String dropTable(TabelaConfig entity) {
		return String.format("DROP TABLE IF EXISTS %1$s;\nDROP SEQUENCE IF EXISTS %1$s_seq;", getTableName(entity));
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
					setValueToStatement(prepareStatement, ++indexValor, campoImport.getCampo(), valoresLinha.get(campoImport.getPositionInFile()), campoImport.getPattern());
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
			stringBuilder.append(" WHERE ");
			boolean andWord = false;
			for (CampoFiltro campoFiltro : camposFiltro) {
				if (andWord) {
					stringBuilder.append(" AND ");
				}
				stringBuilder.append(getFieldName(campoFiltro.getCampo()));
				switch (campoFiltro.getCampo().getTipoFiltro().getId()) {
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
			setValueToStatement(prepareStatement, ++index, campoFiltro.getCampo(), campoFiltro.getValueAsString(), CampoImport.DEFAULT_PATTERN);
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
	
	private void setValueToStatement(PreparedStatement prepareStatement, int index, CampoConfig campo, String value, String datePattern) throws NumberFormatException, SQLException, ParseException {
		try {
			
			switch (campo.getTipoCampo().getId()) {
				case TipoCampo.TIPO_INTEIRO:
					/*if (value == null || value.isEmpty()) {
						throw new ValidationException(String.format("Filtro do campo '%s' do tipo número não pode ser vazio.", campo.getLabelOrNome()));
					}*/
					if (StringUtils.isNotBlank(value))
						prepareStatement.setLong(index, Long.parseLong(value));
					else
						prepareStatement.setNull(index, Types.INTEGER);
					break;
				case TipoCampo.TIPO_TEXTO:
					prepareStatement.setString(index, value);
					break;
				case TipoCampo.TIPO_DATA:
					/*if (value == null || value.isEmpty()) {
						throw new ValidationException(String.format("Filtro do campo '%' do tipo data não pode ser vazio.", campo.getLabelOrNome()));
					} else 	{*/
					if (StringUtils.isNotBlank(value)) {
						prepareStatement.setTimestamp(index, new Timestamp(new SimpleDateFormat(datePattern).parse(value).getTime()));
					} else
						prepareStatement.setNull(index, Types.DATE);
					break;
//					}
				default:
					prepareStatement.setObject(index, value);
					break;
			}
		} catch (NumberFormatException e) {
			throw new ValidationException(String.format("Filtro do campo '%s' do tipo número possui um valor inválido: '%s'.", campo.getLabelOrNome(), value));
		}
	}
}
