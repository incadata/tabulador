package br.gov.inca.tabulador.web.bean.consulta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.ValorCampoConfig;

public class GerarConsultaResultado implements Serializable {
	private static final long serialVersionUID = 301712032133137365L;

	private List<CampoConfig> columns;
	private List<Map<CampoConfig, Object>> lines;

	@PostConstruct
	public void init() {
		setColumns(new ArrayList<>());
		setLines(new ArrayList<>());
	}
	
	public List<CampoConfig> getColumns() {
		return columns;
	}
	
	public void setColumns(List<CampoConfig> columns) {
		this.columns = columns;
	}

	public List<Map<CampoConfig, Object>> getLines() {
		return lines;
	}

	public void setLines(List<Map<CampoConfig, Object>> linesList) {
		this.lines = linesList;
	}
	
	public Object convertValor(CampoConfig campo, Object valor) {
		if (campo.getValores().isEmpty() || !(valor instanceof Number)) {
			return valor;
		}
		final ValorCampoConfig valorCampoConfig = new ValorCampoConfig();
		valorCampoConfig.setDescricao(valor.toString());
		final Integer valorInt = ((Long) valor).intValue();
		return campo.getValores().stream().
				filter(x -> x.getCodigo().equals(valorInt)).
				findAny().orElse(valorCampoConfig).getDescricao();
	}
}
