import { Map } from 'immutable';
import React from 'react';
import ReactDOM from 'react-dom';
import { Route, Router, IndexRoute, hashHistory } from 'react-router';
import { createStore, applyMiddleware } from 'redux';
import { Provider } from 'react-redux';
import thunk from 'redux-thunk';

import { setupSocketEventHandlers } from './websocket';
import reducer from './reducers/reducer';
import App from './components/App.jsx';
import InitSelector from './components/InitSelector.jsx';
import EventTypes from './containers/EventTypes';
import Competitions from './containers/Competitions.jsx';

const createStoreWithMiddleware = applyMiddleware(
  thunk
)(createStore);
const initialState = Map();
const store = createStoreWithMiddleware(
  reducer,
  initialState, //Nice place to get state from server to set client state up to date
  window.devToolsExtension ? window.devToolsExtension() : f => f
);

setupSocketEventHandlers(store);

const routes = (
  <Route path="/" component={ App }>
    <IndexRoute component={ InitSelector } />
    <Route path="eventTypes" component={ EventTypes } />
    <Route path="competitions" component={ Competitions } />
  </Route>
  );

ReactDOM.render(
  <Provider store={store}>
    <Router history={hashHistory}>{routes}</Router>
  </Provider>,
  document.getElementById('app')
);

