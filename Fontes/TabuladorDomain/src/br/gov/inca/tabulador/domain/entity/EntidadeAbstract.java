package br.gov.inca.tabulador.domain.entity;


public abstract class EntidadeAbstract<T> implements Entidade<T> {
	private static final long serialVersionUID = 5204418131543961257L;

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || !obj.getClass().equals(this.getClass())) {
			return false;
		}
		@SuppressWarnings("unchecked")
		final EntidadeAbstract<T> objTipoCampo = (EntidadeAbstract<T>) obj;
		/*
		if (getId() == null || objTipoCampo.getId() == null) {
			return false;
		}
		*/
		return getId().equals(objTipoCampo.getId());
	}
	
	@Override
	public int hashCode() {
		return getId().hashCode();
	}
}
