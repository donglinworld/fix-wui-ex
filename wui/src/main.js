import { createApp } from 'vue';
import App from './App.vue';
import vuetify from './plugins/vuetify'; // Import the Vuetify instance

const app = createApp(App);

app.use(vuetify); // Use Vuetify
app.mount('#app'); // Mount the app