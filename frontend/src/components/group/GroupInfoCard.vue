<template>
  <v-card class="group-card" hover @click="goToGroupPage">
    <div class="card-content">
      <!-- 왼쪽 정보 영역 -->
      <div class="group-info">
        <!-- 그룹 제목 -->
        <div class="group-header">
          <h3 class="group-title">{{ group.name }}</h3>
        </div>

        <!-- 날짜 섹션 -->
        <div class="date-section">
          <div class="date-display">{{ group.startDate }} ~ {{ group.endDate }}</div>
        </div>

        <!-- 역할 제한 섹션 -->
        <div class="role-limits">
          <div class="limit-grid">
            <div class="limit-item">
              <span class="limit-label">재무:</span>
              <span class="limit-value">{{ group.roleLimits.finance }}</span>
            </div>
            <div class="limit-item">
              <span class="limit-label">일정:</span>
              <span class="limit-value">{{ group.roleLimits.schedule }}</span>
            </div>
            <div class="limit-item">
              <span class="limit-label">물류:</span>
              <span class="limit-value">{{ group.roleLimits.logistics }}</span>
            </div>
          </div>
        </div>

        <!-- 상태 표시 섹션 -->
        <div class="status-section">
          <span class="status-text">{{ statusText }}</span>
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
  }
  return statusMap[props.group.status] || props.group.status
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
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.3s ease; /* 애니메이션 속도 조정 */
  cursor: pointer;
  border: 1px solid #e0e0e0;
  position: relative; /* 호버 효과를 위한 기준점 */
}

/* === 호버 효과 === */
/* 카드 호버 시 약간 위로 이동하는 효과 */
.group-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

/* 호버 시 이미지 확장 효과 - 너비나 속도 조정 가능 */
.group-card:hover .group-image {
  width: 100%;
  position: absolute;
  right: 0;
  top: 0;
  z-index: 1;
  transition: all 0.5s ease;
}

/* 호버 시 정보 오버레이 효과 */
.group-card:hover .group-info {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.1); /* 배경 투명도 조정 가능 */
  color: white;
  z-index: 2;
  display: flex;
  flex-direction: column;
  padding: 14px;
  gap: 0;
  transition: all 0.3s ease;
}

/* 호버 시 모든 텍스트 색상 변경 */
.group-card:hover .group-title,
.group-card:hover .date-display,
.group-card:hover .limit-label,
.group-card:hover .limit-value,
.group-card:hover .status-text {
  color: white;
}

/* === 레이아웃 구조 === */
.card-content {
  display: flex;
  height: 100%;
}

/* 정보 영역 스타일 - 피그마 기준으로 수정 */
.group-info {
  width: 50%;
  padding: 14px;
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;
  z-index: 2;
  color: #2c2c2c;
}

/* 이미지 영역 스타일 - 피그마 기준으로 수정 */
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

/* === 컨텐츠 스타일 === */
/* 제목 영역 - 피그마 기준으로 수정 */
.group-header {
  display: flex;
  align-items: flex-start;
  width: 100%;
  margin-bottom: 38px;
}

/* 제목 텍스트 스타일 - 피그마 기준으로 수정 */
.group-title {
  font-family: 'Inter', sans-serif;
  font-size: 24px;
  font-weight: 400;
  margin: 0;
  line-height: 1.2;
  transition: color 0.3s ease;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: #2c2c2c;
}

/* 날짜 섹션 - 피그마 기준으로 수정 */
.date-section {
  margin-bottom: 39px;
  width: 100%;
}

/* 날짜 텍스트 스타일 - 피그마 기준으로 수정 */
.date-display {
  font-family: 'Inter', sans-serif;
  font-size: 15px;
  font-weight: 400;
  transition: color 0.3s ease;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: #2c2c2c;
}

/* 역할 제한 섹션 - 피그마 기준으로 수정 */
.role-limits {
  margin-bottom: 48px;
  width: 100%;
}

/* 역할 제한 그리드 레이아웃 - 피그마 기준으로 수정 */
.limit-grid {
  display: flex;
  gap: 18px;
  width: 100%;
}

/* 각 역할 항목 스타일 - 피그마 기준으로 수정 */
.limit-item {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 5px;
  white-space: nowrap;
}

/* 역할 라벨과 값 스타일 - 피그마 기준으로 수정 */
.limit-label,
.limit-value {
  font-family: 'Inter', sans-serif;
  font-size: 15px;
  font-weight: 400;
  transition: color 0.3s ease;
  color: #2c2c2c;
}

/* 상태 섹션 - 피그마 기준으로 수정 */
.status-section {
  margin-top: 0;
  width: 100%;
}

/* 상태 텍스트 스타일 - 피그마 기준으로 수정 */
.status-text {
  font-family: 'Inter', sans-serif;
  font-size: 15px;
  font-weight: 400;
  transition: color 0.3s ease;
  display: block;
  color: #2c2c2c;
}

/* === 반응형 디자인 === */
@media (max-width: 768px) {
  .group-card {
    height: auto;
    min-height: 265px;
  }

  /* 모바일에서는 이미지가 상단, 정보가 하단에 배치 */
  .card-content {
    flex-direction: column-reverse;
  }

  /* 모바일 이미지 크기 조정 */
  .group-image {
    width: 100%;
    height: 150px;
  }

  /* 모바일 정보 영역 패딩 조정 */
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
}
</style>
