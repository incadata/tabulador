package br.gov.inca.tabulador.domain.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import br.gov.inca.tabulador.domain.result.bean.ConsultaDinamicaResult;
import br.gov.inca.tabulador.domain.result.bean.ItemTabuladorResult;
import br.gov.inca.tabulador.domain.vo.CampoResult;
import br.gov.inca.tabulador.domain.vo.FiltroConsultaTabulador;
import br.gov.inca.tabulador.util.NumberUtil;
import br.gov.inca.tabulador.util.StringUtils;

@Named
public class TabuladorService implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2457115663099522780L;
	
	private static final String VAZIO = "(Vazio)";
	private @Inject ConsultaDinamicaService consultaDinamicaService;
	
	public List<ItemTabuladorResult> gerarTabulador(FiltroConsultaTabulador filtro){
		ConsultaDinamicaResult consultaResult = consultaDinamicaService.executarConsulta(filtro.getTabelaConfig(), filtro.getCamposFiltro(), filtro.getCamposAgrupamento());
		
		return converterParaTabulador(filtro, consultaResult);
	}
	
	public List<ItemTabuladorResult> converterParaTabulador(FiltroConsultaTabulador filtro, ConsultaDinamicaResult consultaResult){
		List<ItemTabuladorResult> resultadoTabulador = new ArrayList<ItemTabuladorResult>();
		Set<ItemTabuladorResult> colunas = new HashSet<ItemTabuladorResult>();

		CampoResult campoLinha = consultaResult.getColumns().get(0);
		CampoResult campoColuna = null;
		CampoResult campoValor = consultaResult.getColumns().get(consultaResult.getColumns().size()-1);
		
		try {
			if (consultaResult.getColumns().size() == 3){
				campoColuna = consultaResult.getColumns().get(1);
				for (Map<CampoResult, Object> mapLine: consultaResult.getLines()){
					Object valor = mapLine.get(campoColuna);
					if (valor == null) valor = VAZIO;
					ItemTabuladorResult item = new ItemTabuladorResult(valor.toString(), campoColuna.getDescricaoValor(valor), filtro);
					colunas.add(item);
				}
			}
			
			Map<Object, ItemTabuladorResult> mapItemLinha = new HashMap<Object, ItemTabuladorResult>();
			for (Map<CampoResult, Object> mapLine: consultaResult.getLines()){
				Object valorLinha = mapLine.get(campoLinha);
				if (valorLinha == null) valorLinha = VAZIO;
				ItemTabuladorResult itemLinha = mapItemLinha.get(campoLinha);
				if (itemLinha == null){
					itemLinha = new ItemTabuladorResult(valorLinha.toString(), campoLinha.getDescricaoValor(valorLinha), filtro);
					for (ItemTabuladorResult itemColuna: colunas)
						itemLinha.addColuna(new ItemTabuladorResult(itemColuna.getCodigo(), itemColuna.getDescricao(), filtro));
					mapItemLinha.put(valorLinha, itemLinha);
				}
				
				if (campoColuna != null) {
					String codigoColuna = StringUtils.safeToString(mapLine.get(campoColuna));
					if (codigoColuna == null) codigoColuna = VAZIO;
					itemLinha.addValorColuna(codigoColuna, mapLine.get(campoValor));
				} else
					itemLinha.setValorAbsoluto(NumberUtil.toLong(valorLinha));
					
			}
			
			for (ItemTabuladorResult item: mapItemLinha.values()){
				resultadoTabulador.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return resultadoTabulador;
	}

}
