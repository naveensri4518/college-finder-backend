package com.college.repository;

import com.college.model.College;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollegeRepository extends JpaRepository<College, Long> {

    /**
     * Case-insensitive search across name, location, description.
     * Also supports filters: location, minRating, maxFees.
     * Uses LOWER() on both sides for MySQL compatibility with any collation.
     */
    @Query("SELECT c FROM College c WHERE " +
           "(:search IS NULL OR " +
           "  LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "  LOWER(c.location) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "  LOWER(COALESCE(c.description,'')) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:location IS NULL OR LOWER(c.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:minRating IS NULL OR c.rating >= :minRating) AND " +
           "(:maxFees IS NULL OR c.fees <= :maxFees)")
    Page<College> findWithFilters(
            @Param("search")    String search,
            @Param("location")  String location,
            @Param("minRating") Double minRating,
            @Param("maxFees")   Double maxFees,
            Pageable pageable
    );

    List<College> findByIdIn(List<Long> ids);
}
