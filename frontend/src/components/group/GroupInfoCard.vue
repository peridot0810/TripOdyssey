<template>
  <v-card class="group-card" hover @click="goToGroupPage">
    <div class="card-content">
      <!-- 왼쪽 정보 영역 -->
      <div class="group-info">
        <!-- 그룹 제목 -->
        <div class="group-header">
          <h3 class="group-title">{{ group.name }}</h3>
        </div>

        <!-- 날짜 섹션 - 호버 시에만 표시 -->
        <div class="date-section hover-only">
          <div class="date-container">
            <v-chip
              class="date-chip"
              size="small"
              variant="flat"
              color="primary"
              prepend-icon="mdi-calendar"
            >
              {{ formattedStartDate }}
            </v-chip>

            <v-icon class="date-separator" size="small" color="primary"> mdi-arrow-right </v-icon>

            <v-chip
              class="date-chip"
              size="small"
              variant="flat"
              color="primary"
              append-icon="mdi-calendar-check"
            >
              {{ formattedEndDate }}
            </v-chip>
          </div>
        </div>

        <!-- 상태 표시 섹션 -->
        <div class="status-section">
          <v-chip
            class="status-chip"
            :color="statusColor"
            :variant="statusVariant"
            size="large"
            :prepend-icon="statusIcon"
            elevation="2"
          >
            <span class="status-text">{{ statusText }}</span>
          </v-chip>
        </div>
      </div>

      <!-- 오른쪽 이미지 영역 -->
      <div class="group-image">
        <v-img :src="groupImageUrl" :alt="group.name" cover class="image">
          <template v-slot:placeholder>
            <div class="d-flex align-center justify-center fill-height">
              <v-progress-circular indeterminate color="primary"></v-progress-circular>
            </div>
          </template>
        </v-img>
      </div>
    </div>
  </v-card>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

// 컴포넌트 Props 정의
const props = defineProps({
  group: {
    type: Object,
    required: true,
  },
})

const router = useRouter()

// 그룹 이미지 URL - 실제 이미지로 교체 가능
const groupImageUrl = computed(() => {
  if (!props.group?.imageUrl) {
    return 'https://fastly.picsum.photos/id/20/3670/2462.jpg?hmac=CmQ0ln-k5ZqkdtLvVO23LjVAEabZQx2wOaT4pyeG10I' // fallback image
  }

  // If it's already a full URL, return as is
  if (props.group.imageUrl.startsWith('http')) {
    return props.group.imageUrl
  }

  // If it's a relative path, construct full URL
  const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
  return `${API_BASE_URL}${props.group.imageUrl}`
})

// 상태 텍스트 계산 - 상태에 따라 한글로 표시
const statusText = computed(() => {
  const statusMap = {
    planning: '계획 중',
    in_progress: '진행 중',
    completed: '완료',
    canceled: '취소됨',
  }
  return statusMap[props.group.status] || props.group.status
})

// 날짜 포맷팅 함수 - YYYY-MM-DD를 YY년MM월DD일로 변환
const formatDate = (dateString) => {
  if (!dateString) return ''

  // YYYY-MM-DD 형식 파싱
  const [year, month, day] = dateString.split('-')
  const shortYear = year.slice(-2) // 마지막 2자리만

  return `${shortYear}년${parseInt(month)}월${parseInt(day)}일`
}

const formattedStartDate = computed(() => formatDate(props.group.startDate))
const formattedEndDate = computed(() => formatDate(props.group.endDate))

// 상태 클래스 계산
const statusClass = computed(() => {
  return `status-${props.group.status}`
})

// 그룹 페이지로 이동하는 함수
const goToGroupPage = () => {
  router.push(`/group/${props.group.groupId}`)
}
</script>

<style scoped>
/* === 기본 카드 스타일 === */
.group-card {
  width: 100%;
  height: 265px;
  border-radius: 20px;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
  border: 3px solid #e2e8f0;
  position: relative;
  font-family:
    'Pretendard', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', '맑은 고딕', sans-serif;
  box-shadow:
    0 8px 32px rgba(0, 0, 0, 0.1),
    0 0 0 1px rgba(255, 255, 255, 0.3) inset;
  background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
}

/* 상단 컬러 스트라이프 추가 */
.group-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #f97316 0%, #ec4899 33%, #3b82f6 66%, #4ecdc4 100%);
  border-radius: 20px 20px 0 0;
  z-index: 3;
}

/* === 호버 효과 === */
.group-card:hover {
  transform: translateY(-6px) scale(1.02);
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.2);
  border-color: #4ecdc4;
}

.group-card:hover .group-image {
  width: 100%;
  position: absolute;
  right: 0;
  top: 0;
  z-index: 1;
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.group-card:hover .group-info {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(0, 0, 0, 0.85), rgba(0, 0, 0, 0.75));
  backdrop-filter: blur(4px);
  color: white;
  z-index: 2;
  display: flex;
  flex-direction: column;
  padding: 20px;
  gap: 0;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 20px;
}

/* 호버 시 텍스트 가시성 향상 */
.group-card:hover .group-title {
  color: white;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.5);
  font-weight: 700;
  transform: translateY(-2px);
}

