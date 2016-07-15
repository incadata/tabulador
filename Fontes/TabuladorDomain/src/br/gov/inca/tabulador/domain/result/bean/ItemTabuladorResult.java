package br.gov.inca.tabulador.domain.result.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.gov.inca.tabulador.domain.vo.FiltroConsultaTabulador;
import br.gov.inca.tabulador.util.NumberUtil;
import br.gov.inca.tabulador.util.StringUtils;

public class ItemTabuladorResult {
	
	private String codigo;
	
	private String descricao;
	
	private String cor;
	
	private String geopathVar;
	
	private String bounds;
	
	private boolean selecionadoMapa = false;
	
	
	private Long valorAbsoluto = 0L;
	
	private Long totalPopulacao = 0L;
	
	private BigDecimal percentual;
	
	private List<ItemTabuladorResult> colunas;
	
	private FiltroConsultaTabulador filtro;
	
	public ItemTabuladorResult() {
	}
	
	public ItemTabuladorResult(String codigo, String descricao, FiltroConsultaTabulador filtro) {
		this.codigo = codigo;
		this.descricao = StringUtils.trim(descricao);
		this.filtro = filtro;
	}
	
	public ItemTabuladorResult(String codigo, String descricao, FiltroConsultaTabulador filtro, Object valor) {
		this.codigo = codigo;
		this.descricao = StringUtils.trim(descricao);
		this.filtro = filtro;
		this.valorAbsoluto = NumberUtil.toLong(valor);
	}


	public ItemTabuladorResult(String codigo, String descricao, String cor, FiltroConsultaTabulador filtro) {
		this.codigo = codigo;
		this.descricao = StringUtils.trim(descricao);
		this.cor = cor;
		this.filtro = filtro;
	}

	public ItemTabuladorResult(String codigo, FiltroConsultaTabulador filtro) {
		this.codigo = codigo;
		this.filtro = filtro;
	}
	
/*	public ItemTabuladorVo(ItemMapaVo itemMapa, FiltroConsultaTabulador filtro) {
		this.codigo = itemMapa.getCodigo().toString();
		this.descricao = StringUtils.trim(itemMapa.getNome());
		this.geopathVar = itemMapa.getGeopathVar();
		this.geoJson = itemMapa.getGeoJson();
		this.bounds = itemMapa.getBounds();
		this.filtro = filtro;
	}*/

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getValorAbsoluto() {
		return valorAbsoluto;
	}

	public void setValorAbsoluto(Long valorAbsoluto) {
		this.valorAbsoluto = valorAbsoluto;
	}
	
	public void addValorAbsoluto(Long valor){
		valorAbsoluto += valor;
	}

	public BigDecimal getPercentual() {
		return percentual;
	}

	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}

	public List<ItemTabuladorResult> getColunas() {
		return colunas;
	}

	public void setColunas(List<ItemTabuladorResult> colunas) {
		this.colunas = colunas;
	}

	public ItemTabuladorResult getColuna(String codigo) {
		if (colunas == null) colunas = new ArrayList<ItemTabuladorResult>();
		
		ItemTabuladorResult coluna = colunas.stream().filter(x -> x.codigo != null ? x.codigo.equals(codigo) : null).findFirst().get();
		if (coluna == null){
			coluna = new ItemTabuladorResult(codigo, codigo, filtro);
			colunas.add(coluna);
		}
		return coluna;
	}
	
	public void calcularPercentual(){
		for (ItemTabuladorResult col: colunas){
			col.setPercentual(new BigDecimal((col.getValorAbsoluto().doubleValue() / valorAbsoluto.doubleValue()) * 100D));
		}
		percentual = new BigDecimal(100D);
	}
	
	public void calcularPercentual(double total){
		percentual = new BigDecimal((valorAbsoluto.doubleValue() / total) * 100D);
	}
	
	public Long getTotalPopulacao() {
		return totalPopulacao;
	}

	public void setTotalPopulacao(Long totalPopulacao) {
		this.totalPopulacao = totalPopulacao;
	}
	
	public void addTotalPopulacao(Long totalPopulacao){
		this.totalPopulacao += totalPopulacao;
	}
	
	public Long getTotalValorAbsoluto(){
		Long total = 0L;
		if (colunas != null)
			for (ItemTabuladorResult col: colunas)
				total += col.getValorAbsoluto();
		
		return total;
	}

	public FiltroConsultaTabulador getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroConsultaTabulador filtro) {
		this.filtro = filtro;
	}
	
	public Double getValor(){
	//	if (filtro.isResultadoValorAbsoluto())
			return valorAbsoluto.doubleValue();
		/*else if (filtro.isResultadoPercentual())
			return percentual != null ? percentual.doubleValue() : 0.0;
		else
			return null;*/
	}
	
	public Double getValorMapa(){
		return NumberUtil.arredondar(getValor(), 2);
	}

	public String getValorFormatado(){
//		if (filtro.isResultadoValorAbsoluto())
			return valorAbsoluto.toString();
//		else if (filtro.isResultadoPercentual())
//			return Formatter.doubleToString(percentual.doubleValue(), "#.##");
//		else if (filtro.isResultadoTaxaBruta())
//			return Formatter.doubleToString(taxaBruta.doubleValue(), "#,###.00");
//		else if (filtro.isResultadoTaxaAjustada())
//			return Formatter.doubleToString(taxaAjustada.calcularTaxa(), "#,###.00");
//		else
//			return "";
	}

	public boolean isSelecionadoMapa() {
		return selecionadoMapa;
	}

	public void setSelecionadoMapa(boolean selecionadoMapa) {
		this.selecionadoMapa = selecionadoMapa;
	}

	public String getBounds() {
		return bounds;
	}

	public void setBounds(String bounds) {
		this.bounds = bounds;
	}

	public String getGeopathVar() {
		return geopathVar;
	}

	public void setGeopathVar(String geopathVar) {
		this.geopathVar = geopathVar;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemTabuladorResult other = (ItemTabuladorResult) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	public void addColuna(ItemTabuladorResult itemTabuladorResult) {
		if (colunas == null) colunas = new ArrayList<ItemTabuladorResult>();
		colunas.add(itemTabuladorResult);
	}

	public void addValorColuna(String codigo, Object valor) {
		ItemTabuladorResult itemColuna = colunas.stream().filter(x -> x.getCodigo().equals(codigo)).findFirst().get();
		itemColuna.valorAbsoluto = NumberUtil.toLong(valor);
	}
}
