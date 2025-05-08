package by.grsu.eventlink.repository;

import by.grsu.eventlink.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    Boolean existsByTitle(String title);

    Page<Category> findAll(Pageable pageable);

    Optional<Category> findCategoryById(Long id);

    @Query(
            value = """
            select cat.id from categories as cat
            left join sub_categories as scat on cat.id = scat.categories_id
            where scat.id = :subCategoryId
            """,
            nativeQuery = true
    )
    Optional<Long> findBySubCategoryId(Long subCategoryId);

    Page<Category> findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description,
                                                                                       Pageable pageable);

}
