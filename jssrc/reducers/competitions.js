import { List, Map, fromJS } from 'immutable';
import { RESP_COMPETITIONS, TOGGLE_IS_LOADING_COMPETITIONS } from '../constants/competitions';

function toggleIsLoading(state) {
  return state.update('isLoading', d => !d);
}

function setState(state, newState) {
  const newList = fromJS(newState);
  const temp = toggleIsLoading(state);
  return temp.set('competitions', newList);
}

const initialState = fromJS({
  competitions: [],
  isLoading: false
});

//SHOULD also have ERROR_EVENTTYPES
export function competitions(state = initialState, action) {
  switch (action.type) {
  case RESP_COMPETITIONS:
    return setState(state, action.competitions);
  case TOGGLE_IS_LOADING_COMPETITIONS:
    return toggleIsLoading(state);
  default:
    return state;
  }
}

export function getCompetitions(state) {
  return state.get('competitions');
};

export function getIsLoading(state) {
  return state.get('isLoading');
};
