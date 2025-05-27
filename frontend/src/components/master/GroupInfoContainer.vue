<template>
  <div class="group-info-container">
    <!-- Toggle Edit Button -->
    <div class="header-actions">
      <button @click="toggleEdit" class="edit-toggle-btn">편집</button>
    </div>

    <!-- Content - Always Display Mode -->
    <div class="content">
      <GroupInfoDisplay />
    </div>

    <!-- Edit Modal -->
    <div v-if="isEditing" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>그룹 정보 편집</h3>
          <button @click="closeModal" class="close-btn">×</button>
        </div>

        <div class="modal-body">
          <GroupInfoEdit ref="editComponent" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import GroupInfoDisplay from './GroupInfoDisplay.vue'
import GroupInfoEdit from './GroupInfoEdit.vue'

const isEditing = ref(false)
const editComponent = ref(null)

const toggleEdit = () => {
  isEditing.value = true
}

const closeModal = () => {
  // Reset the edit form when closing
  if (editComponent.value && editComponent.value.updateEditForm) {
    editComponent.value.updateEditForm()
  }
  isEditing.value = false
}
</script>

<style scoped>
.group-info-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.header-actions {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 1rem;
}

.edit-toggle-btn {
  padding: 0.5rem 1rem;
  border: 1px solid #007bff;
  background: white;
  color: #007bff;
  border-radius: 4px;
  cursor: pointer;
}

.edit-toggle-btn:hover {
  background: #007bff;
  color: white;
}

.content {
  flex: 1;
  overflow: hidden;
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 8px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh; /* Increased from 80vh to 90vh */
  overflow: hidden;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid #eee;
  background: #f8f9fa;
}

.modal-header h3 {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 600;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #666;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
}

.close-btn:hover {
  background: #e9ecef;
  color: #000;
}

.modal-body {
  flex: 1;
  padding: 1.5rem;
  overflow-y: auto;
}
</style>
