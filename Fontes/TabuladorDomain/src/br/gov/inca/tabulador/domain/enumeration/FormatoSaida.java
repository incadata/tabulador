package br.gov.inca.tabulador.domain.enumeration;

public enum FormatoSaida {
	
	TABELA(1, "tabulador.saida.tabela"),
	GRAFICO_LINHA(2, "tabulador.saida.grafico.linha"),
	GRAFICO_BARRA(3, "tabulador.saida.grafico.barra");
	
	public Integer id;
	public String descricao;
	
	FormatoSaida(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	

}
