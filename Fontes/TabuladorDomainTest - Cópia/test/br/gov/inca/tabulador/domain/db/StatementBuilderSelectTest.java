package br.gov.inca.tabulador.domain.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.CampoFiltro;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.domain.entity.tipo.TipoFiltro;

public class StatementBuilderSelectTest extends StatementBuilder {
	private static final long serialVersionUID = -3697066093891617075L;

	@Test
	public void semAgrupadorSemFiltro() {
		final TabelaConfig entity = new TabelaConfig();
		entity.setId(1);
		
		final String selectTabular = selectTabular(entity, new ArrayList<>(), new ArrayList<>());
		
		Assert.assertNotNull(selectTabular);
		Assert.assertEquals(selectTabular, String.format("SELECT COUNT(*) FROM %s", getTableName(entity)));
	}

	@Test
	public void semAgrupadorComUmFiltro() {
		final TabelaConfig entity = new TabelaConfig();
		entity.setId(1);
		
		final List<CampoFiltro> camposFiltro = new ArrayList<>();
		camposFiltro.add(new CampoFiltro());
		camposFiltro.get(0).getCampo().setNome("filtro01");
		camposFiltro.get(0).getCampo().getTipoFiltro().setId(TipoFiltro.FILTRO_IGUAL);
		final String selectTabular = selectTabular(entity, Collections.emptyList(), camposFiltro);
		
		Assert.assertNotNull(selectTabular);
		Assert.assertEquals(selectTabular, String.format("SELECT COUNT(*) FROM %s WHERE %s = ?", getTableName(entity), getFieldName(camposFiltro.get(0).getCampo())));
	}

	@Test
	public void semAgrupadorComDoisFiltros() {
		final TabelaConfig entity = new TabelaConfig();
		entity.setId(1);
		
		final List<CampoFiltro> camposFiltro = new ArrayList<>();
		camposFiltro.add(new CampoFiltro());
		camposFiltro.get(0).getCampo().setNome("filtro01");
		camposFiltro.get(0).getCampo().getTipoFiltro().setId(TipoFiltro.FILTRO_IGUAL);
		camposFiltro.add(new CampoFiltro());
		camposFiltro.get(1).getCampo().setNome("filtro02");
		camposFiltro.get(1).getCampo().getTipoFiltro().setId(TipoFiltro.FILTRO_MAIOR);
		final String selectTabular = selectTabular(entity, Collections.emptyList(), camposFiltro);
		
		Assert.assertNotNull(selectTabular);
		Assert.assertEquals(selectTabular, String.format("SELECT COUNT(*) FROM %s WHERE %s = ? AND %s > ?", getTableName(entity), getFieldName(camposFiltro.get(0).getCampo()), getFieldName(camposFiltro.get(1).getCampo())));
	}

	@Test
	public void commUmAgrupadorSemFiltro() {
		final TabelaConfig entity = new TabelaConfig();
		entity.setId(1);
		
		final List<CampoConfig> camposAgrupar = new ArrayList<>();
		camposAgrupar.add(new CampoConfig());
		camposAgrupar.get(0).setNome("campo01");
		
		final String selectTabular = selectTabular(entity, camposAgrupar, Collections.emptyList());
		
		Assert.assertNotNull(selectTabular);
		Assert.assertEquals(selectTabular, String.format("SELECT COUNT(*), %s FROM %s GROUP BY %s", getFieldName(camposAgrupar.get(0)), getTableName(entity), getFieldName(camposAgrupar.get(0))));
	}

	@Test
	public void commUmAgrupadorComUmFiltro() {
		final TabelaConfig entity = new TabelaConfig();
		entity.setId(1);
		
		final List<CampoConfig> camposAgrupar = new ArrayList<>();
		camposAgrupar.add(new CampoConfig());
		camposAgrupar.get(0).setNome("campo01");

		final List<CampoFiltro> camposFiltro = new ArrayList<>();
		camposFiltro.add(new CampoFiltro());
		camposFiltro.get(0).getCampo().setNome("filtro01");
		camposFiltro.get(0).getCampo().getTipoFiltro().setId(TipoFiltro.FILTRO_IGUAL);
		
		final String selectTabular = selectTabular(entity, camposAgrupar, camposFiltro);
		
		Assert.assertNotNull(selectTabular);
		Assert.assertEquals(selectTabular, 
				String.format(
						"SELECT COUNT(*), %s FROM %s WHERE %s = ? GROUP BY %s", 
						getFieldName(camposAgrupar.get(0)),
						getTableName(entity),
						getFieldName(camposFiltro.get(0).getCampo()),
						getFieldName(camposAgrupar.get(0))));
	}

