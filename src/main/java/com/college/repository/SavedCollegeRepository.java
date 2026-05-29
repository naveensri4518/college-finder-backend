package com.college.repository;

import com.college.model.SavedCollege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedCollegeRepository extends JpaRepository<SavedCollege, Long> {
    List<SavedCollege> findByUserId(Long userId);
    Optional<SavedCollege> findByUserIdAndCollegeId(Long userId, Long collegeId);
    boolean existsByUserIdAndCollegeId(Long userId, Long collegeId);
    void deleteByUserIdAndCollegeId(Long userId, Long collegeId);
}
