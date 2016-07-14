package br.gov.inca.tabulador.domain.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.primefaces.model.DualListModel;

import br.gov.inca.tabulador.domain.entity.config.CampoConfig;
import br.gov.inca.tabulador.domain.entity.config.ValorCampoConfig;
import br.gov.inca.tabulador.domain.entity.tipo.TipoFiltro;
import br.gov.inca.tabulador.util.DateUtils;
import br.gov.inca.tabulador.util.StringUtils;

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
		setPattern(DateUtils.DEFAUT_DATE_FMT);
		setValores(new DualListModel<>(new ArrayList<>(), new ArrayList<>()));
		setFiltro(new TipoFiltro());
	}

	public CampoFiltro(CampoConfig campo) {
		this();
		setCampo(campo);
		if (campo.getTipoCampo().isData()) {
			setValue(null);
			setPattern(DateUtils.DEFAUT_DATE_FMT);
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
		return StringUtils.safeToString(value);
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

	public boolean hasValue() {
		return StringUtils.isNotBlank(value) || (valores != null && valores.getTarget() != null && !valores.getTarget().isEmpty());
	}
}