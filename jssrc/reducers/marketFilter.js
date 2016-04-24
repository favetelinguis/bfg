import { List, Map, fromJS } from 'immutable';

function setState(state, newState) {
  const newList = fromJS(newState);
  const temp = state.updateIn(['isLoading'], d => !d);
  return temp.set('events', newList);
}

function updateIsLoading(state) {
  return state.updateIn(['isLoading'], d => !d);
}

const initialState = fromJS({
  filter: {}
});

//SHOULD also have ERROR_EVENTTYPES
export default function marketFilter(state = initialState, action) {
  switch (action.type) {
  case 'SET':
    return setState(state, action.eventTypes);
  case 'GET':
    return updateIsLoading(state);
  default:
    return state;
  }
}
