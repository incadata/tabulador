package br.gov.inca.tabulador.domain.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.hibernate.QueryException;

import br.gov.inca.tabulador.domain.dao.config.CampoConfigDao;
import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.TabelaConfig;
import br.gov.inca.tabulador.domain.entity.config.ValorCampoConfig;
import br.gov.inca.tabulador.domain.qualifier.Messages;
import br.gov.inca.tabulador.domain.result.bean.ConsultaDinamicaResult;
import br.gov.inca.tabulador.domain.sql.StatementBuilder;
import br.gov.inca.tabulador.domain.vo.CampoFiltro;
import br.gov.inca.tabulador.domain.vo.CampoResult;

public class ConsultaDinamicaService implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6720391369910746967L;
	private @Inject CampoConfigDao campoConfigDao;
	private @Inject StatementBuilder statementBuilder;
	private transient @Inject Instance<Connection> connection;
	private @Inject @Messages ResourceBundle messages;
	
	public ConsultaDinamicaResult executarConsulta(TabelaConfig tabelaConfig, List<CampoFiltro> camposFiltro, List<CampoConfig> camposResultado) throws QueryException {
		final int sizeCamposAgrupar = camposResultado.size();
		
		ConsultaDinamicaResult resultado = new ConsultaDinamicaResult();
		for (int i = 0; i < sizeCamposAgrupar; i++) {
			CampoConfig campoAgrupar = camposResultado.get(i);
			camposResultado.set(i, campoConfigDao.findById(campoAgrupar.getId()).get());
			campoAgrupar = camposResultado.get(i);
			final List<ValorCampoConfig> valores = campoAgrupar.getValores();
			campoAgrupar.setValores(new ArrayList<>());
			for (ValorCampoConfig valor : valores) {
				campoAgrupar.getValores().add(valor);
			}
		}
		for (CampoFiltro campo : camposFiltro) {
			final CampoConfig campoDb = campoConfigDao.findById(campo.getCampo().getId()).get();
			campo.getCampo().setNome(campoDb.getNome());
			campo.getCampo().setTipoFiltro(campoDb.getTipoFiltro());
		}
		try (final Connection connectionLocal = connection.get();
				final StatementBuilder statementBuilderLocal = statementBuilder;
				final PreparedStatement ps = statementBuilderLocal.selectTabular(connectionLocal, tabelaConfig, camposResultado, camposFiltro);
				final ResultSet rs = ps.executeQuery()) {
			final List<CampoResult> resultColumns = resultado.getColumns();
			resultColumns.clear();
			final CampoResult countAsterisco = new CampoResult();
			countAsterisco.setId(0);
			countAsterisco.setNome("_count(*)__");
			countAsterisco.setLabel("Total");
			for (CampoConfig campoConfig: camposResultado)
				resultColumns.add(new CampoResult(campoConfig));
			final List<Map<CampoResult, Object>> resultLines = resultado.getLines();
			resultLines.clear();
			while (rs.next()) {
				final HashMap<CampoResult, Object> hashMap = new HashMap<>();
				hashMap.put(countAsterisco, rs.getLong(1));

				for (CampoConfig campo : camposResultado) {
					hashMap.put(new CampoResult(campo), campo.getTipoCampo().convert(rs.getObject(campo.getNome())));
				}

				resultLines.add(hashMap);
			}
			resultColumns.add(countAsterisco);
//			showInfo(null, String.format(messages.getString("n_lines_found"), contador));
//		} catch (SQLFeatureNotSupportedException e) {
//			if (e.getMessage().contains("free()")) {
//				Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e.getLocalizedMessage());
//			} else {
//				showError(e, messages.getString("insert_data_title"), messages.getString("insert_data_msg"));
//			}
		} catch (Exception e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, messages.getString("insert_data_msg"));
			throw new QueryException(messages.getString("insert_data_title"), messages.getString("insert_data_msg"), e);
		}
		return resultado;
	}

}
