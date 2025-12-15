<template>
  <div class="promotions">
    <!-- Hero Section -->
    <div class="hero-section">
      <div class="hero-content">
        <span class="hero-badge">Special Offers</span>
        <h1 class="hero-title">
          Delicious Deals
          <span class="highlight">Every Day</span>
        </h1>
        <p class="hero-subtitle">
          Experience the best quality with unbeatable prices
        </p>
      </div>
      <div class="hero-decoration">
        <div class="floating-circle"></div>
        <div class="floating-circle secondary"></div>
      </div>
    </div>

    <!-- Weekly Schedule Section -->
    <div class="schedule-section">
      <div class="section-header">
        <h2 class="section-title">Weekly Promotions</h2>
        <p class="section-subtitle">Check out our amazing daily deals</p>
      </div>

      <div class="schedule-wrapper">
        <div class="schedule-container">
          <!-- Monday to Friday -->
          <div class="day-column" v-for="(day, index) in weekdays" :key="index">
            <div class="day-header">{{ day.name }}</div>

            <div
              class="time-slot"
              v-for="(slot, slotIndex) in day.slots"
              :key="slotIndex"
            >
              <div
                v-if="slot.promotion"
                class="promotion-card"
                :class="slot.colorClass"
              >
                <div class="promotion-time">
                  <i class="icon-clock"></i>
                  {{ slot.time }}
                </div>
                <h4 class="promotion-name">{{ slot.promotion.name }}</h4>
                <p class="promotion-detail">{{ slot.promotion.detail }}</p>
                <div class="promotion-icon">
                  <i :class="slot.promotion.icon"></i>
                </div>
              </div>
              <div v-else class="empty-slot">
                <span>—</span>
              </div>
            </div>
          </div>

          <!-- Weekend -->
          <div class="weekend-wrapper">
            <div class="weekend-header">Weekend Special</div>
            <div class="weekend-content">
              <div class="weekend-card">
                <div class="weekend-time">
                  <i class="icon-clock"></i>
                  Fri - Sun • 18:00 - 20:00
                </div>
                <h4>Happy Dinner</h4>
                <p>15% OFF on all items</p>
                <div class="weekend-decoration">
                  <i class="icon-dinner"></i>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Special Offers Section -->
    <div class="offers-section">
      <div class="section-header">
        <h2 class="section-title">Featured Deals</h2>
        <p class="section-subtitle">
          Limited time offers you don't want to miss
        </p>
      </div>

      <div class="offers-grid">
        <!-- Offer Card 1 -->
        <div class="offer-card">
          <div class="offer-image">
            <img src="../assets/images/home2.png" alt="Party Taco" />
            <div class="offer-badge">50% OFF</div>
          </div>
          <div class="offer-content">
            <h3>Party Taco Fiesta</h3>
            <ul class="offer-features">
              <li>
                <i class="icon-check"></i>
                Order 10+ tacos get 50% discount
              </li>
              <li>
                <i class="icon-check"></i>
                Weekend nights only
              </li>
              <li>
                <i class="icon-check"></i>
                Online payment exclusive
              </li>
            </ul>
            <router-link to="/menu" @click="scrollToTop()" class="offer-btn">
              Order Now
              <i class="icon-arrow-right"></i>
            </router-link>
          </div>
        </div>

        <!-- Offer Card 2 -->
        <div class="offer-card">
          <div class="offer-image">
            <img src="../assets/images/home3.png" alt="Happy Lunch" />
            <div class="offer-badge orange">25% EXTRA</div>
          </div>
          <div class="offer-content">
            <h3>Happy Lunch Special</h3>
            <ul class="offer-features">
              <li>
                <i class="icon-check"></i>
                Free burrito upsize
              </li>
              <li>
                <i class="icon-check"></i>
                Daily 10AM - 2PM
              </li>
              <li>
                <i class="icon-check"></i>
                Delivery exclusive
              </li>
            </ul>
            <router-link to="/menu" @click="scrollToTop()" class="offer-btn">
              Order Now
              <i class="icon-arrow-right"></i>
            </router-link>
          </div>
        </div>

        <!-- Offer Card 3 -->
        <div class="offer-card">
          <div class="offer-image">
            <img src="../assets/images/home4.png" alt="New Drink" />
            <div class="offer-badge purple">100% CASHBACK</div>
          </div>
          <div class="offer-content">
            <h3>Michelada Madness</h3>
            <ul class="offer-features">
              <li>
                <i class="icon-check"></i>
                Free Michelada on $20+ orders
              </li>
              <li>
                <i class="icon-check"></i>
                Limited time offer
              </li>
              <li>
                <i class="icon-check"></i>
                Online payment only
              </li>
            </ul>
            <router-link to="/menu" @click="scrollToTop()" class="offer-btn">
              Order Now
              <i class="icon-arrow-right"></i>
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- CTA Section -->
    <div class="cta-section">
      <div class="cta-content">
        <h2>Ready to Save Big?</h2>
        <p>Join thousands of happy customers enjoying our daily deals</p>
        <router-link to="/menu" @click="scrollToTop()" class="cta-btn">
          View Full Menu
          <i class="icon-arrow-right"></i>
        </router-link>
      </div>
      <div class="cta-decoration">
        <div class="cta-shape"></div>
        <div class="cta-shape secondary"></div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Promo",
  data() {
    return {
      weekdays: [
        {
          name: "Monday",
          slots: [
            {
              time: "07:00 - 09:00",
              promotion: {
                name: "Breakfast Time",
                detail: "10% Discount",
                icon: "icon-coffee",
              },
              colorClass: "brown",
            },
            {
              time: "10:00 - 14:00",
              promotion: {
                name: "Happy Lunch",
                detail: "Free Drink",
                icon: "icon-drink",
              },
              colorClass: "beige",
            },
            {
              time: "15:00 - 17:00",
              promotion: null,
            },
          ],
        },
        {
          name: "Tuesday",
          slots: [
            {
              time: "07:00 - 09:00",
              promotion: {
                name: "Breakfast Time",
                detail: "10% Discount",
                icon: "icon-coffee",
              },
              colorClass: "brown",
            },
            {
              time: "10:00 - 14:00",
              promotion: null,
            },
            {
              time: "15:00 - 17:00",
              promotion: {
                name: "Afternoon Snack",
                detail: "20% OFF Nachos",
                icon: "icon-snack",
              },
              colorClass: "earth",
            },
          ],
        },
        {
          name: "Wednesday",
          slots: [
            {
              time: "07:00 - 09:00",
              promotion: {
                name: "Breakfast Time",
                detail: "10% Discount",
                icon: "icon-coffee",
              },
              colorClass: "brown",
            },
            {
              time: "10:00 - 14:00",
              promotion: {
                name: "Happy Lunch",
                detail: "Free Drink",
                icon: "icon-drink",
              },
              colorClass: "beige",
            },
            {
              time: "15:00 - 17:00",
              promotion: null,
            },
          ],
        },
        {
          name: "Thursday",
          slots: [
            {
              time: "07:00 - 09:00",
              promotion: {
                name: "Breakfast Time",
                detail: "10% Discount",
                icon: "icon-coffee",
              },
              colorClass: "brown",
            },
            {
              time: "10:00 - 14:00",
              promotion: null,
            },
            {
              time: "15:00 - 17:00",
              promotion: {
                name: "Afternoon Snack",
                detail: "20% OFF Nachos",
                icon: "icon-snack",
              },
              colorClass: "earth",
            },
          ],
        },
        {
          name: "Friday",
          slots: [
            {
              time: "07:00 - 09:00",
              promotion: {
                name: "Breakfast Time",
                detail: "10% Discount",
                icon: "icon-coffee",
              },
              colorClass: "brown",
            },
            {
              time: "10:00 - 14:00",
              promotion: {
                name: "Happy Lunch",
                detail: "Free Drink",
                icon: "icon-drink",
              },
              colorClass: "beige",
            },
            {
              time: "15:00 - 17:00",
              promotion: null,
            },
          ],
        },
      ],
    };
  },
  methods: {
    scrollToTop() {
      window.scrollTo({ top: 0, behavior: "smooth" });
    },
  },
};
</script>

