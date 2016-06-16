package br.gov.inca.tabulador.domain.db;

import java.io.Serializable;

import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.domain.entity.tipo.TipoCampo;

public class StatementBuilder implements Serializable {
	private static final long serialVersionUID = 2437552777255361630L;

	public String createTable(TabelaConfig entity) {
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
		if (entity.getLocalidade()) {
			// TODO Adicionar campos de localidade
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
	
	public String insertInto(TabelaConfig entity) {
		//final StringBuilder stringBuilder = new StringBuilder(String.format(
		return null;
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
