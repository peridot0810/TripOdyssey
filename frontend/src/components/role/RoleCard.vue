<template>
  <div
    class="role-card"
    :class="{
      selected: isSelected,
      unselected: !isSelected && hasSelection,
    }"
    @click="$emit('select', role.id)"
  >
    <div class="role-card-inner">
      <div class="role-icon-container">
        <v-icon
          :icon="role.icon"
          :size="48"
          :color="isSelected ? 'white' : 'primary'"
          class="role-icon"
        />
      </div>

      <div class="role-content">
        <h3 class="role-title">{{ role.name }}</h3>
        <p class="role-subtitle">{{ role.subtitle }}</p>
      </div>

      <div class="selection-indicator" v-if="isSelected">
        <v-icon icon="mdi-check-circle" color="white" size="24" />
      </div>
    </div>

    <!-- Glowing border effect when selected -->
    <div class="glow-border" v-if="isSelected"></div>
  </div>
</template>

<script setup>
defineProps({
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

defineEmits(['select'])
</script>

<style scoped>
.role-card {
  position: relative;
  background: linear-gradient(145deg, rgba(255, 255, 255, 0.1), rgba(255, 255, 255, 0.05));
  backdrop-filter: blur(10px);
  border: 2px solid rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  padding: 24px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  height: 180px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.role-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.1), transparent);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.role-card:hover::before {
  opacity: 1;
}

.role-card:hover {
  transform: translateY(-8px) scale(1.02);
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.2);
  border-color: rgba(255, 255, 255, 0.4);
}

.role-card.selected {
  background: linear-gradient(145deg, rgba(25, 118, 210, 0.8), rgba(25, 118, 210, 0.6));
  border-color: rgba(255, 255, 255, 0.8);
  transform: translateY(-12px) scale(1.05);
  box-shadow: 0 20px 60px rgba(25, 118, 210, 0.4);
}

.role-card.unselected {
  opacity: 0.6;
  transform: scale(0.95);
}

.role-card-inner {
  position: relative;
  z-index: 2;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  height: 100%;
}

.role-icon-container {
  margin-bottom: 12px;
}

.role-icon {
  transition: all 0.3s ease;
}

.role-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.role-title {
  font-size: 1.2rem;
  font-weight: bold;
  color: white;
  margin-bottom: 4px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.role-subtitle {
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.9);
  margin: 0;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.selection-indicator {
  position: absolute;
  top: 8px;
  right: 8px;
}

.glow-border {
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  background: linear-gradient(45deg, #1976d2, #42a5f5, #1976d2, #42a5f5);
  border-radius: 18px;
  z-index: -1;
  animation: borderGlow 2s linear infinite;
}

@keyframes borderGlow {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .role-card {
    height: 160px;
    padding: 20px;
  }

  .role-title {
    font-size: 1.1rem;
  }

  .role-subtitle {
    font-size: 0.8rem;
  }
}
</style>
