<template>
  <v-dialog v-model="dialog" max-width="600px" persistent class="create-group-dialog">
    <v-card class="dialog-card">
      <v-card-title class="dialog-title">
        <v-icon class="mr-2" color="primary">mdi-account-group-outline</v-icon>
        Create New Group
      </v-card-title>

      <v-divider></v-divider>

      <v-card-text class="dialog-content">
        <v-form ref="form" v-model="formValid" class="group-form">
          <!-- Group Name -->
          <v-text-field
            v-model="formData.name"
            label="Group Name"
            variant="outlined"
            :rules="nameRules"
            required
            class="mb-4"
          >
            <template v-slot:prepend-inner>
              <v-icon>mdi-account-group</v-icon>
            </template>
          </v-text-field>

          <!-- Group Description -->
          <v-textarea
            v-model="formData.description"
            label="Description"
            variant="outlined"
            :rules="descriptionRules"
            rows="3"
            required
            class="mb-4"
          >
            <template v-slot:prepend-inner>
              <v-icon>mdi-text</v-icon>
            </template>
          </v-textarea>

          <!-- Date Range -->
          <div class="date-section mb-4">
            <h4 class="section-title">Travel Dates</h4>
            <v-row>
              <v-col cols="6">
                <v-text-field
                  v-model="formData.startDate"
                  label="Start Date"
                  type="date"
                  variant="outlined"
                  :rules="startDateRules"
                  required
                ></v-text-field>
              </v-col>
              <v-col cols="6">
                <v-text-field
                  v-model="formData.endDate"
                  label="End Date"
                  type="date"
                  variant="outlined"
                  :rules="endDateRules"
                  required
                ></v-text-field>
              </v-col>
            </v-row>
          </div>

          <!-- Role Limits -->
          <div class="role-limits-section">
            <h4 class="section-title">Role Limits</h4>
            <v-row>
              <v-col cols="4">
                <v-text-field
                  v-model.number="formData.roleLimits.finance"
                  label="Finance"
                  type="number"
                  variant="outlined"
                  min="1"
                  max="10"
                  :rules="roleRules"
                  required
                >
                  <template v-slot:prepend-inner>
                    <v-icon>mdi-currency-usd</v-icon>
                  </template>
                </v-text-field>
              </v-col>
              <v-col cols="4">
                <v-text-field
                  v-model.number="formData.roleLimits.schedule"
                  label="Schedule"
                  type="number"
                  variant="outlined"
                  min="1"
                  max="10"
                  :rules="roleRules"
                  required
                >
                  <template v-slot:prepend-inner>
                    <v-icon>mdi-calendar</v-icon>
                  </template>
                </v-text-field>
              </v-col>
              <v-col cols="4">
                <v-text-field
                  v-model.number="formData.roleLimits.logistics"
                  label="Logistics"
                  type="number"
                  variant="outlined"
                  min="1"
                  max="10"
                  :rules="roleRules"
                  required
                >
                  <template v-slot:prepend-inner>
                    <v-icon>mdi-truck</v-icon>
                  </template>
                </v-text-field>
              </v-col>
            </v-row>
          </div>
        </v-form>
      </v-card-text>

      <v-divider></v-divider>

      <v-card-actions class="dialog-actions">
        <v-spacer></v-spacer>
        <v-btn variant="text" @click="closeDialog" class="cancel-btn"> Cancel </v-btn>
        <v-btn
          color="primary"
          variant="elevated"
          @click="createGroup"
          :disabled="!formValid || isLoading"
          :loading="isLoading"
          class="create-btn"
        >
          Create Group
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'

const emit = defineEmits(['update:modelValue', 'group-created'])

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
const isLoading = ref(false)

const formData = ref({
  name: '',
  description: '',
  startDate: '',
  endDate: '',
  roleLimits: {
    finance: 2,
    schedule: 2,
    logistics: 2,
  },
})

// Validation rules
const nameRules = [
  (v) => !!v || 'Group name is required',
  (v) => (v && v.length >= 3) || 'Group name must be at least 3 characters',
  (v) => (v && v.length <= 50) || 'Group name must be less than 50 characters',
]

const descriptionRules = [
  (v) => !!v || 'Description is required',
  (v) => (v && v.length >= 10) || 'Description must be at least 10 characters',
  (v) => (v && v.length <= 200) || 'Description must be less than 200 characters',
]

const startDateRules = [
  (v) => !!v || 'Start date is required',
  (v) => new Date(v) >= new Date().setHours(0, 0, 0, 0) || 'Start date cannot be in the past',
]

const endDateRules = [
  (v) => !!v || 'End date is required',
  (v) => new Date(v) > new Date(formData.value.startDate) || 'End date must be after start date',
]

const roleRules = [
  (v) => !!v || 'This field is required',
  (v) => (v >= 1 && v <= 10) || 'Value must be between 1 and 10',
]

const resetForm = () => {
  formData.value = {
    name: '',
    description: '',
    startDate: '',
    endDate: '',
    roleLimits: {
      finance: 2,
      schedule: 2,
      logistics: 2,
    },
  }
  if (form.value) {
    form.value.resetValidation()
  }
}

const closeDialog = () => {
  dialog.value = false
  resetForm()
}

const createGroup = async () => {
  const { valid } = await form.value.validate()
  if (!valid) return

  isLoading.value = true

  try {
    // Simulate API call
    await new Promise((resolve) => setTimeout(resolve, 1000))

    // Create new group object
    const newGroup = {
      groupId: Date.now(), // In real app, this would come from the server
      name: formData.value.name,
      description: formData.value.description,
      status: 'planning',
      createdAt: new Date().toISOString(),
      startDate: formData.value.startDate,
      endDate: formData.value.endDate,
      roleLimits: { ...formData.value.roleLimits },
    }

    emit('group-created', newGroup)
    closeDialog()
  } catch (error) {
    console.error('Error creating group:', error)
  } finally {
    isLoading.value = false
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
.dialog-card {
  border-radius: 12px;
}

.dialog-title {
  font-size: 1.5rem;
  font-weight: 600;
  padding: 24px 24px 16px;
  color: #333;
}

.dialog-content {
  padding: 24px;
}

.section-title {
  font-size: 1rem;
  font-weight: 500;
  color: #333;
  margin-bottom: 12px;
}

.date-section {
  background-color: #f8f9fa;
  padding: 16px;
  border-radius: 8px;
}

.role-limits-section {
  background-color: #f8f9fa;
  padding: 16px;
  border-radius: 8px;
}

.dialog-actions {
  padding: 16px 24px 24px;
}

.cancel-btn {
  margin-right: 8px;
}

.create-btn {
  padding: 0 24px;
}

/* Custom styling for form fields */
:deep(.v-field) {
  border-radius: 8px;
}

:deep(.v-field--focused) {
  box-shadow: 0 0 0 2px rgba(25, 118, 210, 0.2);
}
</style>
