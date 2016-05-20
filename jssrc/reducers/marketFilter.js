import { List, Map, fromJS } from 'immutable';
import { modelReducer } from 'react-redux-form/lib/immutable/index';

function setState(state, newState) {
  const newList = fromJS(newState);
  const temp = state.updateIn(['isLoading'], d => !d);
  return temp.set('events', newList);
}

function updateIsLoading(state) {
  return state.updateIn(['isLoading'], d => !d);
}

const initialState = fromJS({
  firstName: 'init',
  lastName: 'iiii',
  eventTypes: ['A', 'B', 'C'],
  filter: {}
});

//SHOULD also have ERROR_EVENTTYPES
const old = function marketFilter(state = initialState, action) {
  switch (action.type) {
  case 'SET':
    return setState(state, action.eventTypes);
  case 'GET':
    return updateIsLoading(state);
  default:
    return state;
  }
}

export const marketFilter = (model) => modelReducer(model, initialState);

export const getMarketFilter = (state) => {
  return state;
}
