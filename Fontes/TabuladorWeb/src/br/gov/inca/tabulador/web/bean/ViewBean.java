package br.gov.inca.tabulador.web.bean;

import java.io.Serializable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public interface ViewBean extends Serializable {
	default void showError(Throwable error, String summary, String detail) {
		if (error != null) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, detail, error);
		}
		showError(summary, detail);
	}

	default void showError(String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(getMessageClientID(), new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
	}

	default void showWarn(String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(getMessageClientID(), new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail));
	}

	default void showInfo(String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(getMessageClientID(), new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
	}

	default void clearMessages() {
		clearMessages(getMessageClientID());
	}

	default void clearMessages(String clientId) {
		final Iterator<FacesMessage> messages = FacesContext.getCurrentInstance().getMessages(clientId);
		while (messages.hasNext()) {
			messages.next();
			messages.remove();
		}
	}

	default String getMessageClientID() {
		return getClass().getName();
	}

}