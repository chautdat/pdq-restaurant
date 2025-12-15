<template>
  <vue-basic-alert :duration="300" :closeIn="2000" ref="alert" />
  <section class="feedback-section">
    <div class="heading">
      <span>góp ý khách hàng</span>
      <h3>Chúng tôi lắng nghe mọi chia sẻ của bạn</h3>
    </div>

    <div class="icons-container">
      <div class="icons">
        <img src="../assets/images/icon-1.png" alt="" />
        <h3>Thời gian hỗ trợ: 7:00 - 22:00</h3>
      </div>
      <div class="icons">
        <img src="../assets/images/icon-2.png" alt="" />
        <h3>Hotline: +84 123 456 789</h3>
      </div>
      <div class="icons">
        <img src="../assets/images/icon-3.png" alt="" />
        <h3>Email: support@pdq.vn</h3>
      </div>
    </div>

    <form
      id="feedbackForm"
      @submit="handleSubmit"
      novalidate
      autocomplete="off"
    >
      <div class="grid-form">
        <div class="input-box">
          <label for="fName">Họ và tên</label>
          <input type="text" id="fName" v-model="feedback.name" required />
          <p v-if="errors.name">{{ errors.name }}</p>
        </div>

        <div class="input-box">
          <label for="fPhone">Số điện thoại</label>
          <input type="text" id="fPhone" v-model="feedback.phone" required />
          <p v-if="errors.phone">{{ errors.phone }}</p>
        </div>

        <div class="input-box">
          <label for="fEmail">Email (tuỳ chọn)</label>
          <input type="email" id="fEmail" v-model="feedback.email" />
          <p v-if="errors.email">{{ errors.email }}</p>
        </div>

        <div class="input-box">
          <label for="fSubject">Chủ đề</label>
          <input
            type="text"
            id="fSubject"
            v-model="feedback.subject"
            required
          />
          <p v-if="errors.subject">{{ errors.subject }}</p>
        </div>

        <div class="input-box textarea">
          <label for="fMessage">Nội dung góp ý</label>
          <textarea
            id="fMessage"
            placeholder="Bạn muốn chúng tôi cải thiện điều gì?"
            v-model="feedback.message"
            rows="6"
            required
          ></textarea>
          <p v-if="errors.message">{{ errors.message }}</p>
        </div>
      </div>

      <button class="btn" type="submit" :disabled="submitting">
        <span v-if="submitting">⏳ Đang gửi...</span>
        <span v-else>Gửi góp ý</span>
      </button>
    </form>
  </section>
</template>

<script>
import axios from "axios";
import VueBasicAlert from "vue-basic-alert";
export default {
  name: "Table",
  data() {
    return {
      submitting: false,
      feedback: {
        name: "",
        phone: "",
        email: "",
        subject: "",
        message: "",
      },
      errors: {},
    };
  },
  methods: {
    validate() {
      const errs = {};
      const name = this.feedback.name?.trim();
      const phone = this.feedback.phone?.trim();
      const email = this.feedback.email?.trim();
      const subject = this.feedback.subject?.trim();
      const message = this.feedback.message?.trim();

      if (!name) errs.name = "Vui lòng nhập họ và tên";
      if (!phone) {
        errs.phone = "Vui lòng nhập số điện thoại";
      } else if (!/^0\\d{9,10}$/.test(phone) && !/^84\\d{9}$/.test(phone)) {
        errs.phone = "Số điện thoại không hợp lệ (bắt đầu 0... hoặc 84...)";
      }
      if (email && !/^[\w.-]+@([\w-]+\.)+[\w-]{2,}$/i.test(email)) {
        errs.email = "Email không hợp lệ";
      }
      if (!subject) errs.subject = "Vui lòng nhập chủ đề góp ý";
      if (!message || message.length < 10) {
        errs.message = "Nội dung tối thiểu 10 ký tự";
      }
      this.errors = errs;
      return Object.keys(errs).length === 0;
    },

    resetForm() {
      this.feedback = {
        name: "",
        phone: "",
        email: "",
        subject: "",
        message: "",
      };
      this.errors = {};
      const form = document.getElementById("feedbackForm");
      if (form) form.reset();
    },

    async handleSubmit(e) {
      e.preventDefault();
      if (!this.validate()) return;

      this.submitting = true;
      try {
        await axios.post("/feedback", {
          name: this.feedback.name.trim(),
          phone: this.feedback.phone.trim(),
          email: this.feedback.email?.trim() || undefined,
          subject: this.feedback.subject.trim(),
          message: this.feedback.message.trim(),
        });

        this.$refs.alert.showAlert(
          "success",
          "Cảm ơn bạn đã góp ý! Chúng tôi sẽ phản hồi sớm.",
          "Gửi thành công"
        );
        this.resetForm();
      } catch (err) {
        console.error("❌ Feedback error:", err);
        this.$refs.alert.showAlert(
          "error",
          err.response?.data?.message ||
            "Không thể gửi góp ý. Vui lòng thử lại.",
          "Gửi thất bại"
        );
      } finally {
        this.submitting = false;
      }
    },
  },
  components: {
    VueBasicAlert,
  },
};
</script>

