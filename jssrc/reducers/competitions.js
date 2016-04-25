import { List, Map, fromJS } from 'immutable';
import { RESP_COMPETITIONS,
         SET_TIMEOUT_ID_COMPETITIONS,
         CLEAR_TIMEOUT_ID_COMPETITIONS,
         TOGGLE_ERROR_COMPETITIONS,
         TOGGLE_IS_LOADING_COMPETITIONS } from '../constants/competitions';

function setTimeoutId(state, id) {
  return state.set('timeoutId', id);
}

function clearTimeoutId(state) {
  return state.set('timeoutId', null);
}

function toggleIsLoading(state) {
  return state.update('isLoading', b => !b);
}

function setState(state, competitions) {
  const newList = fromJS(competitions);
  return state.set('competitions', newList);
}

function toggleError(state) {
  return state.update('error', b => !b);
}

const initialState = fromJS({
  competitions: [],
  isLoading: false,
  error: false,
  timeoutId: null
});

export function competitions(state = initialState, action) {
  switch (action.type) {
  case RESP_COMPETITIONS:
    return setState(state, action.competitions);
  case SET_TIMEOUT_ID_COMPETITIONS:
    return setTimeoutId(state, action.timeoutId);
  case CLEAR_TIMEOUT_ID_COMPETITIONS:
    return clearTimeoutId(state);
  case TOGGLE_IS_LOADING_COMPETITIONS:
    return toggleIsLoading(state);
  case TOGGLE_ERROR_COMPETITIONS:
    return toggleError(state);
  default:
    return state;
  }
}

export function getCompetitions(state) {
  return state.get('competitions');
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
