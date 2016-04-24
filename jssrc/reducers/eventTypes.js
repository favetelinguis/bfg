import { List, Map, fromJS } from 'immutable';
import { RESP_EVENTTYPES, TOGGLE_IS_LOADING_EVENTTYPES } from '../constants/eventTypes';

function toggleIsLoading(state) {
  return state.update('isLoading', d => !d);
}

function setState(state, newState) {
  const newList = fromJS(newState);
  const temp = toggleIsLoading(state);
  return temp.set('events', newList);
}

const initialState = fromJS({
  events: [],
  isLoading: false
});

//SHOULD also have ERROR_EVENTTYPES
export function eventTypes(state = initialState, action) {
  switch (action.type) {
  case RESP_EVENTTYPES:
    return setState(state, action.eventTypes);
  case TOGGLE_IS_LOADING_EVENTTYPES:
    return toggleIsLoading(state);
  default:
    return state;
  }
}

export function getEventTypes(state) {
  return state.get('events');
};

export function getIsLoading(state) {
  return state.get('isLoading');
};