/* Vuetify 컴포넌트 호버 효과 */
.group-card:hover .date-container {
  background: rgba(255, 255, 255, 0.2) !important;
  border: 2px solid rgba(255, 255, 255, 0.4) !important;
  backdrop-filter: blur(8px) !important;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3) !important;
  border-radius: 16px !important;
  padding: 8px !important;
}

.group-card:hover .roles-container {
  background: rgba(255, 255, 255, 0.2) !important;
  border: 2px solid rgba(255, 255, 255, 0.4) !important;
  backdrop-filter: blur(8px) !important;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3) !important;
}

.group-card:hover .role-label,
.group-card:hover .role-value {
  color: white !important;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.5) !important;
}

.group-card:hover .date-chip {
  background: rgba(255, 255, 255, 0.25) !important;
  color: white !important;
  border: 1px solid rgba(255, 255, 255, 0.3) !important;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.5) !important;
  font-weight: 700 !important;
}

.group-card:hover .status-chip {
  background: rgba(255, 255, 255, 0.25) !important;
  color: white !important;
  border: 2px solid rgba(255, 255, 255, 0.4) !important;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.5) !important;
  font-weight: 700 !important;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.3) !important;
}

/* 호버 시 아이콘 색상 변경 */
.group-card:hover .v-icon {
  color: white !important;
  filter: drop-shadow(0 1px 4px rgba(0, 0, 0, 0.5));
}

/* === 레이아웃 구조 === */
.card-content {
  display: flex;
  height: 100%;
}

.group-info {
  width: 50%;
  padding: 14px;
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;
  z-index: 2;
  color: #2c2c2c;
}

.group-image {
  width: 50%;
  height: 100%;
  flex-shrink: 0;
  transition: all 0.5s ease;
  background-color: #d9d9d9;
}

.image {
  width: 100%;
  height: 100%;
}

/* === 개선된 컨텐츠 스타일 === */

/* 제목 스타일 */
.group-header {
  display: flex;
  align-items: flex-start;
  width: 100%;
  margin-bottom: 20px;
}

.group-title {
  font-size: 24px;
  font-weight: 700;
  margin: 0;
  line-height: 1.3;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: #1e293b;
  letter-spacing: -0.8px;
  font-family: 'Pretendard', 'Apple SD Gothic Neo', sans-serif;
  background: linear-gradient(135deg, #229cff, #1d68d8);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 호버 시에만 표시되는 요소들 */
.hover-only {
  opacity: 0;
  visibility: hidden;
  transform: translateY(10px);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.group-card:hover .hover-only {
  opacity: 1;
  visibility: visible;
  transform: translateY(0);
}

/* === Vuetify 컴포넌트 커스텀 스타일 === */

/* 날짜 섹션 개선 */
.date-section {
  margin-bottom: 20px;
  width: 100%;
}

.date-section {
  margin-bottom: 20px;
  width: 100%;
}

.date-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 12px;
  background: rgba(59, 130, 246, 0.08);
  border: 2px solid rgba(59, 130, 246, 0.15);
  border-radius: 16px;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 16px rgba(59, 130, 246, 0.15);
}

.date-chip {
  font-family: 'Pretendard', 'Apple SD Gothic Neo', sans-serif !important;
  font-weight: 700 !important;
  font-size: 13px !important;
  letter-spacing: -0.3px !important;
  border-radius: 12px !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.2) !important;
  min-width: 120px !important;
  padding: 0 8px !important;
}

.date-separator {
  margin: 0 4px !important;
  opacity: 0.8 !important;
  transition: all 0.3s ease !important;
  transform: scale(1.2);
}

/* 상태 섹션 개선 */
.status-section {
  margin-top: 0;
  width: 100%;
  display: flex;
  justify-content: flex-start;
}

.status-chip {
  font-family: 'Pretendard', 'Apple SD Gothic Neo', sans-serif !important;
  font-weight: 700 !important;
  font-size: 14px !important;
  letter-spacing: -0.3px !important;
  border-radius: 20px !important;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1) !important;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15) !important;
  padding: 8px 16px !important;
  position: relative !important;
  overflow: hidden !important;
}

/* 상태 칩에 반짝이는 효과 추가 */
.status-chip::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s ease;
}

.status-chip:hover::before {
  left: 100%;
}

.status-text {
  font-weight: 700 !important;
  font-family: 'Pretendard', 'Apple SD Gothic Neo', sans-serif !important;
}

/* === 반응형 디자인 === */
@media (max-width: 768px) {
  .group-card {
    height: auto;
    min-height: 265px;
  }

  .card-content {
    flex-direction: column-reverse;
  }

  .group-image {
    width: 100%;
    height: 150px;
  }

  .group-info {
    padding: 14px;
    width: 100%;
  }

  .group-header {
    margin-bottom: 20px;
  }

  .date-section {
    margin-bottom: 20px;
  }

  .role-limits {
    margin-bottom: 20px;
  }

  .limit-grid {
    gap: 6px;
  }
}
</style>
