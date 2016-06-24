package br.gov.inca.tabulador.web.bean.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.web.bean.consulta.GerarConsultaResultado;
import br.gov.inca.tabulador.web.bean.consulta.GerarConsultaView;

@Named
@ViewScoped
public class TabularView extends GerarConsultaView {
	private static final long serialVersionUID = 2223875497601238537L;

	private boolean converter; 

	@Override
	public void addCampoAgrupar() {
		if (isConverter() && getCamposAgrupar().size() == 2) {
			showError("", "O máximo de agrupamentos para tabulação é de 2 campos, sendo linha e coluna");
		} else {
			super.addCampoAgrupar();
		}
	}

	@Override
	public void tabular() {
		super.tabular();
		if (isConverter()) {
			setResultado(converterResultado(getResultado()));
		}
	}

	// TODO Pode fazer o processamento no banco para melhorar a performance
	protected GerarConsultaResultado converterResultado(GerarConsultaResultado resultado) {
		if (resultado.getColumns().size() == 3) {
			final CampoConfig linha = resultado.getColumns().get(0); 
			final CampoConfig coluna = resultado.getColumns().get(1); 
			final CampoConfig total = resultado.getColumns().get(2); 

			final Set<Object> linhaValoresSet = new TreeSet<>();
			final Set<Object> colunaValoresSet = new TreeSet<>();
			for (Map<CampoConfig, Object> map : resultado.getLines()) {
				linhaValoresSet.add(map.get(linha));
				colunaValoresSet.add(map.get(coluna));
			}

			final Map<Object, Map<Object, Object>> valoresTabulado = new TreeMap<Object, Map<Object, Object>>();
			for (Object linhaValor : linhaValoresSet) {
				valoresTabulado.put(linhaValor, new TreeMap<Object, Object>());
			}
			for (Map<CampoConfig, Object> map : resultado.getLines()) {
				valoresTabulado.get(map.get(linha)).put(map.get(coluna), map.get(total));
			}

			final GerarConsultaResultado resposta = new GerarConsultaResultado();
			resposta.init();
			final CampoConfig linhaColuna = new CampoConfig();
			linhaColuna.setId(-1);
			linhaColuna.setNome(String.format("%s / %s", linha.getLabelOrNome(), coluna.getLabelOrNome()));
			linhaColuna.setValores(linha.getValores());
			resposta.getColumns().add(linhaColuna);
			final Map<Object, CampoConfig> colunaCategoria = new HashMap<Object, CampoConfig>();
			int index = -1;
			for (Object colunaValor : colunaValoresSet) {
				final CampoConfig campoConfig = new CampoConfig();
				campoConfig.setId(--index);
				campoConfig.setNome(colunaValor.toString());
				campoConfig.setLabel(resposta.convertValor(coluna, colunaValor).toString());
				colunaCategoria.put(colunaValor, campoConfig);
				resposta.getColumns().add(campoConfig);
			}
			for (Object linhaValor : linhaValoresSet) {
				final HashMap<CampoConfig, Object> linhaAtual = new HashMap<>();
				linhaAtual.put(linhaColuna, linhaValor);
				for (Object colunaValor : colunaValoresSet) {
					linhaAtual.put(colunaCategoria.get(colunaValor), valoresTabulado.get(linhaValor).get(colunaValor));
				}

				resposta.getLines().add(linhaAtual);
			}

			return resposta;
		}
		return resultado;
	}
	
	public boolean isConverter() {
		return converter;
	}
	
	public void setConverter(boolean converter) {
		this.converter = converter;
	}
	
	public GerarConsultaResultado getResultado() {
		return super.getResultado();
	}
}
