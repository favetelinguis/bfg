import { List, Map, fromJS } from 'immutable';

function setState(state, newState) {
  const newList = fromJS(newState.eventTypes);
  return state.concat(newList);
  // return newState.eventTypes;
}

export default function eventTypes(state = List(), action) {
  switch (action.type) {
  case 'SET_EVENT_TYPES':
    return setState(state, action.state);
  default:
    return state;
  }
}
