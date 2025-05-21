// groupData.js

export const groupData = [
  {
    groupId: 1,
    name: '부산 여름 여행',
    description: '친구들과 함께하는 신나는 여름 바다 여행!',
    status: 'planning',
    createdAt: '2025-05-01T10:15:00',
    startDate: '2025-07-20',
    endDate: '2025-07-24',
    roleLimits: {
      finance: 2,
      schedule: 3,
      logistics: 2,
    },
  },
  {
    groupId: 2,
    name: '제주도 힐링 여행',
    description: '제주의 아름다운 자연을 만끽하는 여행.',
    status: 'in_progress',
    createdAt: '2025-04-15T14:30:00',
    startDate: '2025-06-10',
    endDate: '2025-06-14',
    roleLimits: {
      finance: 1,
      schedule: 2,
      logistics: 3,
    },
  },
  {
    groupId: 3,
    name: '가을 단풍 산행',
    description: '가을 단풍을 즐기며 떠나는 등산 여행.',
    status: 'completed',
    createdAt: '2025-03-20T09:00:00',
    startDate: '2025-04-05',
    endDate: '2025-04-08',
    roleLimits: {
      finance: 1,
      schedule: 1,
      logistics: 1,
    },
  },
]
