package br.gov.inca.tabulador.web.bean.consulta;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.gov.inca.tabulador.domain.result.bean.ConsultaDinamicaResult;

@Named("consultaDinamicaView")
@ViewScoped
public class ConsultaDinamicaView extends GerarConsultaView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6388527311246978979L;
	
	@Override
	public ConsultaDinamicaResult getResultado() {
		return super.getResultado();
	}

}
