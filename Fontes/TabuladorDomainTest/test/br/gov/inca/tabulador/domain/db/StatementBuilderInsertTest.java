package br.gov.inca.tabulador.domain.db;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import br.gov.inca.tabulador.domain.entity.config.CampoImport;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;

public class StatementBuilderInsertTest extends StatementBuilder {
	private static final long serialVersionUID = -7504736610149793712L;

	@Test
	public void insertIntoCommandUmCampo() {
		final TabelaConfig entity = new TabelaConfig();
		entity.setId(1);
		entity.setNome("tabela1");
		
		final List<CampoImport> campos = new ArrayList<>();
		campos.add(new CampoImport());
		campos.get(0).setNome("campo01");
		
		final String insertIntoCommand = insertIntoCommand(entity, campos);
		
		Assert.assertNotNull(insertIntoCommand);
		Assert.assertEquals(insertIntoCommand, String.format("INSERT INTO %s (%s) VALUES (?)", getTableName(entity), getFieldName(campos.get(0))));
	}

	@Test
	public void insertIntoCommandDoisCampos() {
		final TabelaConfig entity = new TabelaConfig();
		entity.setId(1);
		entity.setNome("tabela1");
		
		final List<CampoImport> campos = new ArrayList<>();
		campos.add(new CampoImport());
		campos.get(0).setNome("campo01");
		campos.add(new CampoImport());
		campos.get(1).setNome("campo02");
		
		final String insertIntoCommand = insertIntoCommand(entity, campos);
		
		Assert.assertNotNull(insertIntoCommand);
		Assert.assertEquals(insertIntoCommand, String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", getTableName(entity), getFieldName(campos.get(0)), getFieldName(campos.get(1))));
	}
	
	@Test
	public void insertIntoCommandUmCampoDoisValores() {
		final TabelaConfig entity = new TabelaConfig();
		entity.setId(1);
		entity.setNome("tabela1");
		
		final List<CampoImport> campos = new ArrayList<>();
		campos.add(new CampoImport());
		campos.get(0).setNome("campo01");
		
		final String insertIntoCommand = insertIntoCommand(entity, campos);
		
		Assert.assertNotNull(insertIntoCommand);
		Assert.assertEquals(insertIntoCommand, String.format("INSERT INTO %s (%s) VALUES (?)", getTableName(entity), getFieldName(campos.get(0))));
	}

	@Test
	public void insertIntoCommandDoisCamposDoisValores() {
		final TabelaConfig entity = new TabelaConfig();
		entity.setId(1);
		entity.setNome("tabela1");
		
		final List<CampoImport> campos = new ArrayList<>();
		campos.add(new CampoImport());
		campos.get(0).setNome("campo01");
		campos.add(new CampoImport());
		campos.get(1).setNome("campo02");
		
		final String insertIntoCommand = insertIntoCommand(entity, campos);
		
		Assert.assertNotNull(insertIntoCommand);
		Assert.assertEquals(insertIntoCommand, String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", getTableName(entity), getFieldName(campos.get(0)), getFieldName(campos.get(1))));
	}
}
