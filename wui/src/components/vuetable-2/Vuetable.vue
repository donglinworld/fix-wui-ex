<template>
  <div class="vuetable-wrapper">
    <table :class="['vuetable', css.tableClass]">
      <thead>
        <tr>
          <th v-for="field in visibleFields" 
              :key="field.name"
              :class="[
                'vuetable-th-' + field.name, 
                field.titleClass, 
                { sortable: isSortable(field) }
              ]"
              @click="orderBy(field)"
          >
            {{ field.title }}
            <span v-if="isSortable(field)" class="sort-icon">
              <i :class="sortIcon(field)"></i>
            </span>
          </th>
        </tr>
      </thead>
      
      <tbody>
        <template v-for="(item, index) in tableData" :key="getObjectValue(item, trackBy)">
          <!-- Regular Row -->
          <tr :class="[
                rowClass(item, index),
                { 'vuetable-detail-row-shown': isVisibleDetailRow(getObjectValue(item, trackBy)) }
              ]"
              @click="onRowClick(item)"
          >
            <td v-for="field in visibleFields" 
                :key="field.name"
                :class="[field.dataClass, {
                  'vuetable-detail-row-toggle': useDetailRow
                }]"
                @click="useDetailRow ? 
                       toggleDetailRow(getObjectValue(item, trackBy)) : null"
            >
              <!-- Detail Row Toggle -->
              <template v-if="useDetailRow">
                <span class="detail-row-toggle">
                  <i :class="[
                    'detail-row-icon',
                    isVisibleDetailRow(getObjectValue(item, trackBy)) 
                      ? 'fas fa-chevron-down' 
                      : 'fas fa-chevron-right'
                  ]"></i>
                </span>
              </template>
              
              <!-- Cell Content -->
              {{ renderCell(item, field) }}
            </td>
          </tr>

          <!-- Detail Row -->
          <tr v-if="useDetailRow && isVisibleDetailRow(getObjectValue(item, trackBy))"
              class="vuetable-detail-row"
          >
            <td :colspan="visibleFields.length">
              <component 
                :is="detailRowComponent"
                :row-data="item"
                :row-index="index"
              />
            </td>
          </tr>
        </template>

        <!-- Empty State -->
        <tr v-if="tableData.length === 0">
          <td :colspan="visibleFields.length" class="vuetable-empty">
            {{ noDataMessage }}
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Pagination -->
    <vuetable-pagination
      v-if="tablePagination"
      :pagination="tablePagination"
      @page-changed="onPageChange"
    />
  </div>
</template>

<script>
import { ref, computed, onMounted, watch } from 'vue'
import VuetablePagination from './VuetablePagination.vue'
import axios from 'axios'

