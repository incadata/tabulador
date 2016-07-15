package br.gov.inca.tabulador.domain.enumeration;

import java.util.Arrays;
import java.util.List;

public enum FormatoSaida {
	
	TABELA(1, "formato_saida_tabela", true, true),
	GRAFICO_LINHA(2, "formato_saida_grafico_linha", true, false),
	GRAFICO_BARRA(3, "formato_saida_grafico_barra", false, false),
	GRAFICO_PIZZA(4, "formato_saida_grafico_pizza", false, false);
	
	public Integer id;
	public String descricao;
	public boolean incluiColuna;
	public boolean incluiTotal;
	
	FormatoSaida(Integer id, String descricao, boolean incluiColuna, boolean incluiTotal) {
		this.id = id;
		this.descricao = descricao;
		this.incluiColuna = incluiColuna;
	}
	
	public Integer getId() {
		return id;
	}
	public String getDescricao() {
		return descricao;
	}
	
	public boolean isSomenteLinha() {
		return incluiColuna;
	}
	
	public boolean isIncluiTotal() {
		return incluiTotal;
	}

	public static List<FormatoSaida> list() {
		return Arrays.asList(values());
	}
	
	public boolean isTabela(){
		return TABELA.equals(this);
	}
	
	public boolean isGrafico(){
		return GRAFICO_LINHA.equals(this) || GRAFICO_BARRA.equals(this) || GRAFICO_PIZZA.equals(this);
	}
	
	public boolean isGraficoLinha(){
		return GRAFICO_LINHA.equals(this);
	}
	
	public boolean isGraficoBarra(){
		return GRAFICO_BARRA.equals(this);
	}
	
	public boolean isGraficoPizza(){
		return GRAFICO_PIZZA.equals(this);
	}

}
