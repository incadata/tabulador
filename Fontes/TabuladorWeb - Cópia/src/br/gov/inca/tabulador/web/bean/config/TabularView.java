package br.gov.inca.tabulador.web.bean.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.gov.inca.tabulador.domain.ValidationException;
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
			showError(getMessages().getString("to_table_max_fields_title"), getMessages().getString("to_table_max_fields_msg"));
		} else {
			super.addCampoAgrupar();
		}
	}

	@Override
	public void tabular() {
		try {
			super.tabular();
		} catch (ValidationException e) {
<<<<<<< HEAD
			showError(getMessages().getString("to_table_error_title"), e.getMessage());
=======
			showError("Erro ao gerar tabulação", e.getMessage());
>>>>>>> branch 'master' of https://github.com/incadata/tabulador.git
		}
		if (isConverter()) {
			setResultado(converterResultado(getResultado()));
		}
	}

	// TODO Pode fazer o processamento no banco para melhorar a performance
	protected GerarConsultaResultado converterResultado(GerarConsultaResultado resultado) {
		if (resultado.getColumns().size() == 3) {
			// Colunas na tabela original que irão compor a nova tabela
			final CampoConfig linha = resultado.getColumns().get(0); 
			final CampoConfig coluna = resultado.getColumns().get(1); 
			final CampoConfig total = resultado.getColumns().get(2); 

			// Todos os valores disponíveis para linha e coluna
			final Set<Object> linhaValoresSet = new TreeSet<>();
			final Set<Object> colunaValoresSet = new TreeSet<>();
			for (Map<CampoConfig, Object> map : resultado.getLines()) {
				linhaValoresSet.add(map.get(linha));
				colunaValoresSet.add(map.get(coluna));
			}

			// Mapeamento de linha, coluna e valor. Tabela de duas dimensões.
			final Map<Object, Map<Object, Object>> valoresTabulado = new TreeMap<Object, Map<Object, Object>>();
			for (Object linhaValor : linhaValoresSet) {
				valoresTabulado.put(linhaValor, new TreeMap<Object, Object>());
			}
			for (Map<CampoConfig, Object> map : resultado.getLines()) {
				valoresTabulado.get(map.get(linha)).put(map.get(coluna), map.get(total));
			}

			// Tabela com a resposta
			final GerarConsultaResultado resposta = new GerarConsultaResultado();
			resposta.init();
			// Campo que formará a primeira coluna
			final CampoConfig linhaColuna = new CampoConfig();
			linhaColuna.setId(-1);
			linhaColuna.setNome(String.format("%s", linha.getLabelOrNome()));
			linhaColuna.setValores(linha.getValores());
			resposta.getColumns().add(linhaColuna);
			// Colunas que serão formadas pelos valroes da tabela
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
			final String keyTotal = "__Total___Index%";
			final String labelTotal = "Total";
			{
				// Coluna que irá conter o total das linhas
				final CampoConfig campoConfigTotal = new CampoConfig();
				campoConfigTotal.setId(--index);
				campoConfigTotal.setNome(labelTotal);
				resposta.getColumns().add(campoConfigTotal);
				colunaCategoria.put(keyTotal, campoConfigTotal);
			}

			final Map<Object, Long> totaisColunas = new HashMap<>();
			for (Object linhaValor : linhaValoresSet) {
				final HashMap<CampoConfig, Object> linhaAtual = new HashMap<>();
				linhaAtual.put(linhaColuna, linhaValor);
				long valorTotal = 0L;
				for (Object colunaValor : colunaValoresSet) {
					final Object valorAtual = valoresTabulado.get(linhaValor).get(colunaValor);
					linhaAtual.put(colunaCategoria.get(colunaValor), valorAtual);
					final long valorAtualLong = convetValorAtual(valorAtual);
					valorTotal += valorAtualLong;
					totaisColunas.put(colunaValor, totaisColunas.getOrDefault(colunaValor, 0L) + valorAtualLong);
				}
				linhaAtual.put(colunaCategoria.get(keyTotal), valorTotal);

				resposta.getLines().add(linhaAtual);
			}
			{
				final HashMap<CampoConfig, Object> linhaTotal = new HashMap<>();
				linhaTotal.put(linhaColuna, labelTotal);
				long valorTotal = 0L;
				for (Object colunaValor : colunaValoresSet) {
					final long valorAtualLong = totaisColunas.getOrDefault(colunaValor, 0L);
					linhaTotal.put(colunaCategoria.get(colunaValor), valorAtualLong);
					valorTotal += valorAtualLong;
				}
				linhaTotal.put(colunaCategoria.get(keyTotal), valorTotal);
				resposta.getLines().add(linhaTotal);
			}

			return resposta;
		}
		return resultado;
	}
	
	public long convetValorAtual(Object valorAtual) {
		if (valorAtual instanceof Integer || Integer.class.isInstance(valorAtual)) {
			return  (Integer) valorAtual; 
		} else if (valorAtual instanceof Long) {
			return (Long) valorAtual; 
		} else if ((valorAtual instanceof String) && ((String) valorAtual).matches("\\d+")) {
			return Long.parseLong((String) valorAtual);
		}
		return 0L;
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