export default {
  name: 'Vuetable',
  components: {
    VuetablePagination
  },
  
  props: {
    fields: {
      type: Array,
      required: true
    },
    apiUrl: {
      type: String,
      default: ''
    },
    data: {
      type: Array,
      default: () => []
    },
    perPage: {
      type: Number,
      default: 10
    },
    currentPage: {
      type: Number,
      default: 1
    },
    detailRowComponent: {
      type: [String, Object],
      default: ''
    },
    trackBy: {
      type: String,
      default: 'id'
    },
    css: {
      type: Object,
      default: () => ({
        tableClass: 'table',
        ascendingClass: 'sorted-asc',
        descendingClass: 'sorted-desc'
      })
    },
    sortOrder: {
      type: Array,
      default: () => []
    },
    noDataMessage: {
      type: String,
      default: 'No data available'
    }
  },

  setup(props, { emit }) {
    const tableData = ref(props.data)
    const tablePagination = ref(null)
    const visibleDetailRows = ref([])
    const localSortOrder = ref([...props.sortOrder])
    
    // Computed
    const visibleFields = computed(() => 
      props.fields.filter(field => field.visible !== false)
    )

    const useDetailRow = computed(() => 
      props.detailRowComponent !== '' && tableData.value.length > 0
    )

    // Methods
    const loadData = async (page = 1) => {
      if (!props.apiUrl) return
      
      try {
        const params = {
          page,
          per_page: props.perPage,
          sort: getSortParam()
        }
        
        const response = await axios.get(props.apiUrl, { params })
        tableData.value = response.data.data
        tablePagination.value = response.data.pagination
        emit('loaded')
      } catch (error) {
        emit('error', error)
      }
    }

    const getSortParam = () => {
      return localSortOrder.value
        .map(sort => `${sort.field},${sort.direction}`)
        .join(';')
    }

    // Detail Row Methods
    const isVisibleDetailRow = (rowId) => {
      return visibleDetailRows.value.includes(rowId)
    }

    const toggleDetailRow = (rowId) => {
      const index = visibleDetailRows.value.indexOf(rowId)
      if (index === -1) {
        visibleDetailRows.value.push(rowId)
      } else {
        visibleDetailRows.value.splice(index, 1)
      }
    }

    // Sort Methods
    const isSortable = (field) => {
      return field.sortable !== false
    }

    const orderBy = (field) => {
      if (!isSortable(field)) return
      
      const currentSort = localSortOrder.value.find(sort => sort.field === field.name)
      
      if (!currentSort) {
        localSortOrder.value = [{
          field: field.name,
          direction: 'asc'
        }]
      } else {
        currentSort.direction = currentSort.direction === 'asc' ? 'desc' : 'asc'
      }
      
      loadData(1)
    }

    const sortIcon = (field) => {
      const sort = localSortOrder.value.find(sort => sort.field === field.name)
      if (!sort) return 'fas fa-sort'
      return sort.direction === 'asc' ? 'fas fa-sort-up' : 'fas fa-sort-down'
    }

    // Event Handlers
    const onPageChange = (page) => {
      loadData(page)
    }

    const onRowClick = (item) => {
      emit('row-clicked', item)
    }

    // Utility Methods
    const getObjectValue = (object, path) => {
      return path.split('.').reduce((obj, key) => obj?.[key], object)
    }

    const renderCell = (item, field) => {
      const value = getObjectValue(item, field.name)
      if (field.formatter) {
        return field.formatter(value, item)
      }
      return value
    }

    const rowClass = (item, index) => {
      if (typeof props.rowClass === 'function') {
        return props.rowClass(item, index)
      }
      return ''
    }

    // Lifecycle
    onMounted(() => {
      if (props.apiUrl) {
        loadData(props.currentPage)
      }
    })

    watch(() => props.data, (newVal) => {
      tableData.value = newVal
    })

    return {
      tableData,
      tablePagination,
      visibleFields,
      visibleDetailRows,
      useDetailRow,
      isVisibleDetailRow,
      toggleDetailRow,
      isSortable,
      orderBy,
      sortIcon,
      onPageChange,
      onRowClick,
      getObjectValue,
      renderCell,
      rowClass
    }
  }
}
</script>

<style scoped>
.vuetable-wrapper {
  width: 100%;
  overflow-x: auto;
}

.vuetable {
  width: 100%;
  border-collapse: collapse;
  background: white;
}

.vuetable th,
.vuetable td {
  padding: 12px;
  border: 1px solid #ddd;
  text-align: left;
}

.vuetable th.sortable {
  cursor: pointer;
}

.vuetable th.sortable:hover {
  background: #f5f5f5;
}

.sort-icon {
  margin-left: 5px;
}

.detail-row-toggle {
  cursor: pointer;
  padding-right: 8px;
}

.detail-row-icon {
  transition: transform 0.2s;
}

.vuetable-detail-row {
  background: #f9fafb;
}

.vuetable-detail-row > td {
  padding: 20px;
}

.vuetable-empty {
  text-align: center;
  padding: 20px;
  color: #666;
}
</style>
