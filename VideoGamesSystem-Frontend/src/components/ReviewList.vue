<template>
  <div class="review-list">
    <h2 class="reviews-title">Reviews</h2>

    <div v-if="reviews.length === 0" class="no-reviews">
      No reviews yet. Be the first to leave one!
    </div>

    <div v-else class="reviews-container">
      <ReviewCard
        v-for="review in reviews"
        :key="review.id"
        :review="review"
        :isStaff="isStaff"
        @reply-posted="replyPosted"
      />
    </div>

    <!-- Post Review Section for Customers -->
    <div v-if="!isStaff" class="post-review">
      <h3>Write a Review</h3>
      <textarea v-model="newReviewContent" placeholder="Share your thoughts..."></textarea>
      <div class="rating-section">
        <label for="rating">Rating:</label>
        <select id="rating" v-model="newReviewRating">
          <option v-for="rating in [1, 2, 3, 4, 5]" :key="rating" :value="rating">
            {{ rating }}
          </option>
        </select>
      </div>
      <button @click="submitReview" class="submit-review-btn">Submit Review</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import ReviewCard from "./ReviewCard.vue";
import { store } from "../store.js";

const PLACEHOLDER_CUSTOMER_ID = 999; // Replace with a valid dummy customer ID in your database

export default {
  name: "ReviewList",
  components: { ReviewCard },
  props: {
    gameId: {
      type: Number,
      required: true,
    },
    isStaff: {
      type: Boolean,
      required: true,
    },
  },
  data() {
    return {
      reviews: [],
      newReviewContent: "",
      newReviewRating: 5,
    };
  },
  async created() {
    await this.fetchReviews();
  },
  methods: {
    async fetchReviews() {
      try {
        const response = await axios.get(`http://localhost:8081/reviews/game/${this.gameId}`);
        this.reviews = response.data;
      } catch (error) {
        console.error("Error fetching reviews:", error);
        alert("Failed to load reviews. Please try again later.");
      }
    },
    async submitReview() {
      if (!this.newReviewContent.trim()) {
        alert("Review content cannot be empty.");
        return;
      }

      const customerId = this.isStaff ? PLACEHOLDER_CUSTOMER_ID : store.user.id;

      const payload = {
        reviewContent: this.newReviewContent,
        gameRating: this.newReviewRating,
        customerId,
        gameId: this.gameId,
        reviewDate: new Date().toISOString().split("T")[0],
      };

      try {
        const response = await axios.post("http://localhost:8081/reviews", payload);
        this.reviews.push(response.data);
        this.newReviewContent = "";
        this.newReviewRating = 5;
        alert("Review posted successfully!");
      } catch (error) {
        console.error("Error submitting review:", error.response || error);
        alert(`Failed to post review: ${error.response?.data?.message || error.message}`);
      }
    },
    replyPosted(reply) {
      const parentReview = this.reviews.find((review) => review.id === reply.parentReviewId);
      if (parentReview) {
        if (!parentReview.replies) parentReview.replies = [];
        parentReview.replies.push(reply);
      }
    },
  },
};
</script>

<style scoped>
.review-list {
  margin-top: 20px;
}

.reviews-title {
  font-size: 1.8rem;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

.no-reviews {
  color: #777;
  font-style: italic;
  margin-bottom: 20px;
}

.reviews-container {
  margin-bottom: 30px;
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 8px;
  border: 1px solid #ddd;
}

.post-review {
  margin-top: 20px;
  padding: 15px;
  background-color: #f1f1f1;
  border-radius: 8px;
  border: 1px solid #ccc;
}

textarea {
  width: 100%;
  height: 80px;
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #ddd;
}

.rating-section {
  margin-top: 10px;
}

.rating-section select {
  padding: 5px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.submit-review-btn {
  margin-top: 10px;
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
}

.submit-review-btn:hover {
  background-color: #0056b3;
}
</style>
