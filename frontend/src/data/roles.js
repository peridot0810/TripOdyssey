// Role background images - using key for background mapping
export const roleBackgrounds = {
  normal:
    'https://fastly.picsum.photos/id/201/5000/3333.jpg?hmac=NE8fOMa8u9PBfkq4AVwEoJdRqoPTNwUsyKvKWuXyNCk',
  scheduler:
    'https://fastly.picsum.photos/id/219/5000/3333.jpg?hmac=nQIYAD6CmzCXEG3xtEckzbu3iauIE3DpHiqnQy3gdG4',
  finance:
    'https://fastly.picsum.photos/id/237/3500/2095.jpg?hmac=y2n_cflHFKpQwLOL1SSCtVDqL8NmOnBzEW7LYKZ-z_o',
  logistics:
    'https://fastly.picsum.photos/id/244/4288/2848.jpg?hmac=R6j9PBP4aBk2vcEIoOPU4R_nuknizryn2Vq8GGtWTrM',
}

// Role definitions
export const rolesData = [
  {
    id: 2,
    key: 'scheduler',
    name: '일정 담당',
    subtitle: 'Schedule Manager',
    icon: 'mdi-calendar-clock',
    responsibilities: [
      '여행 일정 계획 및 관리',
      '관광지 및 액티비티 예약',
      '시간표 작성 및 공유',
      '일정 변경사항 공지',
      '효율적인 동선 계획',
    ],
    skills: ['계획성', '시간관리', '정보수집', '의사소통'],
    difficulty: 'medium',
    difficultyDescription: '다양한 정보를 종합하여 최적의 일정을 짜는 능력이 필요합니다.',
  },
  {
    id: 3,
    key: 'finance',
    name: '재무 담당',
    subtitle: 'Finance Manager',
    icon: 'mdi-currency-usd',
    responsibilities: [
      '여행 예산 계획 및 관리',
      '공동 경비 정산',
      '영수증 관리 및 기록',
      '비용 절약 방안 제안',
      '재무 현황 보고',
    ],
    skills: ['계산능력', '꼼꼼함', '절약정신', '투명성'],
    difficulty: 'easy',
    difficultyDescription: '기본적인 계산 능력과 꼼꼼함만 있으면 충분합니다.',
  },
  {
    id: 4,
    key: 'logistics',
    name: '교통/숙소 담당',
    subtitle: 'Logistics Coordinator',
    icon: 'mdi-car-multiple',
    responsibilities: [
      '교통수단 예약 및 관리',
      '숙박시설 예약',
      '이동 경로 최적화',
      '짐 보관 및 운송 관리',
      '교통비 및 숙박비 관리',
    ],
    skills: ['정보수집', '협상력', '문제해결', '체력'],
    difficulty: 'hard',
    difficultyDescription: '다양한 교통수단과 숙소 정보를 비교 분석하는 능력이 필요합니다.',
  },
  {
    id: 5,
    key: 'normal',
    name: '일반 멤버',
    subtitle: 'General Member',
    icon: 'mdi-account-group',
    responsibilities: [
      '여행 계획에 적극적으로 참여',
      '의견 제시 및 피드백 제공',
      '정해진 일정과 규칙 준수',
      '다른 멤버들과의 협력',
      '여행 분위기 메이커 역할',
    ],
    skills: ['소통능력', '협력정신', '융통성', '긍정성'],
    difficulty: 'easy',
    difficultyDescription: '특별한 책임 없이 여행을 즐기며 팀에 기여할 수 있습니다.',
  },
]
