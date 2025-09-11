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
      />
      
      <v-text-field
        v-model="messageText"
        label="Enter FIX Message (e.g. 8=FIX.4.2|9=57|35=D|34=6|49=BANZAI42|52=20250910-23:54:51.405|56=EXEC42|22=2|48=Sedol|55=Sony|38=100|40=1|54=1|60=20250910-23:54:51.405|21=1|11=abc-123|44=10.5|10=129|)"
        :rules="[v => !!v || 'Message is required']"
        class="mb-4"
      />
      
      <v-btn 
        type="submit" 
        color="primary"
        :disabled="!selectedSession || !messageText"
      >
        Send Message
      </v-btn>
    </v-form>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import axios from 'axios'

export default {
  name: 'Messages',
  setup() {
    const selectedSession = ref('')
    const messageText = ref('')
    const sessions = ref([])

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
        const response = await fetch('/api/messages/send', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            sessionId: selectedSession.value,
            message: messageText.value
          })
        })
        
        if (response.ok) {
          alert('Message sent successfully')
          messageText.value = ''
        } else {
          alert('Failed to send message')
        }
      } catch (error) {
        console.error('Error sending message:', error)
        alert('Error sending message')
      }
    }

    onMounted(() => {
      fetchSessions()
    })

    return {
      selectedSession,
      messageText,
      sessions,
      sendMessage
    }
  }
}
</script>

<style scoped>
.messages-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}
</style>