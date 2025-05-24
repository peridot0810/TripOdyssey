<template>
  <v-dialog v-model="dialog" max-width="600px" persistent>
    <v-card>
      <v-card-title class="text-h5 pa-6">
        <v-icon class="mr-2" color="primary">mdi-account-group-outline</v-icon>
        새 그룹 만들기
      </v-card-title>

      <v-divider></v-divider>

      <v-card-text class="pa-6">
        <v-form ref="form" v-model="formValid">
          <!-- Group Name -->
          <v-text-field
            v-model="formData.name"
            label="그룹 이름"
            variant="outlined"
            :rules="nameRules"
            required
            class="mb-4"
          ></v-text-field>

          <!-- Group Description -->
          <v-textarea
            v-model="formData.description"
            label="설명 (선택사항)"
            variant="outlined"
            rows="3"
            class="mb-4"
            hint="나중에 추가할 수 있습니다"
          ></v-textarea>

          <!-- Travel Dates -->
          <div class="mb-4">
            <h4 class="mb-3">여행 날짜 (선택사항)</h4>
            <v-row>
              <v-col cols="6">
                <v-text-field
                  v-model="formData.startDate"
                  label="시작일"
                  type="date"
                  variant="outlined"
                ></v-text-field>
              </v-col>
              <v-col cols="6">
                <v-text-field
                  v-model="formData.endDate"
                  label="종료일"
                  type="date"
                  variant="outlined"
                  :rules="endDateRules"
                ></v-text-field>
              </v-col>
            </v-row>
          </div>

          <!-- Role Limits -->
          <div>
            <h4 class="mb-3">역할별 인원 제한</h4>
            <v-row>
              <v-col cols="4">
                <v-text-field
                  v-model.number="formData.roleLimits.finance"
                  label="재무"
                  type="number"
                  variant="outlined"
                  min="1"
                  max="10"
                  :rules="roleRules"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="4">
                <v-text-field
                  v-model.number="formData.roleLimits.schedule"
                  label="일정"
                  type="number"
                  variant="outlined"
                  min="1"
                  max="10"
                  :rules="roleRules"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="4">
                <v-text-field
                  v-model.number="formData.roleLimits.logistics"
                  label="교통/숙소"
                  type="number"
                  variant="outlined"
                  min="1"
                  max="10"
                  :rules="roleRules"
                  required
                ></v-text-field>
              </v-col>
            </v-row>
          </div>

          <!-- Error Message -->
          <v-alert
            v-if="groupStore.error"
            type="error"
            class="mt-4"
            closable
            @click:close="groupStore.clearError()"
          >
            {{ groupStore.error }}
          </v-alert>
        </v-form>
      </v-card-text>

      <v-divider></v-divider>

      <v-card-actions class="pa-6">
        <v-spacer></v-spacer>
        <v-btn variant="text" @click="closeDialog">취소</v-btn>
        <v-btn
          color="primary"
          variant="elevated"
          @click="createGroup"
          :disabled="!formValid || groupStore.isLoading"
          :loading="groupStore.isLoading"
        >
          그룹 만들기
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useGroupListStore } from '@/stores/groupList'

const emit = defineEmits(['update:modelValue', 'group-created'])
const groupStore = useGroupListStore()

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
})

const dialog = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
})

const form = ref(null)
const formValid = ref(false)

const formData = ref({
  name: '',
  description: '',
  startDate: '',
  endDate: '',
  roleLimits: {
    finance: 1,
    schedule: 1,
    logistics: 1,
  },
})

// Validation rules
const nameRules = [
  (v) => !!v || '그룹 이름을 입력해주세요',
  (v) => (v && v.length >= 2) || '그룹 이름은 2글자 이상이어야 합니다',
  (v) => (v && v.length <= 50) || '그룹 이름은 50글자 이하여야 합니다',
]

const endDateRules = [
  (v) => !v || !formData.value.startDate || new Date(v) >= new Date(formData.value.startDate) || '종료일은 시작일보다 늦어야 합니다',
]

const roleRules = [
  (v) => !!v || '필수 입력 항목입니다',
  (v) => (v >= 1 && v <= 10) || '1~10 사이의 숫자를 입력해주세요',
]

const resetForm = () => {
  formData.value = {
    name: '',
    description: '',
    startDate: '',
    endDate: '',
    roleLimits: {
      finance: 1,
      schedule: 1,
      logistics: 1,
    },
  }
  if (form.value) {
    form.value.resetValidation()
  }
  groupStore.clearError()
}

const closeDialog = () => {
  dialog.value = false
  resetForm()
}

const createGroup = async () => {
  const { valid } = await form.value.validate()
  if (!valid) return

  const result = await groupStore.createGroup(formData.value)

  if (result.success) {
    // Create complete group object with form data + API response
    const completeGroup = {
      groupId: result.data.groupId,
      name: formData.value.name,
      description: formData.value.description || '',
      status: result.data.status,
      createdAt: result.data.createdAt,
      startDate: formData.value.startDate || '',
      endDate: formData.value.endDate || '',
      roleLimits: { ...formData.value.roleLimits }
    }

    emit('group-created', completeGroup)
    closeDialog()
  }
}

// Reset form when dialog opens
watch(dialog, (newValue) => {
  if (newValue) {
    resetForm()
  }
})
</script>

<style scoped>
.v-card-title {
  font-weight: 600;
}
</style>
