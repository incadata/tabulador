package br.gov.inca.tabulador.domain.result.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import br.gov.inca.tabulador.domain.entity.config.ValorCampoConfig;
import br.gov.inca.tabulador.domain.vo.CampoResult;

public class ConsultaDinamicaResult implements Serializable {
	private static final long serialVersionUID = 301712032133137365L;

	private List<CampoResult> columns;
	private List<Map<CampoResult, Object>> lines;

	public ConsultaDinamicaResult() {
		init();
	}
	
	@PostConstruct
	public void init() {
		setColumns(new ArrayList<>());
		setLines(new ArrayList<>());
	}
	
	public List<CampoResult> getColumns() {
		return columns;
	}
	
	public void setColumns(List<CampoResult> columns) {
		this.columns = columns;
	}

	public List<Map<CampoResult, Object>> getLines() {
		return lines;
	}

	public void setLines(List<Map<CampoResult, Object>> linesList) {
		this.lines = linesList;
	}
	
	public Object convertValor(CampoResult campo, Object valor) {
		if (campo.getValores().isEmpty() || !(valor instanceof Number)) {
			return valor;
		}
		final ValorCampoConfig valorCampoConfig = new ValorCampoConfig();
		valorCampoConfig.setDescricao(valor.toString());
		final Integer valorInt = ((Long) valor).intValue();
		return String.format("%s - %s", valor, campo.getValores().stream().
				filter(x -> x.getCodigo().equals(valorInt)).
				findAny().orElse(valorCampoConfig).getDescricao());
	}
}
