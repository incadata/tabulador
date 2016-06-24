package br.gov.inca.tabulador.domain.entity.config;

import br.gov.inca.tabulador.domain.entity.tipo.TipoCampo;


public class CampoImport extends CampoConfig {
	public static final String DEFAULT_PATTERN = "dd/mm/yyyy";
	private static final long serialVersionUID = -1011397846544532181L;

	private boolean aspas;
	private boolean ignore;
	private String pattern;

	public CampoImport() {
		this(new CampoConfig());
	}

	public CampoImport(CampoConfig campo) {
		super(campo);
		setAspas(false);
		setIgnore(false);
		setPattern("");
	}

	public CampoImport(CampoImport campo) {
		this((CampoConfig) campo);
		setAspas(campo.isAspas());
		setIgnore(campo.isIgnore());
		if (getTipoCampo().getId() != null && TipoCampo.TIPO_DATA == getTipoCampo().getId()) {
			setPattern(DEFAULT_PATTERN);
		}
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

	public boolean getIgnore() {
		return isIgnore();
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}
	
	public String getPattern() {
		return pattern;
	}
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}