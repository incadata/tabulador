package br.gov.inca.tabulador.web.factory;

import java.util.PropertyResourceBundle;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

public class BundleCreator {
	@Produces
    public PropertyResourceBundle getBundle() {
        final FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().evaluateExpressionGet(context, "#{msg}", PropertyResourceBundle.class);
    }
}
