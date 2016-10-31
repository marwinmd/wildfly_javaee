package com.library.app.commontests.utils;

import javax.persistence.*;

import org.junit.*;

@Ignore
public class DBCommandTransactionExecutor {

	private EntityManager entityManager;

	public DBCommandTransactionExecutor(EntityManager em) {
		this.entityManager = em;
	}

	public <T> T executeCommand(DBCommand<T> dbCommand) {
		try {
			entityManager.getTransaction().begin();
			T toReturn = dbCommand.execute();
			entityManager.getTransaction().commit();
			entityManager.clear();
			return toReturn;
		} catch (Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			throw new IllegalStateException(e);
		}
	}
}
