package br.gov.inca.tabulador.domain.db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	
	public PreparedStatement insertInto(Connection connection, TabelaConfig entity, List<CampoImport> campos, List<List<String>> listaDeValores) throws SQLException {
		final StringBuilder stringBuilder = new StringBuilder("INSERT INTO ");
		stringBuilder.append(getTableName(entity));
		stringBuilder.append(" (");
		boolean virgula = false;
		for (CampoImport campo : campos) {
			if (virgula) {
				stringBuilder.append(", ");
			}
			stringBuilder.append(getFieldName(campo));
			virgula = true;
		}
		stringBuilder.append(") VALUES (");
		virgula = false;
		final Map<Integer, String> valorMap = new HashMap<>();
		int valorIndex = 0;
		for (List<String> valores : listaDeValores) {
			if (virgula) {
				stringBuilder.append(", ");
			}
			stringBuilder.append("(");
			
			boolean virgulaInterna = false;
			for (String valor : valores) {
				if (virgulaInterna) {
					stringBuilder.append(", ");
				}
				stringBuilder.append("?");
				valorMap.put(++valorIndex, valor);
				virgulaInterna = true;
			}
			stringBuilder.append(")");
			virgula = true;
		}
		stringBuilder.append(")");

		final PreparedStatement prepareStatement = connection.prepareStatement(stringBuilder.toString());
		for (Entry<Integer, String> valorEntry : valorMap.entrySet()) {
			prepareStatement.setString(valorEntry.getKey(), valorEntry.getValue());
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
