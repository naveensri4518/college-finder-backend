package com.college.service;

import com.college.model.College;
import com.college.model.SavedCollege;
import com.college.repository.CollegeRepository;
import com.college.repository.SavedCollegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final SavedCollegeRepository savedCollegeRepository;

    /**
     * Get colleges with optional filters and pagination.
     */
    public Map<String, Object> getColleges(String search, String location,
                                            Double minRating, Double maxFees,
                                            int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("rating").descending());

        String searchParam = (search != null && !search.isBlank()) ? search.trim() : null;
        String locationParam = (location != null && !location.isBlank()) ? location.trim() : null;

        Page<College> collegePage = collegeRepository.findWithFilters(
                searchParam, locationParam, minRating, maxFees, pageable
        );

        return Map.of(
                "colleges", collegePage.getContent(),
                "totalPages", collegePage.getTotalPages(),
                "totalElements", collegePage.getTotalElements(),
                "currentPage", page
        );
    }

    /**
     * Get a single college by ID.
     */
    public Optional<College> getCollegeById(Long id) {
        return collegeRepository.findById(id);
    }

    /**
     * Get multiple colleges by IDs (for comparison).
     */
    public List<College> getCollegesByIds(List<Long> ids) {
        return collegeRepository.findByIdIn(ids);
    }

    /**
     * Save a college for a user. Returns true if newly saved, false if already saved.
     */
    @Transactional
    public boolean saveCollege(Long userId, Long collegeId) {
        if (savedCollegeRepository.existsByUserIdAndCollegeId(userId, collegeId)) {
            return false;
        }
        SavedCollege savedCollege = new SavedCollege(null, userId, collegeId);
        savedCollegeRepository.save(savedCollege);
        return true;
    }

    /**
     * Unsave a college for a user.
     */
    @Transactional
    public void unsaveCollege(Long userId, Long collegeId) {
        savedCollegeRepository.deleteByUserIdAndCollegeId(userId, collegeId);
    }

    /**
     * Get all saved colleges for a user.
     */
    public List<College> getSavedColleges(Long userId) {
        List<SavedCollege> savedList = savedCollegeRepository.findByUserId(userId);
        List<Long> ids = savedList.stream()
                .map(SavedCollege::getCollegeId)
                .collect(Collectors.toList());
        return collegeRepository.findByIdIn(ids);
    }

    /**
     * Check if a college is saved by a user.
     */
    public boolean isSaved(Long userId, Long collegeId) {
        return savedCollegeRepository.existsByUserIdAndCollegeId(userId, collegeId);
    }
}
