package br.gov.inca.tabulador.domain.db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.CampoImport;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.domain.entity.tipo.TipoCampo;

public class StatementBuilder implements Serializable {
	private static final long serialVersionUID = 2437552777255361630L;

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
		return stringBuilder.toString();
	}

	public String dropTable(TabelaConfig entity) {
		return String.format("DROP TABLE %s", getTableName(entity));
	}
	
	public String tableExists(TabelaConfig entity) {
		return String.format("SELECT (count(*) = 1) FROM INFORMATION_SCHEMA.TABLES" 
				+ "WHERE TABLE_CATALOG = '%s'" 
				+ "AND  TABLE_NAME = '%s'", getTableCatalog(entity), getTableName(entity));
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
				stringBuilder.append(getFieldName(campo));
				virgula = true;
			}
		}
		stringBuilder.append(") VALUES (");
		virgula = false;
		final int camposSize = campos.size();
		for (int j = 0; j < camposSize; j++) {
			if (virgula) {
				stringBuilder.append(", ");
			}
			stringBuilder.append("?");
			virgula = true;
		}
		stringBuilder.append(")");
		return stringBuilder.toString();
	}

	public PreparedStatement insertInto(Connection connection, TabelaConfig entity, List<CampoImport> campos, List<List<String>> listaDeValores) throws SQLException, ParseException {
		final PreparedStatement prepareStatement = connection.prepareStatement(insertIntoCommand(entity, campos));
		for (List<String> valoresLinha : listaDeValores) {
			final int camposSize = campos.size();
			for (int indexValor = 0; indexValor < camposSize; indexValor++) {
				final CampoImport campoImport = campos.get(indexValor);
				switch (campoImport.getTipoCampo().getId()) {
					case TipoCampo.TIPO_INTEIRO:
						prepareStatement.setLong(indexValor + 1, Long.parseLong(valoresLinha.get(indexValor)));
						break;
					case TipoCampo.TIPO_TEXTO:
						prepareStatement.setString(indexValor + 1, valoresLinha.get(indexValor));
						break;
					case TipoCampo.TIPO_DATA:
					{
						final Calendar cal = Calendar.getInstance();
						cal.setTime(new SimpleDateFormat(campoImport.getPattern()).parse(valoresLinha.get(indexValor)));
						prepareStatement.setTimestamp(indexValor + 1, new Timestamp(cal.getTimeInMillis()));
						break;
					}
					default:
						prepareStatement.setObject(indexValor, valoresLinha.get(indexValor));
						break;
				}
			}
			prepareStatement.addBatch();
		}
		return prepareStatement;
	}

	protected String getTableCatalog(TabelaConfig entity) {
		return "tabulador";
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
}
