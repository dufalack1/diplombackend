package by.grsu.eventlink.repository;

import by.grsu.eventlink.entity.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SubCategoryRepository extends CrudRepository<SubCategory, Long> {

    Boolean existsByTitle(String title);

    @EntityGraph(attributePaths = { "categoryOwner", "nodes" })
    Optional<SubCategory> getById(Long id);

    @Query(value = """
            select sc.id, sc.description, sc.image_url, sc.categories_id,
            sc.short_description, sc.title from sub_categories sc
            left outer join nodes n on sc.id = n.sub_categories_id
            group by sc.id order by count(n) limit :limit
            """,
            nativeQuery = true)
    List<SubCategory> getSubCategoriesByNodesCount(Integer limit);

    Page<SubCategory> getSubCategoriesByCategoryOwner_Id(Long categoryOwner_id, Pageable pageable);

    Page<SubCategory> getSubCategoriesByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrShortDescriptionContainingIgnoreCase(String title,
                                                                                                                                         String description,
                                                                                                                                         String shortDescription, Pageable pageable);

    default Page<SubCategory> getSubCategoriesAnyFieldMatch(String query, Pageable pageable) {
        return getSubCategoriesByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrShortDescriptionContainingIgnoreCase(
                query, query, query, pageable
        );
    }

    default Page<SubCategory> getByCategoryId(Long categoryId, Pageable pageable) {
        return getSubCategoriesByCategoryOwner_Id(categoryId, pageable);
    }

}
