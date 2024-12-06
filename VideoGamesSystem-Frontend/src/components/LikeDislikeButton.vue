<template>
  <div class="like-dislike-container">
    <button @click="handleLike" :disabled="userLiked" class="like-btn">
      👍 Like ({{ likeCount }})
    </button>
    <button @click="handleDislike" :disabled="userDisliked" class="dislike-btn">
      👎 Dislike ({{ dislikeCount }})
    </button>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "LikeDislikeButton",
  props: {
    reviewId: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      likeCount: 0,
      dislikeCount: 0,
      userLiked: false,
      userDisliked: false,
    };
  },
  async created() {
    await this.fetchCounts();
    await this.checkUserInteraction();
  },
  methods: {
    async fetchCounts() {
      try {
        const response = await axios.get(
          `http://localhost:8081/reviews/${this.reviewId}/likes`
        );
        this.likeCount = response.data.likeCount || 0;
        this.dislikeCount = response.data.dislikeCount || 0;
      } catch (error) {
        console.error("Error fetching counts:", error);
      }
    },
    async checkUserInteraction() {
      try {
        const response = await axios.get(
          `http://localhost:8081/reviews/${this.reviewId}/user-interaction`
        );
        this.userLiked = response.data.liked || false;
        this.userDisliked = response.data.disliked || false;
      } catch (error) {
        console.error("Error checking user interaction:", error);
      }
    },
    async handleLike() {
      if (!this.userLiked && !this.userDisliked) {
        try {
          await axios.post(
            `http://localhost:8081/reviews/${this.reviewId}/like`
          );
          this.likeCount += 1;
          this.userLiked = true;
        } catch (error) {
          console.error("Error liking review:", error);
        }
      }
    },
    async handleDislike() {
      if (!this.userLiked && !this.userDisliked) {
        try {
          await axios.post(
            `http://localhost:8081/reviews/${this.reviewId}/dislike`
          );
          this.dislikeCount += 1;
          this.userDisliked = true;
        } catch (error) {
          console.error("Error disliking review:", error);
        }
      }
    },
  },
};
</script>

<style scoped>
.like-dislike-container {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}
.like-btn,
.dislike-btn {
  padding: 5px 10px;
  font-size: 14px;
  cursor: pointer;
  border-radius: 4px;
}
.like-btn {
  background-color: #4caf50;
  color: white;
}
.dislike-btn {
  background-color: #f44336;
  color: white;
}
button:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}
</style>
