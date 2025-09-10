<template>
  <div id="app">
    <v-app>
      <v-main>
        <div :class="mainclass">
          <v-tabs v-model="activeTab">
            <v-tab value="tab-1">Sessions</v-tab>
            <v-tab value="tab-2">Messages</v-tab>
          </v-tabs>
          <v-window v-model="activeTab">
            <v-window-item value="tab-1">
              <Sessions />
            </v-window-item>
            <v-window-item value="tab-2">
              <Messages />
            </v-window-item>
          </v-window>
        </div>
      </v-main>
    </v-app>
  </div>
</template>

<script>
import { ref, computed } from 'vue';
import { useDisplay } from 'vuetify';
import Sessions from './components/Sessions.vue';
import Messages from './components/Messages.vue';

export default {
  name: 'App',
  components: {
    Sessions,
    Messages
  },
  setup() {
    const activeTab = ref('tab-1');

    const mainclass = computed(() => {
      switch (useDisplay()) {
        case 'xs':
          return 'mainclassThin';
        default:
          return 'mainclassNormal';
      }
    });

    return {
      activeTab,
      mainclass,
    };
  },
};
</script>

<style scoped>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
.mainclassThin {
  width: 375px;
}
</style>
