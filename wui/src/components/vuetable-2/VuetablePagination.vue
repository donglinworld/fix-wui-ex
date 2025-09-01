<template>
  <div class="vuetable-pagination">
    <button 
      class="pagination-btn"
      @click="changePage('prev')"
      :disabled="isOnFirstPage"
    >
      <i class="fas fa-chevron-left"></i>
    </button>

    <button 
      v-for="page in pages" 
      :key="page"
      class="pagination-btn"
      :class="{ active: isCurrentPage(page) }"
      @click="changePage(page)"
    >
      {{ page }}
    </button>

    <button 
      class="pagination-btn"
      @click="changePage('next')"
      :disabled="isOnLastPage"
    >
      <i class="fas fa-chevron-right"></i>
    </button>
  </div>
</template>

<script>
import { computed } from 'vue'

export default {
  name: 'VuetablePagination',
  props: {
    pagination: {
      type: Object,
      required: true
    }
  },
  emits: ['page-changed'],
  
  setup(props, { emit }) {
    const isOnFirstPage = computed(() => props.pagination.current_page === 1)
    const isOnLastPage = computed(() => props.pagination.current_page === props.pagination.last_page)
    
    const pages = computed(() => {
      const totalPages = props.pagination.last_page
      const currentPage = props.pagination.current_page
      const range = []
      
      let start = Math.max(1, currentPage - 2)
      let end = Math.min(totalPages, currentPage + 2)
      
      if (start > 1) {
        range.push(1)
        if (start > 2) range.push('...')
      }
      
      for (let i = start; i <= end; i++) {
        range.push(i)
      }
      
      if (end < totalPages) {
        if (end < totalPages - 1) range.push('...')
        range.push(totalPages)
      }
      
      return range
    })
    
    const isCurrentPage = (page) => page === props.pagination.current_page
    
    const changePage = (page) => {
      if (page === '...') return
      
      if (page === 'prev' && !isOnFirstPage.value) {
        emit('page-changed', props.pagination.current_page - 1)
      } else if (page === 'next' && !isOnLastPage.value) {
        emit('page-changed', props.pagination.current_page + 1)
      } else if (typeof page === 'number') {
        emit('page-changed', page)
      }
    }
    
    return {
      isOnFirstPage,
      isOnLastPage,
      pages,
      isCurrentPage,
      changePage
    }
  }
}
</script>

<style scoped>
.vuetable-pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 1rem 0;
  gap: 0.5rem;
}

.pagination-btn {
  padding: 0.5rem 1rem;
  border: 1px solid #ddd;
  background: white;
  cursor: pointer;
  border-radius: 4px;
  min-width: 40px;
}

.pagination-btn:hover:not(:disabled) {
  background: #f0f0f0;
}

.pagination-btn.active {
  background: #1976d2;
  color: white;
  border-color: #1976d2;
}

.pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
