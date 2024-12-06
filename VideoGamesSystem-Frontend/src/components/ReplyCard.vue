<template>
    <div class="reply-card">
      <p><strong>{{ reply.customerUsername || 'Anonymous' }}</strong>: {{ reply.reviewContent }}</p>
      <p>Rating: {{ reply.gameRating }} / 5</p>
      <p><small>{{ formatDate(reply.reviewDate) }}</small></p>
  
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
  
  export default {
    name: "ReplyCard",
    props: {
      reply: {
        type: Object,
        required: true,
      },
      isStaff: {
        type: Boolean,
        required: true,
      },
      staffId: {
        type: Number,
        required: true, // Ensure the staffId is passed
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
          gameRating: 5,
          customerId: this.staffId, // Staff ID for replies
          parentReviewId: this.reply.id, // Parent review ID
          gameId: this.reply.game.id, // Game ID
          reviewDate: new Date().toISOString().split("T")[0], // Current date
        };
  
        console.log("Submitting reply payload:", payload);
  
        try {
          const response = await axios.post("http://localhost:8081/reviews", payload);
          console.log("Reply submitted successfully:", response.data);
  
          this.$emit("reply-posted", response.data);
          this.replyContent = "";
          this.toggleReplyForm();
          alert("Reply submitted successfully!");
        } catch (error) {
          console.error("Error submitting reply:", error.response || error);
          alert(
            `Failed to submit reply: ${
              error.response?.data?.message || error.message
            }`
          );
        }
      },
      formatDate(date) {
        return new Date(date).toLocaleDateString();
      },
    },
  };
  </script>
  
  <style scoped>
  .reply-card {
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
  