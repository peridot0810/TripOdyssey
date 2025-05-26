<template>
  <div class="day-selector">
    <v-chip-group v-model="selectedDay" active-class="primary" mandatory class="day-chips">
    <v-chip
      v-for="day in availableDays"
      :key="day"
      :value="day"
      color="pink"
      variant="flat"
      size="large"
      class="day-chip d-flex align-center justify-center font-weight-bold text-white"
    >
      {{ day }}일차
    </v-chip>
    </v-chip-group>
  </div>
</template>

<script setup>
import { computed } from 'vue'

// Props
const props = defineProps({
  availableDays: {
    type: Array,
    required: true,
    default: () => [1]
  },
  modelValue: {
    type: Number,
    required: true,
    default: 1
  }
})

// Emits
const emit = defineEmits(['update:modelValue'])

// Two-way binding for selected day
const selectedDay = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})
</script>

<style scoped>
.day-selector {
  position: sticky;
  top: 0;
  background-color: white;
  z-index: 10;
  padding: 8px 0;
  border-radius: 8px;
  margin-bottom: 16px;
}

.day-chips {
  justify-content: center;
}

.day-chip {
  margin: 0 4px;
  transition: all 0.3s ease;
}

.day-chip:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* Responsive adjustments */
@media (max-width: 600px) {
  .day-chips {
    justify-content: flex-start;
    overflow-x: auto;
    padding-bottom: 8px;
  }

  .day-chip {
    flex-shrink: 0;
  }
}
</style>
