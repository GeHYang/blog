import Vuex from 'vuex';
import Vue from 'vue';
import user from './user.js';
import dynamic from './dynamic.js';
import ws from './ws.js';
import chat from './chat.js';

Vue.use(Vuex);

const store = new Vuex.Store({
  modules: {
    user,
    dynamic,
    ws,
    chat
  }
});

export default store;