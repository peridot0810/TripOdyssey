<template>
  <v-card class="finance-card" elevation="2">
    <div class="card-content">
      <!-- Content Block (Image + Location or Icon) -->
      <div class="content-block">
        <div v-if="expense.contentId && contentImage && contentName" class="content-with-image">
          <div class="image-container">
            <v-img
              :src="contentImage"
              :alt="contentName"
              height="80"
              width="80"
              cover
              class="rounded"
            >
              <template v-slot:placeholder>
                <div class="d-flex align-center justify-center fill-height">
                  <v-progress-circular
                    indeterminate
                    color="primary"
                    size="20"
                  ></v-progress-circular>
                </div>
              </template>
            </v-img>
          </div>
          <div class="location-info">
            <div class="location-title text-caption font-weight-bold text-truncate">
              {{ contentName }}
            </div>
            <div class="location-address text-caption text-grey text-truncate">
              {{ contentAddress || '주소 정보 없음' }}
            </div>
          </div>
        </div>

        <div v-else class="content-without-image d-flex flex-column align-center justify-center">
          <v-icon color="grey-lighten-1" size="large">mdi-receipt</v-icon>
          <span class="text-caption text-grey-lighten-1 mt-1">일반 지출</span>
        </div>
      </div>

      <v-divider vertical class="divider"></v-divider>

      <!-- Category Block -->
      <div class="category-block d-flex align-center justify-center">
        <v-chip size="small" color="primary" variant="flat" class="text-caption">
          {{ expense.categoryName }}
        </v-chip>
      </div>

      <v-divider vertical class="divider"></v-divider>

      <!-- Description Block -->
      <div class="description-block d-flex align-center">
        <div class="description-text">
          <p class="text-body-2 ma-0">{{ expense.description }}</p>
        </div>
      </div>

      <v-divider vertical class="divider"></v-divider>

      <!-- DateTime Block -->
      <div class="datetime-block d-flex align-center justify-center">
        <div class="datetime-info text-center">
          <div class="date-text text-body-2 font-weight-medium">
            {{ formatDate(expense.datetime) }}
          </div>
          <div class="time-text text-caption text-grey">
            {{ formatTime(expense.datetime) }}
          </div>
        </div>
      </div>

      <v-divider vertical class="divider"></v-divider>

      <!-- Amount Block -->
      <div class="amount-block d-flex align-center justify-center">
        <div class="amount-info text-center">
          <div class="amount-text text-h6 font-weight-bold text-primary">
            {{ formatAmount(expense.amount) }}
          </div>
          <div class="currency-text text-caption text-grey">원</div>
        </div>
      </div>
    </div>
  </v-card>
</template>

<script setup>
//import { computed } from 'vue'

//const props =
defineProps({
  expense: {
    type: Object,
    required: true,
  },
  contentImage: {
    type: String,
    default: null,
  },
  contentName: {
    type: String,
    default: null,
  },
  contentAddress: {
    type: String,
    default: null,
  },
})

// Format date to Korean format
const formatDate = (datetime) => {
  const date = new Date(datetime)
  return date.toLocaleDateString('ko-KR', {
    month: 'short',
    day: 'numeric',
  })
}

// Format time
const formatTime = (datetime) => {
  const date = new Date(datetime)
  return date.toLocaleTimeString('ko-KR', {
    hour: '2-digit',
    minute: '2-digit',
    hour12: false,
  })
}

// Format amount with Korean number formatting
const formatAmount = (amount) => {
  return new Intl.NumberFormat('ko-KR').format(amount)
}
</script>

<style scoped>
.finance-card {
  width: 100%;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
  margin-bottom: 12px;
}

.finance-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15) !important;
}

.card-content {
  display: flex;
  align-items: stretch;
  min-height: 100px;
  padding: 12px;
}

.content-block {
  width: 120px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
}

.content-with-image {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.image-container {
  width: 80px;
  height: 80px;
  margin: 0 auto 8px auto;
  flex-shrink: 0;
}

.location-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.location-title {
  line-height: 1.2;
  margin-bottom: 2px;
}

.location-address {
  line-height: 1.2;
}

.content-without-image {
  height: 100%;
  min-height: 80px;
}

.category-block {
  width: 100px;
  flex-shrink: 0;
  padding: 0 8px;
}

.description-block {
  flex: 1;
  min-width: 150px;
  padding: 0 12px;
}

.description-text {
  word-break: break-word;
  hyphens: auto;
}

.datetime-block {
  width: 80px;
  flex-shrink: 0;
  padding: 0 8px;
}

.datetime-info {
  width: 100%;
}

.date-text {
  line-height: 1.2;
  margin-bottom: 2px;
}

.time-text {
  line-height: 1.2;
}

.amount-block {
  width: 100px;
  flex-shrink: 0;
  padding: 0 8px;
}

.amount-info {
  width: 100%;
}

.amount-text {
  line-height: 1.2;
  margin-bottom: 2px;
}

.currency-text {
  line-height: 1.2;
}

.divider {
  margin: 0 4px;
  opacity: 0.3;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .card-content {
    flex-direction: column;
    min-height: auto;
  }

  .content-block,
  .category-block,
  .datetime-block,
  .amount-block {
    width: 100%;
    flex-shrink: 1;
    margin-bottom: 8px;
  }

  .description-block {
    min-width: auto;
    margin-bottom: 8px;
  }

  .divider {
    display: none;
  }

  .content-with-image {
    flex-direction: row;
    align-items: center;
  }

  .image-container {
    margin: 0 12px 0 0;
  }

  .location-info {
    justify-content: flex-start;
  }
}
</style>