<style scoped>
/* Global Styles */
.promotions {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen,
    Ubuntu, Cantarell, sans-serif;
  color: #1a1a1a;
  overflow-x: hidden;
}

/* Hero Section */
.hero-section {
  position: relative;
  padding: 100px 9% 80px;
  background: linear-gradient(135deg, #f5f5f5 0%, #ffffff 100%);
  overflow: hidden;
}

.hero-content {
  position: relative;
  z-index: 2;
  max-width: 600px;
}

.hero-badge {
  display: inline-block;
  padding: 8px 20px;
  background: #27ae60;
  color: white;
  border-radius: 30px;
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 20px;
  animation: fadeInUp 0.6s ease-out;
}

.hero-title {
  font-size: clamp(2.5rem, 5vw, 4rem);
  font-weight: 800;
  margin-bottom: 20px;
  line-height: 1.2;
  animation: fadeInUp 0.8s ease-out 0.1s both;
}

.hero-title .highlight {
  display: block;
  background: linear-gradient(135deg, #27ae60 0%, #2ecc71 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: 1.25rem;
  color: #666;
  animation: fadeInUp 1s ease-out 0.2s both;
}

.hero-decoration {
  position: absolute;
  right: 10%;
  top: 50%;
  transform: translateY(-50%);
}

.floating-circle {
  width: 300px;
  height: 300px;
  background: linear-gradient(135deg, #27ae60 0%, #2ecc71 100%);
  border-radius: 50%;
  opacity: 0.1;
  animation: float 6s ease-in-out infinite;
}

.floating-circle.secondary {
  width: 200px;
  height: 200px;
  position: absolute;
  top: -50px;
  right: -50px;
  animation-delay: -3s;
}

/* Section Headers */
.section-header {
  text-align: center;
  margin-bottom: 60px;
  animation: fadeIn 0.8s ease-out;
}

.section-title {
  font-size: clamp(2rem, 4vw, 3rem);
  font-weight: 800;
  margin-bottom: 10px;
  color: #1a1a1a;
}

.section-subtitle {
  font-size: 1.125rem;
  color: #666;
}

/* Schedule Section */
.schedule-section {
  padding: 80px 9%;
  background: #ffffff;
}

.schedule-wrapper {
  overflow-x: auto;
  padding-bottom: 20px;
}

.schedule-container {
  display: grid;
  grid-template-columns: repeat(5, 1fr) 1.5fr;
  gap: 20px;
  min-width: 900px;
}

.day-column {
  background: #f8f9fa;
  border-radius: 20px;
  padding: 20px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.day-column:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.day-header {
  font-size: 1.125rem;
  font-weight: 700;
  margin-bottom: 20px;
  text-align: center;
  color: #1a1a1a;
}

.time-slot {
  margin-bottom: 15px;
}

.promotion-card {
  background: white;
  border-radius: 15px;
  padding: 20px;
  position: relative;
  overflow: hidden;
  transition: transform 0.3s ease;
}

.promotion-card:hover {
  transform: scale(1.05);
}

.promotion-card.brown {
  background: linear-gradient(135deg, #876445 0%, #a17855 100%);
  color: white;
}

.promotion-card.beige {
  background: linear-gradient(135deg, #ca955c 0%, #d4a76a 100%);
  color: white;
}

.promotion-card.earth {
  background: linear-gradient(135deg, #eddfb3 0%, #f5eac5 100%);
  color: #876445;
}

.promotion-time {
  font-size: 0.75rem;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.promotion-name {
  font-size: 1rem;
  font-weight: 700;
  margin-bottom: 5px;
}

.promotion-detail {
  font-size: 0.875rem;
  opacity: 0.9;
}

.promotion-icon {
  position: absolute;
  right: 15px;
  bottom: 15px;
  font-size: 1.5rem;
  opacity: 0.3;
}

.empty-slot {
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ddd;
  font-size: 1.5rem;
}

/* Weekend Section */
.weekend-wrapper {
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
  border-radius: 20px;
  padding: 20px;
  color: white;
}

.weekend-header {
  font-size: 1.125rem;
  font-weight: 700;
  margin-bottom: 20px;
  text-align: center;
}

.weekend-card {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 15px;
  padding: 30px;
  text-align: center;
  position: relative;
  overflow: hidden;
  backdrop-filter: blur(10px);
}

.weekend-time {
  font-size: 0.875rem;
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  opacity: 0.8;
}

.weekend-card h4 {
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 10px;
}

.weekend-card p {
  font-size: 1.125rem;
  color: #76ba99;
  font-weight: 600;
}

.weekend-decoration {
  position: absolute;
  right: -20px;
  bottom: -20px;
  font-size: 5rem;
  opacity: 0.1;
  transform: rotate(-15deg);
}

/* Offers Section */
.offers-section {
  padding: 80px 9%;
  background: #f8f9fa;
}

.offers-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 40px;
}

.offer-card {
  background: white;
  border-radius: 25px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  animation: fadeInUp 0.8s ease-out;
}

.offer-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

.offer-image {
  position: relative;
  height: 250px;
  overflow: hidden;
}

.offer-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.offer-card:hover .offer-image img {
  transform: scale(1.1);
}

.offer-badge {
  position: absolute;
  top: 20px;
  right: 20px;
  background: #27ae60;
  color: white;
  padding: 10px 20px;
  border-radius: 30px;
  font-weight: 700;
  font-size: 0.875rem;
  animation: pulse 2s ease-in-out infinite;
}

.offer-badge.orange {
  background: #f39c12;
}

.offer-badge.purple {
  background: #9b59b6;
}

.offer-content {
  padding: 30px;
}

.offer-content h3 {
  font-size: 1.75rem;
  font-weight: 700;
  margin-bottom: 20px;
  color: #1a1a1a;
}

.offer-features {
  list-style: none;
  padding: 0;
  margin: 0 0 30px 0;
}

.offer-features li {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
  color: #666;
}

.offer-features i {
  color: #27ae60;
  font-size: 1.125rem;
}

.offer-btn {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  background: linear-gradient(135deg, #27ae60 0%, #2ecc71 100%);
  color: white;
  padding: 15px 30px;
  border-radius: 30px;
  text-decoration: none;
  font-weight: 600;
  transition: all 0.3s ease;
}

.offer-btn:hover {
  transform: translateX(5px);
  box-shadow: 0 10px 20px rgba(39, 174, 96, 0.3);
}

/* CTA Section */
.cta-section {
  position: relative;
  padding: 100px 9%;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
  text-align: center;
  color: white;
  overflow: hidden;
}

.cta-content {
  position: relative;
  z-index: 2;
}

.cta-content h2 {
  font-size: clamp(2rem, 4vw, 3rem);
  font-weight: 800;
  margin-bottom: 20px;
}

.cta-content p {
  font-size: 1.25rem;
  margin-bottom: 40px;
  opacity: 0.8;
}

.cta-btn {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  background: white;
  color: #1a1a1a;
  padding: 20px 40px;
  border-radius: 50px;
  text-decoration: none;
  font-weight: 700;
  font-size: 1.125rem;
  transition: all 0.3s ease;
}

.cta-btn:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 40px rgba(255, 255, 255, 0.2);
}

.cta-decoration {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.cta-shape {
  width: 400px;
  height: 400px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 50%;
  animation: rotate 20s linear infinite;
}

.cta-shape.secondary {
  width: 300px;
  height: 300px;
  position: absolute;
  top: 50px;
  left: 50px;
  animation-direction: reverse;
}

/* Animations */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-30px);
  }
}

@keyframes pulse {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* Icons (you can replace with actual icon fonts) */
[class^="icon-"]:before {
  font-family: "Font Awesome 6 Free";
  font-weight: 900;
}

.icon-clock:before {
  content: "🕐";
}
.icon-coffee:before {
  content: "☕";
}
.icon-drink:before {
  content: "🥤";
}
.icon-snack:before {
  content: "🍿";
}
.icon-dinner:before {
  content: "🍽️";
}
.icon-check:before {
  content: "✓";
}
.icon-arrow-right:before {
  content: "→";
}

/* Responsive Design */
@media (max-width: 1024px) {
  .hero-section {
    padding: 80px 5% 60px;
  }

  .schedule-section,
  .offers-section,
  .cta-section {
    padding: 60px 5%;
  }

  .hero-decoration {
    opacity: 0.5;
  }
}

@media (max-width: 768px) {
  .hero-title {
    font-size: 2.5rem;
  }

  .hero-subtitle {
    font-size: 1.125rem;
  }

  .section-title {
    font-size: 2rem;
  }

  .floating-circle {
    width: 200px;
    height: 200px;
  }

  .floating-circle.secondary {
    width: 150px;
    height: 150px;
  }

  .schedule-container {
    grid-template-columns: repeat(5, 200px) 300px;
  }

  .offers-grid {
    grid-template-columns: 1fr;
    gap: 30px;
  }

  .cta-shape {
    width: 300px;
    height: 300px;
  }

  .cta-shape.secondary {
    width: 200px;
    height: 200px;
  }
}

@media (max-width: 576px) {
  .hero-section {
    padding: 60px 5% 40px;
  }

  .hero-title {
    font-size: 2rem;
  }

  .hero-subtitle {
    font-size: 1rem;
  }

  .section-header {
    margin-bottom: 40px;
  }

  .section-title {
    font-size: 1.75rem;
  }

  .section-subtitle {
    font-size: 1rem;
  }

  .day-column {
    padding: 15px;
  }

  .promotion-card {
    padding: 15px;
  }

  .offer-content {
    padding: 20px;
  }

  .offer-content h3 {
    font-size: 1.5rem;
  }

  .offer-btn {
    padding: 12px 24px;
    font-size: 1rem;
  }

  .cta-btn {
    padding: 16px 32px;
    font-size: 1rem;
  }

  .hero-decoration,
  .cta-decoration {
    display: none;
  }
}

/* Scrollbar Styling */
.schedule-wrapper::-webkit-scrollbar {
  height: 8px;
}

.schedule-wrapper::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.schedule-wrapper::-webkit-scrollbar-thumb {
  background: #27ae60;
  border-radius: 10px;
}

.schedule-wrapper::-webkit-scrollbar-thumb:hover {
  background: #219a52;
}
</style>
