package br.gov.inca.tabulador.domain.cdi.interceptor;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.gov.inca.tabulador.domain.cdi.qualifier.Transactional;

@Transactional
@Interceptor
public class TransactionInterceptor implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private @Inject EntityManager manager;
	
	@AroundInvoke
	public Object invoke(InvocationContext context) throws Exception {
		final EntityTransaction trx = manager.getTransaction();
		boolean criador = false;
		try {
			if (!trx.isActive()) {
				// truque para fazer rollback no que já passou
				// (senão, um futuro commit, confirmaria até mesmo
				// operações sem transação)
				trx.begin();
				trx.rollback();
				// agora sim inicia a transação
				trx.begin();
				criador = true;
			}
			return context.proceed();
		} catch (Exception e) {
			if (trx != null && criador) {
				trx.rollback();
			}
			throw e;
		} finally {
			if (trx != null && trx.isActive() && criador) {
				trx.commit();
			}
		}
	}

}
