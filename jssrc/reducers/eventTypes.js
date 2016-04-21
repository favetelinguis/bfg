import { List, Map, fromJS } from 'immutable';

function setState(state, newState) {
  const newList = fromJS(newState.eventTypes);
  return state.events.concat(newList);
  // return newState.eventTypes;
}

function updateIsLoading(state) {
  return state.updateIn(['isLoading'], d => !d);
}

const initialState = fromJS({
  events: [],
  isLoading: false
});

export default function listEventTypes(state = initialState, action) {
  switch (action.type) {
  case 'SET_EVENT_TYPES':
    return setState(state, action.state);
  case 'GET_EVENTTYPES':
    return updateIsLoading(state);
  default:
    return state;
  }
}
