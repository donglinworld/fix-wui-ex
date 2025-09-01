// Vuetify 3 setup
import { createVuetify } from 'vuetify';
import 'vuetify/styles'; // Import global styles
import { aliases, mdi } from 'vuetify/iconsets/mdi'; // For Material Design Icons

const vuetify = createVuetify({
  icons: {
    defaultSet: 'mdi', // Default icon set
    aliases,
    sets: {
      mdi,
    },
  },
  theme: {
    defaultTheme: 'light',
    themes: {
      light: {
        colors: {
          primary: '#6200EE',
          secondary: '#03DAC6',
        },
      },
    },
  },
});

export default vuetify;