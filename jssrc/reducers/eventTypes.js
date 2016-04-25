import { List, Map, fromJS } from 'immutable';
import { RESP_EVENTTYPES,
         SET_TIMEOUT_ID_EVENTTYPES,
         CLEAR_TIMEOUT_ID_EVENTTYPES,
         TOGGLE_ERROR_EVENTTYPES,
         TOGGLE_IS_LOADING_EVENTTYPES } from '../constants/eventTypes';

function setTimeoutId(state, id) {
  return state.set('timeoutId', id);
}

function clearTimeoutId(state) {
  return state.set('timeoutId', null);
}

function toggleIsLoading(state) {
  return state.update('isLoading', b => !b);
}

function setState(state, events) {
  const newList = fromJS(events);
  return state.set('events', newList);
}

function toggleError(state) {
  return state.update('error', b => !b);
}

const initialState = fromJS({
  events: [],
  isLoading: false,
  error: false,
  timeoutId: null
});

export function eventTypes(state = initialState, action) {
  switch (action.type) {
  case RESP_EVENTTYPES:
    return setState(state, action.eventTypes);
  case SET_TIMEOUT_ID_EVENTTYPES:
    return setTimeoutId(state, action.timeoutId);
  case CLEAR_TIMEOUT_ID_EVENTTYPES:
    return clearTimeoutId(state);
  case TOGGLE_IS_LOADING_EVENTTYPES:
    return toggleIsLoading(state);
  case TOGGLE_ERROR_EVENTTYPES:
    return toggleError(state);
  default:
    return state;
  }
}

export function getEventTypes(state) {
  return state.get('events');
};

export function getTimeoutId(state) {
  return state.get('timeoutId');
};

export function getError(state) {
  return state.get('error');
};

export function getIsLoading(state) {
  return state.get('isLoading');
};
