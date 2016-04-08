import { List, Map } from 'immutable';

function setState(state, newState) {
  return state.merge(newState);
}

export default function eventTypes(state = List(), action) {
  switch (action.type) {
  case 'SET_STATE':
    return setState(state, action.state);
  default:
    return state;
  }
}
