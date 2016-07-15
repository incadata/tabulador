package br.gov.inca.tabulador.web.bean.config;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.gov.inca.tabulador.domain.ValidationException;
import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.domain.enumeration.FormatoSaida;
import br.gov.inca.tabulador.domain.result.bean.ConsultaDinamicaResult;
import br.gov.inca.tabulador.domain.result.bean.ItemTabuladorResult;
import br.gov.inca.tabulador.domain.service.TabuladorService;
import br.gov.inca.tabulador.domain.vo.CampoFiltro;
import br.gov.inca.tabulador.domain.vo.FiltroConsultaTabulador;
import br.gov.inca.tabulador.web.bean.consulta.GerarConsultaView;

@Named
@ViewScoped
public class TabularView extends GerarConsultaView {
	private static final long serialVersionUID = 2223875497601238537L;
	
	@Inject
	private TabuladorService tabuladorService;

	private List<FormatoSaida> listFormatosSaida;
	
	private List<ItemTabuladorResult> resultadoTabulador;
	
	FiltroConsultaTabulador filtro;

	@PostConstruct
	public void init() {
		filtro = new FiltroConsultaTabulador();
		filtro.setTabelaConfig(new TabelaConfig());
		filtro.setFormatoSaida(FormatoSaida.TABELA);
		filtro.setCamposFiltro(new ArrayList<CampoFiltro>());

		setResultadosPorPagina(10);
	}
	
	@Override
	public void findById(Integer id) {
		super.findById(id);
		filtro.setTabelaConfig(super.getTabelaConfig());
	}
	
	@Override
	public void tabular() {
		try {
			resultadoTabulador = tabuladorService.gerarTabulador(filtro);
			clearMessages();
			showInfo(null, String.format(getMessages().getString("n_lines_found"), getResultadoTabulador().size()));
		} catch (ValidationException e) {
			showError(getMessages().getString("to_table_error_title"), e.getMessage());
		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, getMessages().getString("to_table_error_title"), e);
			showError(getMessages().getString("to_table_error_title"), e.getMessage());
		}
	}

	/*protected ConsultaDinamicaResult converterResultado(ConsultaDinamicaResult resultado) {
		if (resultado.getColumns().size() == 3) {
			// Colunas na tabela original que irão compor a nova tabela
			final CampoConfig linha = resultado.getColumns().get(0); 
			final CampoConfig coluna = resultado.getColumns().get(1); 
			final CampoConfig total = resultado.getColumns().get(2); 

			// Todos os valores disponíveis para linha e coluna
			final Set<Object> linhaValoresSet = new HashSet<>();
			final Set<Object> colunaValoresSet = new HashSet<>();
			for (Map<CampoConfig, Object> map : resultado.getLines()) {
				final Object linhaValor = map.getOrDefault(linha, "");
				linhaValoresSet.add(linhaValor != null ? linhaValor : "");
				final Object colunaValor = map.getOrDefault(coluna, "");
				colunaValoresSet.add(colunaValor != null ? colunaValor : "");
			}

			// Mapeamento de linha, coluna e valor. Tabela de duas dimensões.
			final Map<Object, Map<Object, Object>> valoresTabulado = new HashMap<Object, Map<Object, Object>>();
			for (Object linhaValor : linhaValoresSet) {
				valoresTabulado.put(linhaValor, new HashMap<Object, Object>());
			}
			for (Map<CampoConfig, Object> map : resultado.getLines()) {
				final Object linhaValor = map.getOrDefault(linha, "");
				final Object colunaValor = map.getOrDefault(coluna, "");
				valoresTabulado.get(linhaValor != null ? linhaValor : "").put(colunaValor != null ? colunaValor : "", map.get(total));
			}

			// Tabela com a resposta
			final ConsultaDinamicaResult resposta = new ConsultaDinamicaResult();
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
	}*/
	
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
	
	public ConsultaDinamicaResult getResultado() {
		return null;
	}

	public List<FormatoSaida> getListFormatosSaida() {
		if (listFormatosSaida == null)
			listFormatosSaida = FormatoSaida.list();
		return listFormatosSaida;
	}
	
	public List<ItemTabuladorResult> getResultadoTabulador() {
		return resultadoTabulador;
	}
	
	public FiltroConsultaTabulador getFiltro() {
		return filtro;
	}
	
	public void setFiltro(FiltroConsultaTabulador filtro) {
		this.filtro = filtro;
	}
	
	@Override
	public List<CampoFiltro> getCampos() {
		return filtro.getCamposFiltro();
	}
	
	@Override
	public void setCampos(List<CampoFiltro> campos) {
		filtro.setCamposFiltro(campos);
	}
	
	@Override
	public List<CampoConfig> getCamposAgrupar() {
		return filtro.getCamposAgrupamento();
	}
	
	@Override
	public TabelaConfig getTabelaConfig() {
		return filtro.getTabelaConfig();
	}

}

