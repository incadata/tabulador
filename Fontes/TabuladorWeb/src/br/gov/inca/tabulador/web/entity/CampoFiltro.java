package br.gov.inca.tabulador.web.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.primefaces.model.DualListModel;

import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.ValorCampoConfig;
import br.gov.inca.tabulador.domain.entity.tipo.TipoFiltro;

public class CampoFiltro implements Serializable {
	private static final long serialVersionUID = -3857590020028288112L;

	private CampoConfig campo;
	private TipoFiltro filtro;
	private String pattern;
	private Object value;
	private DualListModel<ValorCampoConfig> valores;

	public CampoFiltro() {
		super();
		setCampo(new CampoConfig());
		setPattern(CampoImport.DEFAULT_PATTERN);
		setValores(new DualListModel<>(new ArrayList<>(), new ArrayList<>()));
		setFiltro(new TipoFiltro());
	}

	public CampoFiltro(CampoConfig campo) {
		this();
		setCampo(campo);
		if (campo.getTipoCampo().isData()) {
			setValue(new Date());
			setPattern(CampoImport.DEFAULT_PATTERN);
		}
		getValores().getSource().addAll(campo.getValores());
		if (campo.getTipoFiltro() != null) {
			setFiltro(campo.getTipoFiltro());
		}
	}

	public CampoFiltro(CampoFiltro filtro) {
		this(filtro.getCampo());
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
	
	public DualListModel<ValorCampoConfig> getValores() {
		return valores;
	}
	
	public void setValores(DualListModel<ValorCampoConfig> valores) {
		this.valores = valores;
	}
	
	public TipoFiltro getFiltro() {
		return filtro;
	}
	
	public void setFiltro(TipoFiltro filtro) {
		this.filtro = filtro;
	}
}