import { combineReducers } from 'redux';
import { List, Map } from 'immutable';

// Reducer compoeition
// having separate reducers for eventTypes
// marketFilter etc
// const appReducer = combineReducers({
// always name the reducer the same as the state they manage, so then the key and the
// reducer function will have the same name and i can have only one value in the map!!
// es6 convetion
//   marketFiler: importedFromMarketFilterReducer,
//   eventTypes: importedFromEventTypesReducer
// });
//Use this with create store

function setState(state, newState) {
  return state.merge(newState);
}

export default (state = Map(), action) => {
  switch (action.type) {
  case 'SET_STATE':
    return setState(state, action.state);
  default:
    return state;
  }
}
