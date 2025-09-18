<template>
  <div class="messages-container">
    <v-form @submit.prevent="sendMessage">
      <v-select
        v-model="selectedSession"
        :items="sessions"
        item-title="sessionId"
        item-value="sessionId"
        label="Select Session"
        class="mb-4"
        @update:model-value="loadSessionMessages"
      />
      
      <v-text-field
        v-model="messageText"
        label="Enter FIX Message (e.g. 8=FIX.4.2|9=57|35=D|34=6|49=BANZAI42|52=20250910-23:54:51.405|56=EXEC42|22=2|48=Sedol|55=Sony|38=100|40=1|54=1|60=20250910-23:54:51.405|21=1|11=abc-123|44=10.5|10=129|)"
        :rules="[v => !!v || 'Message is required']"
        class="mb-4 message-input"
        variant="outlined"
        rows="4"
        multiline
      />
      
      <v-btn 
        type="submit" 
        color="primary"
        :disabled="!selectedSession || !messageText"
        class="mb-4"
      >
        Send Message
      </v-btn>
    </v-form>

    <div v-if="selectedSession" class="message-table mt-4">
      <h3>Message History for {{ selectedSession }}</h3>
      <vuetable ref="vuetable"
        :api-url="messagesApiUrl"
        :fields="fields"
        :css="css"
        pagination-path=""
        @vuetable:loading="showLoader"
        @vuetable:loaded="hideLoader"
      >
      </vuetable>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import Vuetable from './vuetable-2/Vuetable.vue'

export default {
  name: 'Messages',
  components: {
    Vuetable
  },
  setup() {
    const selectedSession = ref('')
    const messageText = ref('')
    const sessions = ref([])
    const loading = ref(false)

    const messagesApiUrl = computed(() => {
      return selectedSession.value 
        ? `/api/messages/sendrecvlist?sessionId=${selectedSession.value}`
        : ''
    })

    const fields = ref([
      {
        name: 'msgDirection',
        title: 'Direction',
        sortField: 'msgDirection',
        width: '100px'
      },
      {
        name: 'messageContent',
        title: 'Message',
        sortField: 'messageContent'
      },
      {
        name: 'timestamp',
        title: 'Timestamp',
        sortField: 'timestamp',
        width: '200px'
      }
    ])

    const css = ref({
      tableClass: 'vuetable',
      loadingClass: 'loading',
      ascendingIcon: 'fas fa-sort-up',
      descendingIcon: 'fas fa-sort-down',
      sortableIcon: 'fas fa-sort'
    })

    const fetchSessions = async () => {
      try {
        const res = await axios.get('/api/sessions')
        sessions.value = res.data.data
      } catch (error) {
        console.error('Error fetching sessions:', error)
      }
    }

    const sendMessage = async () => {
      try {
        await axios.post('/api/messages/send', {
          sessionId: selectedSession.value,
          message: messageText.value
        });
        
        alert('Message sent successfully');
        messageText.value = '';
        
        // Refresh message table
        if (selectedSession.value) {
          loadSessionMessages();
        }
      } catch (error) {
        console.error('Error sending message:', error);
        alert('Error sending message');
      }
    }

    const loadSessionMessages = () => {
      if (selectedSession.value && this.$refs.vuetable) {
        this.$refs.vuetable.refresh();
      }
    }

    const showLoader = () => {
      loading.value = true;
    }

    const hideLoader = () => {
      loading.value = false;
    }

    onMounted(() => {
      fetchSessions()
    })

    return {
      selectedSession,
      messageText,
      sessions,
      messagesApiUrl,
      fields,
      css,
      sendMessage,
      loadSessionMessages,
      showLoader,
      hideLoader
    }
  }
}
</script>

<style scoped>
.messages-container {
  padding: 20px;
  max-width: 1600px;
  margin: 0 auto;
}

.message-input {
  width: 100%;
  font-family: monospace;
}

:deep(.message-input .v-field__input) {
  min-height: 200px !important;
  font-size: 14px;
  line-height: 1.5;
}

.message-table {
  background: white;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  margin-top: 20px;
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