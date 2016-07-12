package br.gov.inca.tabulador.web.entity;

import java.io.Serializable;

import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.tipo.TipoCampo;


public class CampoImport implements Serializable {
	public static final String DEFAULT_PATTERN = "dd/MM/yyyy";
	private static final long serialVersionUID = -1011397846544532181L;

	private boolean aspas;
	private boolean ignore;
	private String pattern;
	private CampoConfig campo;
	private int positionInFile;

	public CampoImport() {
		this(new CampoConfig());
	}

	public CampoImport(CampoConfig campo) {
		setCampo(campo);
		setAspas(false);
		setIgnore(false);
		if (getCampo().getTipoCampo().getId() != null && TipoCampo.TIPO_DATA == getCampo().getTipoCampo().getId()) {
			setPattern(DEFAULT_PATTERN);
		}
	}

	public CampoImport(CampoImport campo) {
		this(campo.getCampo());
		setAspas(campo.isAspas());
		setIgnore(campo.isIgnore());
		setPattern(campo.getPattern());
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
	
	public CampoConfig getCampo() {
		return campo;
	}
	
	public void setCampo(CampoConfig campo) {
		this.campo = campo;
	}

	public int getPositionInFile() {
		return positionInFile;
	}

	public void setPositionInFile(int positionInFile) {
		this.positionInFile = positionInFile;
	}
}