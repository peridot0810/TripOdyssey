import { createRouter, createWebHistory } from 'vue-router'

// Views
import HomeView from '@/views/HomeView.vue'
import LoginView from '@/views/LoginView.vue'
import SignupView from '@/views/SignupView.vue'
import CommunityView from '@/views/CommunityView.vue'
import UserInfoView from '@/views/UserInfoView.vue'

// Group Layout and Views
import GroupLayout from '@/views/group/GroupLayout.vue'
import GroupMainView from '@/views/group/GroupMainView.vue'
import FinanceView from '@/views/group/FinanceView.vue'
import LogisticsView from '@/views/group/LogisticsView.vue'
import MapView from '@/views/group/MapView.vue'
import MeetingView from '@/views/group/MeetingView.vue'
import ScheduleView from '@/views/group/ScheduleView.vue'
import ChatView from '@/views/group/ChatView.vue'
import RoleSelectView from '@/views/group/RoleSelectView.vue'
import MasterView from '@/views/group/MasterView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/login', name: 'login', component: LoginView },
    { path: '/signup', name: 'signup', component: SignupView },
    { path: '/', name: 'home', component: HomeView },
    { path: '/community', name: 'community', component: CommunityView },
    { path: '/user-info', name: 'user-info', component: UserInfoView },

    {
      path: '/group/:groupId',
      component: GroupLayout,
      children: [
        { path: '', name: 'group-main', component: GroupMainView },
        { path: 'finance', name: 'group-finance', component: FinanceView },
        { path: 'logistics', name: 'group-logistics', component: LogisticsView },
        { path: 'map', name: 'group-map', component: MapView },
        { path: 'meeting', name: 'group-meeting', component: MeetingView },
        { path: 'schedule', name: 'group-schedule', component: ScheduleView },
        { path: 'chat', name: 'group-chat', component: ChatView },
        { path: 'role', name: 'role', component: RoleSelectView },
        { path: 'master', name: 'master', component: MasterView}
      ],
    },
  ],
})

export default router
