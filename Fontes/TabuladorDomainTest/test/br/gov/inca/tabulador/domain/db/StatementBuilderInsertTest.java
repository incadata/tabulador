package br.gov.inca.tabulador.domain.db;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.domain.sql.StatementBuilder;
import br.gov.inca.tabulador.domain.vo.CampoImport;

public class StatementBuilderInsertTest extends StatementBuilder {
	private static final long serialVersionUID = -7504736610149793712L;

	@Test
	public void insertIntoCommandUmCampo() {
		final TabelaConfig entity = new TabelaConfig();
		entity.setId(1);
		entity.setNome("tabela1");
		
		final List<CampoImport> campos = new ArrayList<>();
		campos.add(new CampoImport());
		campos.get(0).getCampo().setNome("campo01");
		
		final String insertIntoCommand = insertIntoCommand(entity, campos);
		
		Assert.assertNotNull(insertIntoCommand);
		Assert.assertEquals(insertIntoCommand, String.format("INSERT INTO %s (%s) VALUES (?)", getTableName(entity), getFieldName(campos.get(0).getCampo())));
	}

	@Test
	public void insertIntoCommandDoisCampos() {
		final TabelaConfig entity = new TabelaConfig();
		entity.setId(1);
		entity.setNome("tabela1");
		
		final List<CampoImport> campos = new ArrayList<>();
		campos.add(new CampoImport());
		campos.get(0).getCampo().setNome("campo01");
		campos.add(new CampoImport());
		campos.get(1).getCampo().setNome("campo02");
		
		final String insertIntoCommand = insertIntoCommand(entity, campos);
		
		Assert.assertNotNull(insertIntoCommand);
		Assert.assertEquals(insertIntoCommand, String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", getTableName(entity), getFieldName(campos.get(0).getCampo()), getFieldName(campos.get(1).getCampo())));
	}
	
	@Test
	public void insertIntoCommandUmCampoDoisValores() {
		final TabelaConfig entity = new TabelaConfig();
		entity.setId(1);
		entity.setNome("tabela1");
		
		final List<CampoImport> campos = new ArrayList<>();
		campos.add(new CampoImport());
		campos.get(0).getCampo().setNome("campo01");
		
		final String insertIntoCommand = insertIntoCommand(entity, campos);
		
		Assert.assertNotNull(insertIntoCommand);
		Assert.assertEquals(insertIntoCommand, String.format("INSERT INTO %s (%s) VALUES (?)", getTableName(entity), getFieldName(campos.get(0).getCampo())));
	}

	@Test
	public void insertIntoCommandDoisCamposDoisValores() {
		final TabelaConfig entity = new TabelaConfig();
		entity.setId(1);
		entity.setNome("tabela1");
		
		final List<CampoImport> campos = new ArrayList<>();
		campos.add(new CampoImport());
		campos.get(0).getCampo().setNome("campo01");
		campos.add(new CampoImport());
		campos.get(1).getCampo().setNome("campo02");
		
		final String insertIntoCommand = insertIntoCommand(entity, campos);
		
		Assert.assertNotNull(insertIntoCommand);
		Assert.assertEquals(insertIntoCommand, String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", getTableName(entity), getFieldName(campos.get(0).getCampo()), getFieldName(campos.get(1).getCampo())));
	}
}
