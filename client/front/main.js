import App from './App'
import Vue from 'vue'
import uView from "uview-ui";
import MyFooter from "./component/MyFooter";
import eventBus from './utils/eventBus';
import store from './store';
import { parseDate } from './utils/util';
import sqlite from './sqlite';

Vue.use(uView)
Vue.component("MyFooter", MyFooter)
Vue.config.productionTip = false
Vue.prototype.$bus = eventBus;

Vue.prototype.parseDate = parseDate;
Vue.prototype.$sqlite = sqlite;

import "./utils/intercept.js";

App.mpType = 'app'
const app = new Vue({
    ...App,
    store
});
app.$mount();
