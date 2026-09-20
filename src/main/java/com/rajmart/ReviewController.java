package com.rajmart;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @PostMapping
    public Map<String, Object> addReview(
            @RequestBody ReviewRequest request) {

        Map<String, Object> response = new HashMap<>();

        if (request.productId <= 0) {
            response.put("success", false);
            response.put("message", "Invalid product ID");
            return response;
        }

        if (request.customerName == null ||
                request.customerName.trim().isEmpty()) {

            response.put("success", false);
            response.put("message", "Customer name is required");
            return response;
        }

        if (request.rating < 1 || request.rating > 5) {

            response.put("success", false);
            response.put("message", "Rating must be between 1 and 5");
            return response;
        }

        boolean success = ReviewDAO.addReview(
                request.productId,
                request.customerName.trim(),
                request.rating,
                request.reviewText
        );

        response.put("success", success);

        if (success) {
            response.put("message", "Review added successfully!");
        } else {
            response.put("message", "Failed to add review.");
        }

        return response;
    }


    @GetMapping("/{productId}")
    public ArrayList<Map<String, Object>> getReviews(
            @PathVariable int productId) {

        return ReviewDAO.getReviews(productId);
    }


    public static class ReviewRequest {

        public int productId;
        public String customerName;
        public int rating;
        public String reviewText;
    }
}
