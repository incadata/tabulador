package br.gov.inca.tabulador.domain.entity.config;


public class CampoImport extends CampoConfig {
	private static final long serialVersionUID = -1011397846544532181L;
	private boolean aspas;
	private boolean ignore;

	public CampoImport() {
		super();
	}

	public CampoImport(CampoConfig campo) {
		super(campo);
	}

	public CampoImport(CampoImport campo) {
		this((CampoConfig) campo);
		setAspas(campo.isAspas());
	}

	public boolean isAspas() {
		return aspas;
	}

	public void setAspas(boolean aspas) {
		this.aspas = aspas;
	}

	public boolean isIgnore() {
		return ignore;
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}
}