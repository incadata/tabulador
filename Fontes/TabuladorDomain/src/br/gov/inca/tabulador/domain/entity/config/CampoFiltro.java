package br.gov.inca.tabulador.domain.entity.config;


public class CampoFiltro extends CampoConfig {
	private static final long serialVersionUID = -3857590020028288112L;

	private String value;

	public CampoFiltro() {
		super();
	}

	public CampoFiltro(CampoConfig campo) {
		super(campo);
	}

	public CampoFiltro(CampoFiltro campo) {
		this((CampoConfig) campo);
		setValue(campo.getValue());
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}