package com.college.controller;

import com.college.dto.SaveCollegeRequest;
import com.college.model.College;
import com.college.service.AuthService;
import com.college.service.CollegeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/colleges")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowedHeaders = "*", methods = {
    RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS
})
public class CollegeController {

    private final CollegeService collegeService;
    private final AuthService authService;

    // ── GET /api/colleges ─────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<Map<String, Object>> getColleges(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxFees,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        // Treat minRating=0 as "no filter" (frontend slider default)
        Double effectiveMinRating = (minRating != null && minRating > 0) ? minRating : null;

        Map<String, Object> result = collegeService.getColleges(
                search, location, effectiveMinRating, maxFees, page, size
        );
        return ResponseEntity.ok(result);
    }

    // ── GET /api/colleges/compare?ids=1,2,3 ──────────────────────────────────
    @GetMapping("/compare")
    public ResponseEntity<?> compareColleges(@RequestParam List<Long> ids) {
        if (ids == null || ids.size() < 2 || ids.size() > 3) {
            return ResponseEntity.badRequest().body(Map.of("error", "Provide 2 to 3 college IDs"));
        }
        return ResponseEntity.ok(collegeService.getCollegesByIds(ids));
    }

    // ── GET /api/colleges/saved ───────────────────────────────────────────────
    @GetMapping("/saved")
    public ResponseEntity<?> getSavedColleges(
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        Long userId = extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized. Please log in."));
        }
        return ResponseEntity.ok(collegeService.getSavedColleges(userId));
    }

    // ── GET /api/colleges/{id} ────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<?> getCollege(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        Optional<College> college = collegeService.getCollegeById(id);
        if (college.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "College not found"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("college", college.get());

        Long userId = extractUserId(authHeader);
        response.put("isSaved", userId != null && collegeService.isSaved(userId, id));

        return ResponseEntity.ok(response);
    }

    // ── POST /api/colleges/save ───────────────────────────────────────────────
    @PostMapping("/save")
    public ResponseEntity<?> saveCollege(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody SaveCollegeRequest request
    ) {
        Long userId = extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized. Please log in."));
        }
        boolean saved = collegeService.saveCollege(userId, request.getCollegeId());
        String msg = saved ? "College saved successfully ❤️" : "College already saved";
        return ResponseEntity.ok(Map.of("message", msg, "saved", true));
    }

    // ── DELETE /api/colleges/save/{collegeId} ─────────────────────────────────
    @DeleteMapping("/save/{collegeId}")
    public ResponseEntity<?> unsaveCollege(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable Long collegeId
    ) {
        Long userId = extractUserId(authHeader);
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized. Please log in."));
        }
        collegeService.unsaveCollege(userId, collegeId);
        return ResponseEntity.ok(Map.of("message", "College removed from saved list", "saved", false));
    }

    // ─── Helper ───────────────────────────────────────────────────────────────
    private Long extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        String token = authHeader.substring(7).trim();
        if (token.isEmpty()) return null;
        return authService.validateToken(token).orElse(null);
    }
}
