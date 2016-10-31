package com.library.app.category.repository;

import java.util.*;

import javax.persistence.*;

import com.library.app.category.model.*;

public class CategoryRepository {

	EntityManager em;

	public Category addAndReturn(final Category category) {
		persist(category);
		return category;
	}

	public void add(final Category category) {
		persist(category);
	}

	public Category findById(final Long id) {
		if (id == null) {
			return null;
		}
		return em.find(Category.class, id);
	}

	public void update(Category categoryAfterAdd) {
		em.merge(categoryAfterAdd);
	}

	private void persist(final Category category) {
		em.persist(category);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Category> findAllCategoriesOrderedByNameAsc() {
		return (ArrayList<Category>) em.createQuery("Select e from Category e Order by e.name").getResultList();
	}

	public boolean categoryAllreadyExist(Category category) {
		return em.createQuery("Select e from Category e").getResultList().size() > 0;
	}

}