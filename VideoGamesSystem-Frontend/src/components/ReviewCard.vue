<template>
  <div class="review-card">
    <div class="review-content">
      <p><strong>{{ review.customerUsername || 'Anonymous' }}</strong>:</p>
      <p>{{ review.reviewContent }}</p>
      <p>Rating: {{ review.gameRating }} / 5</p>
      <p><small>{{ formatDate(review.reviewDate) }}</small></p>
    </div>

    <!-- Replies Section -->
    <div v-if="review.replies && review.replies.length > 0" class="replies">
      <h4>Replies:</h4>
      <ReviewCard
        v-for="reply in review.replies"
        :key="reply.id"
        :review="reply"
        :isStaff="isStaff"
        @reply-posted="replyPosted"
      />
    </div>

    <!-- Reply Form for Staff -->
    <div v-if="isStaff && !showReplyForm" class="reply-btn">
      <button @click="toggleReplyForm">Reply</button>
    </div>

    <div v-if="isStaff && showReplyForm" class="reply-form">
      <textarea v-model="replyContent" placeholder="Write your reply..."></textarea>
      <button @click="submitReply">Submit Reply</button>
      <button @click="toggleReplyForm">Cancel</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

const PLACEHOLDER_CUSTOMER_ID = 999; // Replace with a valid dummy customer ID in your database

export default {
  name: "ReviewCard",
  props: {
    review: {
      type: Object,
      required: true,
    },
    isStaff: {
      type: Boolean,
      required: true,
    },
  },
  data() {
    return {
      showReplyForm: false,
      replyContent: "",
    };
  },
  methods: {
    toggleReplyForm() {
      this.showReplyForm = !this.showReplyForm;
    },
    async submitReply() {
      if (!this.replyContent.trim()) {
        alert("Reply content cannot be empty.");
        return;
      }

      const payload = {
        reviewContent: this.replyContent,
        gameRating: 5, // Default rating for replies
        customerId: PLACEHOLDER_CUSTOMER_ID, // Use placeholder customer ID for staff replies
        parentReviewId: this.review.id,
        gameId: this.review.game.id,
        reviewDate: new Date().toISOString().split("T")[0],
      };

      try {
        const response = await axios.post("http://localhost:8081/reviews", payload);
        this.$emit("reply-posted", response.data);
        this.replyContent = "";
        this.toggleReplyForm();
      } catch (error) {
        console.error("Error submitting reply:", error.response || error);
        alert("Failed to submit reply. Please try again.");
      }
    },
    formatDate(date) {
      return new Date(date).toLocaleDateString();
    },
    replyPosted(reply) {
      if (!this.review.replies) this.review.replies = [];
      this.review.replies.push(reply);
    },
  },
};
</script>

<style scoped>
.review-card {
  background-color: #ffffff;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.review-card p {
  margin: 5px 0;
  font-size: 1rem;
  color: #333;
}

.review-card strong {
  font-weight: bold;
  color: #000;
}

.replies {
  margin-left: 20px;
  margin-top: 10px;
  border-left: 2px solid #ccc;
  padding-left: 10px;
}

.reply-btn button {
  margin-top: 10px;
  background-color: #4caf50;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 4px;
  cursor: pointer;
}

.reply-btn button:hover {
  background-color: #45a049;
}

.reply-form textarea {
  width: 100%;
  margin-bottom: 10px;
}

.reply-form button {
  margin-right: 5px;
}
</style>
