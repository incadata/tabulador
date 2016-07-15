package br.gov.inca.tabulador.web.factory;

import java.util.PropertyResourceBundle;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

import br.gov.inca.tabulador.domain.qualifier.Labels;
import br.gov.inca.tabulador.domain.qualifier.Messages;

public class BundleCreator {
	@Produces @Messages @Default
    public PropertyResourceBundle getMessagesBundle() {
        final FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().evaluateExpressionGet(context, "#{msg}", PropertyResourceBundle.class);
    }

	@Produces @Labels
    public PropertyResourceBundle getLabelsBundle() {
        final FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().evaluateExpressionGet(context, "#{labels}", PropertyResourceBundle.class);
    }
}