<style scoped>
.feedback-section {
  padding: 2rem 9%;
  background: linear-gradient(135deg, #f0fbf4 0%, #e4f6eb 100%);
  border-radius: 22px;
}
.heading {
  text-align: center;
  margin-bottom: 2rem;
}
.heading span {
  display: inline-block;
  text-transform: uppercase;
  color: #2f9e44;
  font-weight: 800;
  font-size: 18px;
  letter-spacing: 2px;
  background: #eef7f0;
  padding: 6px 14px;
  border-radius: 18px;
  margin-bottom: 8px;
}
.heading h3 {
  font-size: 2.2rem;
  color: #1f2937;
  font-weight: 800;
  margin-top: 0.6rem;
  letter-spacing: 1px;
}

.icons-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}
.icons {
  border-radius: 0.5rem;
  padding: 1.6rem;
  text-align: center;
  background: #f7fbf8;
  box-shadow: 0 8px 28px rgba(47, 158, 68, 0.08);
}
.icons img {
  height: 5rem;
}
.icons h3 {
  font-size: 1.15rem;
  color: #1f2937;
  margin-top: 0.5rem;
  font-weight: 700;
}

form {
  background: #f7fbf8;
  padding: 2rem;
  border-radius: 1rem;
  box-shadow: 0 10px 38px rgba(47, 158, 68, 0.08);
  max-width: 980px;
  margin: 0 auto;
}

.grid-form {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem 2.5rem;
}
.input-box {
  display: flex;
  flex-direction: column;
  position: relative;
}
.input-box label {
  font-size: 1.05rem;
  color: #111827;
  margin-bottom: 0.5rem;
  font-weight: 700;
  letter-spacing: 0.2px;
}
.input-box input,
.input-box textarea {
  padding: 0.95rem 1.15rem;
  border: 2px solid #e5e7eb;
  border-radius: 0.7rem;
  font-size: 1rem;
  outline: none;
  background: #fff;
  color: #111827;
  transition: border-color 0.2s, box-shadow 0.2s;
}
.input-box input:focus,
.input-box textarea:focus {
  border-color: #2f9e44;
  box-shadow: 0 0 0 3px rgba(47, 158, 68, 0.14);
  background: #f7fcf8;
}
.input-box textarea {
  min-height: 120px;
  resize: vertical;
}
.input-box p {
  color: #e11d48;
  font-size: 0.98rem;
  margin: 3px 0 0 0;
  position: relative;
  font-weight: 600;
}

.input-box.textarea {
  grid-column: span 2;
}

.btn {
  display: block;
  width: 230px;
  margin: 2.5rem auto 0;
  padding: 1.05rem 0;
  background: linear-gradient(90deg, #2f9e44, #248f3c 100%);
  color: #fff;
  font-weight: 800;
  font-size: 1.1rem;
  border: none;
  border-radius: 999px;
  cursor: pointer;
  box-shadow: 0 10px 25px rgba(47, 158, 68, 0.22);
  transition: background 0.2s, transform 0.17s, box-shadow 0.2s;
  letter-spacing: 0.8px;
}
.btn:hover:not(:disabled) {
  background: linear-gradient(90deg, #248f3c, #1f7f34 100%);
  transform: translateY(-2px) scale(1.02);
  box-shadow: 0 12px 30px rgba(36, 143, 60, 0.28);
}
.btn:disabled {
  opacity: 0.65;
  cursor: not-allowed;
  box-shadow: none;
}

@media (max-width: 900px) {
  .grid-form {
    grid-template-columns: 1fr;
    gap: 1.4rem 0;
  }
  .input-box.textarea {
    grid-column: span 1;
  }
}
@media (max-width: 600px) {
  .feedback-section {
    padding: 1rem 1.1rem;
  }
  .heading h3 {
    font-size: 1.3rem;
  }
  .icons-container {
    grid-template-columns: 1fr;
  }
  form {
    padding: 1.1rem;
  }
  .btn {
    width: 100%;
    font-size: 1rem;
    padding: 1rem 0;
  }
}
</style>
