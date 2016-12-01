package br.gov.inca.tabulador.web.bean.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import br.gov.inca.tabulador.domain.result.bean.ConsultaDinamicaResult;
import br.gov.inca.tabulador.domain.vo.CampoResult;

public class TabularViewTest extends TabularView {
	private static final long serialVersionUID = -7537093244422598492L;

	@Test
	@Ignore
	public void converterResultadoVazio() {
		final ConsultaDinamicaResult resultado = new ConsultaDinamicaResult();
		resultado.setColumns(new ArrayList<>());
		resultado.setLines(new ArrayList<>());

		/*
		final ConsultaDinamicaResult linhasResultado = converterResultado(resultado);
		Assert.assertNotNull(linhasResultado);
		Assert.assertTrue(linhasResultado.getColumns().isEmpty());
		Assert.assertTrue(linhasResultado.getLines().isEmpty());
		*/
	}

	@Test
	@Ignore
	public void converterResultadoSimples() {
		final ArrayList<CampoResult> colunas = new ArrayList<>();
		final CampoResult nome = new CampoResult();
		nome.setId(1);
		nome.setNome("Nome");
		colunas.add(nome);
		final CampoResult categoria = new CampoResult();
		categoria.setId(2);
		categoria.setNome("Categoria");
		colunas.add(categoria);
		final CampoResult total = new CampoResult();
		total.setId(0);
		total.setNome("Total");
		colunas.add(total);

		final ArrayList<Map<CampoResult, Object>> linhas = new ArrayList<>();
		final HashMap<CampoResult, Object> linha01 = new HashMap<>();
		linha01.put(nome, "Fulano");
		linha01.put(categoria, "1");
		linha01.put(total, 2);
		linhas.add(linha01);
		final HashMap<CampoResult, Object> linha02 = new HashMap<>();
		linha02.put(nome, "Beltrano");
		linha02.put(categoria, "3");
		linha02.put(total, 4);
		linhas.add(linha02);
		final HashMap<CampoResult, Object> linha03 = new HashMap<>();
		linha03.put(nome, "Beltrano");
		linha03.put(categoria, "2");
		linha03.put(total, 3);
		linhas.add(linha03);
		final HashMap<CampoResult, Object> linha04 = new HashMap<>();
		linha04.put(nome, "Fulano");
		linha04.put(categoria, "2");
		linha04.put(total, 3);
		linhas.add(linha04);
		
		final ConsultaDinamicaResult resultado = new ConsultaDinamicaResult();
		resultado.setColumns(colunas);
		resultado.setLines(linhas);
		/*
		final ConsultaDinamicaResult linhasResultado = converterResultado(resultado);
		Assert.assertNotNull(linhasResultado);
		Assert.assertFalse(linhasResultado.getColumns().isEmpty());
		// Coluna de total
		Assert.assertEquals(5, linhasResultado.getColumns().size());

		final CampoResult totalColuna = linhasResultado.getColumns().get(linhasResultado.getColumns().size() - 1);
		Assert.assertEquals("Total", totalColuna.getLabelOrNome());
		Assert.assertEquals(7L, linhasResultado.getLines().get(0).get(totalColuna)); 
		Assert.assertEquals(5L, linhasResultado.getLines().get(1).get(totalColuna)); 
		
		Assert.assertFalse(linhasResultado.getLines().isEmpty());
		Assert.assertEquals(3, linhasResultado.getLines().size());
		*/
	}
}
