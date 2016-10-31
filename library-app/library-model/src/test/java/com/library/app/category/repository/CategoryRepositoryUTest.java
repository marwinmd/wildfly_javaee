package com.library.app.category.repository;

import static org.junit.Assert.*;

import java.util.*;

import javax.persistence.*;

import org.junit.*;

import com.library.app.category.model.*;
import com.library.app.commontests.category.*;
import com.library.app.commontests.utils.*;

public class CategoryRepositoryUTest {
	private static final long INVALID_ID = 999L;
	private EntityManagerFactory emf;
	private EntityManager em;
	private CategoryRepository categoryRepository;
	private DBCommandTransactionExecutor dbExecutor;

	@Before
	public void initTestCase() {
		emf = Persistence.createEntityManagerFactory("libraryPU");
		em = emf.createEntityManager();

		categoryRepository = new CategoryRepository();
		categoryRepository.em = em;
		dbExecutor = new DBCommandTransactionExecutor(em);
	}

	@After
	public void closeEntityManager() {
		em.close();
		emf.close();
	}

	@Test
	public void addCategoryShouldReturnNotNullCategory() {
		assertNotNull(persistAndReturnCategory(CategoryForTestsRepository.java()));
	}

	@Test
	public void addCategoryShouldReturnValidCategoryWithNonEmtpyId() {
		assertNotNull(persistAndReturnCategory(CategoryForTestsRepository.java()).getId());
	}

	@Test
	public void findByIdShouldReturnPersistedCategoryForValidCategory() {
		Category persistedCategory = persistAndReturnCategory(CategoryForTestsRepository.java());
		Category queriedCategory = findById(persistedCategory.getId());
		assertEquals(persistedCategory, queriedCategory);
	}

	@Test
	public void findByIdShouldReturnNullForInvalidId() {
		assertNull(categoryRepository.findById(INVALID_ID));
	}

	@Test
	public void findByIdShouldReturnNullForNullId() {
		assertNull(categoryRepository.findById(null));
	}

	@Test
	public void updateCategoryForExistingCategoryShouldReturnCorrectCategory() {
		String newName = CategoryForTestsRepository.cleanCode().getName();

		Category initialPersistedCategory = persistAndReturnCategory(CategoryForTestsRepository.java());
		Category queriedCategory = findById(initialPersistedCategory.getId());

		queriedCategory.setName(newName);
		categoryRepository.update(queriedCategory);

		assertEquals(initialPersistedCategory.getId(), queriedCategory.getId());
		assertEquals(queriedCategory.getName(), newName);
	}

	@Test
	public void findAllCategoriesShouldReturnTheCorrectCategories() {
		ArrayList<Category> categoriesToBeInserted = fillListWithCategoriesToBePersisted();
		categoriesToBeInserted.forEach(this::persist);

		List<Category> allQueriedCategories = getAllCategories();

		assertEquals("expected size didnt match", categoriesToBeInserted.size(), allQueriedCategories.size());
		for (int i = 0; i < categoriesToBeInserted.size(); i++) {
			assertEquals(categoriesToBeInserted.get(i).getName(), allQueriedCategories.get(i).getName());
		}
	}

	/** helpers */

	private List<Category> getAllCategories() {
		return dbExecutor.executeCommand(new DBCommand<List<Category>>() {
			@Override
			public List<Category> execute() {
				return categoryRepository.findAllCategoriesOrderedByNameAsc();
			}
		});
	}

	private void persist(Category category) {
		categoryRepository.add(category);
	}

	private Category persistAndReturnCategory(Category category) {
		Category executeCommand = dbExecutor.executeCommand(new DBCommand<Category>() {
			@Override
			public Category execute() {
				return executeAddAndReturnWithValidCategory(category);
			}
		});
		return executeCommand;
	}

	private Category executeAddAndReturnWithValidCategory(Category category) {
		return categoryRepository.addAndReturn(category);
	}

	private Category findById(Long id) {
		return categoryRepository.findById(id);
	}

	private ArrayList<Category> fillListWithCategoriesToBePersisted() {
		ArrayList<Category> categoriesToBeInserted = new ArrayList<Category>();
		categoriesToBeInserted.add(CategoryForTestsRepository.architecture());
		categoriesToBeInserted.add(CategoryForTestsRepository.cleanCode());
		categoriesToBeInserted.add(CategoryForTestsRepository.java());
		categoriesToBeInserted.add(CategoryForTestsRepository.networks());
		return categoriesToBeInserted;
	}
}