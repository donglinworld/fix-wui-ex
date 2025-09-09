<template>
  <div class="sessions-container">
    <vuetable ref="vuetable"
      :api-url="apiUrl"
      :fields="fields"
      :css="css"
      track-by="sessionId"
      pagination-path=""
      @vuetable:loading="showLoader"
      @vuetable:loaded="hideLoader"
    >
      <template #actions-slot="props">
        <v-btn
          size="small"
          color="primary"
          @click="onActionClick(props.rowData)"
        >
          Details
        </v-btn>
      </template>
    </vuetable>
  </div>
</template>

<script>
import Vuetable from './vuetable-2/Vuetable.vue'

export default {
  name: 'Sessions',
  components: {
    Vuetable
  },
  data() {
    return {
      loading: false,
      apiUrl: '/api/sessions',
      fields: [
        {
          name: 'sessionId',
          title: 'Session ID',
          sortField: 'sessionId'
        },
        {
          name: 'status',
          title: 'Status',
          sortField: 'status'
        },
        {
          name: '__slot:actions-slot',
          title: 'Actions',
          width: '150px'
        }
      ],
      css: {
        tableClass: 'vuetable',
        loadingClass: 'loading',
        ascendingIcon: 'fas fa-sort-up',
        descendingIcon: 'fas fa-sort-down',
        sortableIcon: 'fas fa-sort'
      }
    }
  },
  methods: {
    showLoader() {
      this.loading = true
    },
    hideLoader() {
      this.loading = false
    },
    onActionClick(rowData) {
      console.log('Action clicked for row:', rowData)
      // Add your action handling logic here
    }
  }
}
</script>

<style scoped>
.sessions-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.vuetable {
  width: 100%;
  background-color: white;
  border: 1px solid #ddd;
}

.loading {
  opacity: 0.5;
  pointer-events: none;
}
</style>