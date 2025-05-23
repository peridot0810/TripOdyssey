// dummy-data.js
export const communityPosts = [
  {
    postId: 1,
    title: '제주도 3박 4일 여행 후기 공유해요!',
    content:
      '안녕하세요! 지난주에 제주도 다녀왔는데 정말 좋았어요. 특히 성산일출봉에서 본 일출이 정말 인상깊었습니다. 맛집도 많이 다녔는데 흑돼지 맛집 추천드려요!',
    categoryId: 1,
    categoryName: 'Community',
    createdAt: '2025-05-20T14:30:00',
    views: 156,
    likes: 12,
    author: '여행러버',
    commentList: [
      {
        commentId: 1,
        postId: 1,
        content: '저도 제주도 가고 싶어요! 맛집 정보 더 알려주세요~',
        writer: '맛집탐험가',
        createdAt: '2025-05-20T15:45:00',
      },
      {
        commentId: 2,
        postId: 1,
        content: '성산일출봉 일출 정말 예쁘죠! 사진도 올려주세요',
        writer: '사진작가',
        createdAt: '2025-05-20T16:20:00',
      },
    ],
  },
  {
    postId: 2,
    title: '부산 여행 메이트 구해요! (6월 1-3일)',
    content:
      '6월 1일부터 3일까지 부산 여행 계획하고 있어요. 혼자 가기 아쉬워서 같이 갈 분 찾습니다! 20대 여성이고, 맛집 투어와 해운대, 감천마을 등 관광지 위주로 다닐 예정입니다.',
    categoryId: 2,
    categoryName: 'Recruit',
    createdAt: '2025-05-21T09:15:00',
    views: 89,
    likes: 7,
    author: '부산가자',
    commentList: [
      {
        commentId: 3,
        postId: 2,
        content: '저도 관심있어요! 메시지 보내드릴게요',
        writer: '바다사랑',
        createdAt: '2025-05-21T10:30:00',
      },
    ],
  },
  {
    postId: 3,
    title: '유럽 배낭여행 준비 중인데 조언 부탁드려요',
    content:
      '7월에 유럽 한 달 배낭여행을 계획하고 있는데, 처음이라 막막해요. 경험 있으신 분들의 조언을 구합니다. 특히 짐싸기와 교통편 예약 관련해서 팁이 있다면 공유해주세요!',
    categoryId: 1,
    categoryName: 'Community',
    createdAt: '2025-05-21T18:45:00',
    views: 203,
    likes: 18,
    author: '배낭여행초보',
    commentList: [
      {
        commentId: 4,
        postId: 3,
        content: '유럽 3번 다녀온 경험으로는 짐은 최대한 가볍게! 현지에서 사는게 나아요',
        writer: '유럽전문가',
        createdAt: '2025-05-21T19:20:00',
      },
      {
        commentId: 5,
        postId: 3,
        content: '유레일패스 미리 끊어두시고, 숙소는 호스텔 추천해요',
        writer: '배낭여행러',
        createdAt: '2025-05-21T20:15:00',
      },
    ],
  },
  {
    postId: 4,
    title: '강릉 펜션 같이 잡으실 분 있나요?',
    content:
      '5월 마지막 주말에 강릉 펜션 예약하려는데 혼자 가기엔 비용이 부담되어서요. 1박 2일로 계획하고 있고, 바다 구경하고 커피거리 돌아다닐 예정입니다!',
    categoryId: 2,
    categoryName: 'Recruit',
    createdAt: '2025-05-22T11:30:00',
    views: 67,
    likes: 4,
    author: '강릉가고파',
    commentList: [],
  },
  {
    postId: 5,
    title: '일본 오사카 맛집 리스트 정리했어요',
    content:
      '작년에 오사카 다녀와서 맛집 리스트 정리해뒀는데 공유드려요! 다코야키는 역시 도톤보리가 최고고, 라멘은 이치란보다 현지 작은 가게들이 더 맛있더라구요. 상세 리스트는 댓글로 남겨드릴게요!',
    categoryId: 1,
    categoryName: 'Community',
    createdAt: '2025-05-22T16:20:00',
    views: 134,
    likes: 9,
    author: '일본맛집킬러',
    commentList: [
      {
        commentId: 6,
        postId: 5,
        content: '우와 감사해요! 다음달에 오사카 가는데 참고할게요',
        writer: '오사카준비중',
        createdAt: '2025-05-22T17:10:00',
      },
    ],
  },
]

export const categories = [
  { id: 1, name: 'Community', label: '커뮤니티', color: 'primary' },
  { id: 2, name: 'Recruit', label: '동행구해요', color: 'secondary' },
]

export const currentUser = '여행러버' // 현재 로그인한 사용자
