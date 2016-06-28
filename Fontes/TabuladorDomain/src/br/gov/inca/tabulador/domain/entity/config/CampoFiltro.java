package br.gov.inca.tabulador.domain.entity.config;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CampoFiltro implements Serializable {
	private static final long serialVersionUID = -3857590020028288112L;

	private CampoConfig campo;
	private String pattern;
	private Object value;

	public CampoFiltro() {
		super();
		setCampo(new CampoConfig());
		setPattern(CampoImport.DEFAULT_PATTERN);
	}

	public CampoFiltro(CampoConfig campo) {
		this();
		setCampo(campo);
		if (campo.getTipoCampo().isData()) {
			setValue(new Date());
			setPattern(CampoImport.DEFAULT_PATTERN);
		}
	}

	public CampoFiltro(CampoFiltro filtro) {
		this();
		setCampo(filtro.getCampo());
		setPattern(filtro.getPattern());
		setValue(filtro.getValue());
	}

	public Object getValue() {
		return value;
	}

	public String getValueAsString() {
		final Object valueO = getValue();
		if (valueO instanceof Date) {
			return new SimpleDateFormat(getPattern()).format((Date) valueO);
		}
		return value.toString();
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public CampoConfig getCampo() {
		return campo;
	}

	public void setCampo(CampoConfig campo) {
		this.campo = campo;
		setValue(null);
	}

	public String getPattern() {
		return pattern;
	}
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}