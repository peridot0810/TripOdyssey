import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import { useKakao } from 'vue3-kakao-maps/@utils'

import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

const vuetify = createVuetify({
  components,
  directives,
})

useKakao(import.meta.env.VITE_KAKAO_JS_KEY)

const app = createApp(App)
useKakao('appkey')
app.use(createPinia())
app.use(router)
app.use(vuetify)

app.mount('#app')
