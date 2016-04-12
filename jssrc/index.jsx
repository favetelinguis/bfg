import { Map } from 'immutable';
import React from 'react';
import ReactDOM from 'react-dom';
import { Route, Router, IndexRoute, hashHistory } from 'react-router';
import { createStore, applyMiddleware } from 'redux';
import { Provider } from 'react-redux';

import socket from './websocket';
import reducer from './reducers/reducer';
import remoteActionMiddleware from './remote_action_middleware';
import App from './components/App.jsx';
import InitSelector from './components/InitSelector.jsx';
import { Selector1Container } from './components/Selector1.jsx';
import Selector2 from './components/Selector2.jsx';

/* import '../resources/style.css';
 */
const createStoreWithMiddleware = applyMiddleware(
  remoteActionMiddleware(socket)
)(createStore);
const initialState = Map();
const store = createStoreWithMiddleware(
  reducer,
  initialState,
  window.devToolsExtension ? window.devToolsExtension() : f => f
);

socket.registerEventHandler((dispatch, payload) => {
  switch (dispatch) {
    case 'action':
      store.dispatch(payload);
      break;
    default:
      console.warn(dispatch, payload);
  }
});

const routes = (
  <Route path="/" component={ App }>
    <IndexRoute component={ InitSelector } />
    <Route path="selector1" component={ Selector1Container } />
    <Route path="selector2" component={ Selector2 } />
  </Route>
  );

ReactDOM.render(
  <Provider store={store}>
    <Router history={hashHistory}>{routes}</Router>
  </Provider>,
  document.getElementById('app')
);