	@Test
	public void commUmAgrupadorComDoisFiltros() {
		final TabelaConfig entity = new TabelaConfig();
		entity.setId(1);
		
		final List<CampoConfig> camposAgrupar = new ArrayList<>();
		camposAgrupar.add(new CampoConfig());
		camposAgrupar.get(0).setNome("campo01");
		
		final List<CampoFiltro> camposFiltro = new ArrayList<>();
		camposFiltro.add(new CampoFiltro());
		camposFiltro.get(0).getCampo().setNome("filtro01");
		camposFiltro.get(0).getCampo().getTipoFiltro().setId(TipoFiltro.FILTRO_IGUAL);
		camposFiltro.add(new CampoFiltro());
		camposFiltro.get(1).getCampo().setNome("filtro02");
		camposFiltro.get(1).getCampo().getTipoFiltro().setId(TipoFiltro.FILTRO_MENOR);
		
		final String selectTabular = selectTabular(entity, camposAgrupar, camposFiltro);
		
		Assert.assertNotNull(selectTabular);
		Assert.assertEquals(selectTabular, 
				String.format(
						"SELECT COUNT(*), %s FROM %s WHERE %s = ? AND %s < ? GROUP BY %s", 
						getFieldName(camposAgrupar.get(0)),
						getTableName(entity),
						getFieldName(camposFiltro.get(0).getCampo()),
						getFieldName(camposFiltro.get(1).getCampo()),
						getFieldName(camposAgrupar.get(0))));
	}

	@Test
	public void commDoisAgrupadoresSemFiltro() {
		final TabelaConfig entity = new TabelaConfig();
		entity.setId(1);
		
		final List<CampoConfig> camposAgrupar = new ArrayList<>();
		camposAgrupar.add(new CampoConfig());
		camposAgrupar.get(0).setNome("campo01");
		camposAgrupar.add(new CampoConfig());
		camposAgrupar.get(1).setNome("campo02");
		
		final String selectTabular = selectTabular(entity, camposAgrupar, Collections.emptyList());
		
		Assert.assertNotNull(selectTabular);
		Assert.assertEquals(selectTabular, 
				String.format(
						"SELECT COUNT(*), %s, %s FROM %s GROUP BY %s, %s", 
						getFieldName(camposAgrupar.get(0)),
						getFieldName(camposAgrupar.get(1)),
						getTableName(entity),
						getFieldName(camposAgrupar.get(0)),
						getFieldName(camposAgrupar.get(1))));
	}

	@Test
	public void commDoisAgrupadoresComUmFiltro() {
		final TabelaConfig entity = new TabelaConfig();
		entity.setId(1);
		
		final List<CampoConfig> camposAgrupar = new ArrayList<>();
		camposAgrupar.add(new CampoConfig());
		camposAgrupar.get(0).setNome("campo01");
		camposAgrupar.add(new CampoConfig());
		camposAgrupar.get(1).setNome("campo02");
		
		final List<CampoFiltro> camposFiltro = new ArrayList<>();
		camposFiltro.add(new CampoFiltro());
		camposFiltro.get(0).getCampo().setNome("filtro01");
		camposFiltro.get(0).getCampo().getTipoFiltro().setId(TipoFiltro.FILTRO_IGUAL);
		
		final String selectTabular = selectTabular(entity, camposAgrupar, camposFiltro);
		
		Assert.assertNotNull(selectTabular);
		Assert.assertEquals(selectTabular, 
				String.format(
						"SELECT COUNT(*), %s, %s FROM %s WHERE %s = ? GROUP BY %s, %s", 
						getFieldName(camposAgrupar.get(0)),
						getFieldName(camposAgrupar.get(1)),
						getTableName(entity),
						getFieldName(camposFiltro.get(0).getCampo()),
						getFieldName(camposAgrupar.get(0)),
						getFieldName(camposAgrupar.get(1))));
	}

	@Test
	public void commDoisAgrupadoresComDoisFiltros() {
		final TabelaConfig entity = new TabelaConfig();
		entity.setId(1);
		
		final List<CampoConfig> camposAgrupar = new ArrayList<>();
		camposAgrupar.add(new CampoConfig());
		camposAgrupar.get(0).setNome("campo01");
		camposAgrupar.add(new CampoConfig());
		camposAgrupar.get(1).setNome("campo02");
		
		final List<CampoFiltro> camposFiltro = new ArrayList<>();
		camposFiltro.add(new CampoFiltro());
		camposFiltro.get(0).getCampo().setNome("filtro01");
		camposFiltro.get(0).getCampo().getTipoFiltro().setId(TipoFiltro.FILTRO_IGUAL);
		camposFiltro.add(new CampoFiltro());
		camposFiltro.get(1).getCampo().setNome("filtro02");
		camposFiltro.get(1).getCampo().getTipoFiltro().setId(TipoFiltro.FILTRO_DIFERENTE);
		
		final String selectTabular = selectTabular(entity, camposAgrupar, camposFiltro);
		
		Assert.assertNotNull(selectTabular);
		Assert.assertEquals(selectTabular, 
				String.format(
						"SELECT COUNT(*), %s, %s FROM %s WHERE %s = ? AND %s <> ? GROUP BY %s, %s", 
						getFieldName(camposAgrupar.get(0)),
						getFieldName(camposAgrupar.get(1)),
						getTableName(entity),
						getFieldName(camposFiltro.get(0).getCampo()),
						getFieldName(camposFiltro.get(1).getCampo()),
						getFieldName(camposAgrupar.get(0)),
						getFieldName(camposAgrupar.get(1))));
	}

}
