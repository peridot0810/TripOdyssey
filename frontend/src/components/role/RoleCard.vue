<template>
  <!-- 역할 카드 컨테이너 -->
  <div
    class="role-card"
    :class="{
      selected: isSelected,
      unselected: !isSelected && hasSelection,
    }"
    @click="$emit('select', role.id)"
  >
    <!-- 카드 내부 콘텐츠 (이미지 포함) -->
    <div class="role-card-inner">
      <div class="role-image-container">
        <img
          :src="getRoleImagePath()"
          :alt="role.name"
          class="role-image"
        />
      </div>
    </div>

    <!-- 선택 시 나타나는 그라데이션 외곽선 효과 -->
    <div class="gradient-border" v-if="isSelected"></div>
  </div>
</template>

<script setup>
const props = defineProps({
  role: {
    type: Object,
    required: true,
  },
  isSelected: {
    type: Boolean,
    default: false,
  },
  hasSelection: {
    type: Boolean,
    default: false,
  },
})

// 역할에 따른 이미지 경로 반환 함수
const getRoleImagePath = () => {
  // role.key에 따라 해당하는 _btn 이미지 반환
  if (props.role.key === 'scheduler') {
    return '/img/schedule_bnt.png'  // 파일명 오타 그대로 사용 (bnt)
  } else if (props.role.key === 'finance') {
    return '/img/finance_btn.png'
  } else if (props.role.key === 'logistics') {
    return '/img/logistics_btn.png'
  } else if (props.role.key === 'normal') {
    return '/img/normal_btn.png'
  }

  // 기본값
  return '/img/normal_btn.png'
}

defineEmits(['select'])
</script>

<style scoped>
/* ===== 메인 카드 스타일 ===== */
.role-card {
  position: relative;
  background: #D9D9D9; /* 카드 배경색 - 여기서 변경 가능 */
  border-radius: 20px; /* 카드 모서리 둥글기 - 여기서 변경 가능 */
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1); /* 애니메이션 속도 조절 */
  width: 162.5px; /* 카드 너비 - 여기서 변경 가능 */
  height: 162.5px; /* 카드 높이 - 여기서 변경 가능 */
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0px 8px 15px rgba(0, 0, 0, 0.3); /* 기본 그림자 효과 */
  margin: 0 30px; /* 카드 간 간격 - 여기서 변경 가능 */
}

/* ===== 호버 효과 ===== */
.role-card:hover {
  transform: translateY(-5px); /* 호버 시 위로 이동 거리 - 여기서 변경 가능 */
  box-shadow: 0px 12px 20px rgba(0, 0, 0, 0.4); /* 호버 시 그림자 효과 */
}

/* ===== 선택된 카드 스타일 ===== */
.role-card.selected {
  transform: translateY(-8px) scale(1.05); /* 선택 시 위로 이동 + 크기 증가 - 여기서 변경 가능 */
  box-shadow: 0 12px 24px rgba(25, 118, 210, 0.5); /* 선택 시 그림자 효과 */
}

/* ===== 선택되지 않은 카드 스타일 ===== */
.role-card.unselected {
  /* opacity: 0.7; */ /* 투명도 제거 */
}

/* ===== 카드 내부 콘텐츠 영역 ===== */
.role-card-inner {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden; /* 이미지가 카드 밖으로 나가지 않도록 */
  border-radius: 20px; /* 카드와 동일한 모서리 둥글기 */
  position: relative;
  z-index: 2; /* 그라데이션 테두리보다 위에 표시 */
}

/* ===== 이미지 컨테이너 ===== */
.role-image-container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}

/* ===== 역할 이미지 스타일 ===== */
.role-image {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 이미지 비율 유지하며 카드에 맞춤 */
}

/* ===== 선택 시 그라데이션 테두리 효과 ===== */
.gradient-border {
  position: absolute;
  top: -5px; /* 테두리 두께 조절 - 여기서 변경 가능 */
  left: -5px; /* 테두리 두께 조절 - 여기서 변경 가능 */
  right: -5px; /* 테두리 두께 조절 - 여기서 변경 가능 */
  bottom: -5px; /* 테두리 두께 조절 - 여기서 변경 가능 */
  border-radius: 25px; /* 테두리 모서리 둥글기 - 카드보다 5px 더 크게 */
  z-index: 1; /* 카드 뒤에 표시 */
  border: 5px solid transparent; /* 투명한 테두리 생성 */

    /* 그라데이션 배경 설정 */
    background:
    linear-gradient(#D9D9D9, #D9D9D9) padding-box, /* 내부 영역 색상 */
    linear-gradient(to right, #1976d2, #8E24AA, #6A1B9A, #42a5f5, #1976d2, #1976d2) border-box; /* 테두리 그라데이션 색상 - 여기서 색상 변경 가능 */

  background-size: 200% 100%; /* 그라데이션 크기 - 애니메이션 속도에 영향 */
  animation: gradientMove 5s linear infinite; /* 애니메이션 적용 - 5s는 속도 조절 가능 */
}

/* ===== 그라데이션 이동 애니메이션 ===== */
@keyframes gradientMove {
  0% {
    background-position: 0% 0%; /* 시작 위치 */
  }
  100% {
    background-position: 200% 0%; /* 끝 위치 - background-size와 일치 */
  }
}

/* ===== 반응형 디자인 (모바일) ===== */
@media (max-width: 768px) {
  .role-card {
    width: 130px; /* 모바일에서 카드 너비 */
    height: 130px; /* 모바일에서 카드 높이 */
    margin: 0 25px; /* 모바일에서 카드 간격 */
  }

  .gradient-border {
    top: -15px; /* 모바일에서 테두리 두께 */
    left: -15px;
    right: -15px;
    bottom: -15px;
    border-width: 15px; /* 모바일에서 테두리 두께 */
    border-radius: 35px; /* 모바일에서 테두리 모서리 둥글기 */
  }
}
</style>
