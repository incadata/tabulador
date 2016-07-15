package br.gov.inca.tabulador.domain.vo;

import java.util.ArrayList;
import java.util.List;

import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.domain.enumeration.FormatoSaida;

public class FiltroConsultaTabulador {
	
	private TabelaConfig tabelaConfig;
	private List<CampoFiltro> camposFiltro;
	private CampoConfig campoLinha;
	private CampoConfig campoColuna;
	private FormatoSaida formatoSaida;
	
	public FiltroConsultaTabulador() {
	}

	public TabelaConfig getTabelaConfig() {
		return tabelaConfig;
	}

	public void setTabelaConfig(TabelaConfig tabelaConfig) {
		this.tabelaConfig = tabelaConfig;
	}

	public List<CampoFiltro> getCamposFiltro() {
		return camposFiltro;
	}

	public void setCamposFiltro(List<CampoFiltro> campos) {
		this.camposFiltro = campos;
	}

	public CampoConfig getCampoLinha() {
		return campoLinha;
	}

	public void setCampoLinha(CampoConfig campoLinha) {
		this.campoLinha = campoLinha;
	}

	public CampoConfig getCampoColuna() {
		return campoColuna;
	}

	public void setCampoColuna(CampoConfig campoColuna) {
		this.campoColuna = campoColuna;
	}
	
	public boolean hasCampoColuna(){
		return campoColuna != null;
	}

	public FormatoSaida getFormatoSaida() {
		return formatoSaida;
	}

	public void setFormatoSaida(FormatoSaida formatoSaida) {
		this.formatoSaida = formatoSaida;
	}

	public List<CampoConfig> getCamposAgrupamento() {
		List<CampoConfig> camposAgrupamento = new ArrayList<CampoConfig>();
		camposAgrupamento.add(campoLinha);
		if (campoColuna != null) camposAgrupamento.add(campoColuna);
		return camposAgrupamento;
	}
	
}
