package br.gov.inca.tabulador.domain.entity.tipo;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Direitos Autorais Reservados (c) 2016 Instituto Nacional de C�ncer
 * 
 * Este programa � software livre; voc� pode redistribu�-lo
 * e/ou modific�-lo sob os termos da Licen�a P�blica Geral
 * GNU conforme publicada pela Free Software Foundation;
 * 
 * Este programa � distribu�do na expectativa de que seja �til,
 * por�m, SEM NENHUMA GARANTIA; nem mesmo a garantia impl�cita
 * de COMERCIABILIDADE OU ADEQUA��O A UMA FINALIDADE ESPEC�FICA.
 * 
 * Consulte a Licen�a P�blica Geral do GNU para mais detalhes.
 * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral do
 * GNU junto com este programa; se n�o, escreva para a
 * Free Software Foundation, Inc., no endere�o:
 * 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */
public class TipoCampoTest extends TipoCampo {
	private static final long serialVersionUID = -3779772160023565140L;

	@Test
	public void isDataNull() {
		setId(null);
		Assert.assertFalse(isData());
	}

	@Test
	public void isDataFalse() {
		setId(0);
		Assert.assertFalse(isData());

		setId(TipoCampo.TIPO_DATA + 1);
		Assert.assertFalse(isData());

		setId(TipoCampo.TIPO_INTEIRO);
		Assert.assertFalse(isData());

		setId(TipoCampo.TIPO_TEXTO);
		Assert.assertFalse(isData());
	}

	@Test
	public void isDataTrue() {
		setId(TipoCampo.TIPO_DATA);
		Assert.assertTrue(isData());
	}

}
