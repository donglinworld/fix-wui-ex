<template>
  <div class="sessions-container">
    <vuetable ref="vuetable"
      :api-url="apiUrl"
      :fields="fields"
      :css="css"
      track-by="sessionId"
      pagination-path=""
    >
    </vuetable>
  </div>
</template>

<script>
import { ref } from 'vue'
import Vuetable from './vuetable-2/Vuetable.vue'

export default {
  name: 'Sessions',
  components: {
    Vuetable
  },
  setup() {
    const apiUrl = ref('/api/sessions')
    const fields = ref([
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
    ])
    const css = ref({
      tableClass: 'vuetable',
      loadingClass: 'loading',
      ascendingIcon: 'fas fa-sort-up',
      descendingIcon: 'fas fa-sort-down',
      sortableIcon: 'fas fa-sort'
    })

    return {
      apiUrl,
      fields,
      css
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

</style>