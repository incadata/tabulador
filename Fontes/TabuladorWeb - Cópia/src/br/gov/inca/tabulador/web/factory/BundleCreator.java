package br.gov.inca.tabulador.web.factory;

import java.util.PropertyResourceBundle;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

public class BundleCreator {
	@Produces
    public PropertyResourceBundle getBundle() {
        final FacesContext context = FacesContext.getCurrentInstance();
<<<<<<< HEAD
        return context.getApplication().evaluateExpressionGet(context, "#{msg}", PropertyResourceBundle.class);
=======
        return context.getApplication().evaluateExpressionGet(context, "#{text}", PropertyResourceBundle.class);
>>>>>>> branch 'master' of https://github.com/incadata/tabulador.git
    }
}
