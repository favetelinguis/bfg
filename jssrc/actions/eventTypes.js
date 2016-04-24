import {send} from '../websocket';
import {RESP_EVENTTYPES, TOGGLE_IS_LOADING_EVENTTYPES} from '../constants/eventTypes';

export const toggleIsLoadingEventTypes = () => {
  return {
    type: TOGGLE_IS_LOADING_EVENTTYPES
  };
};

export const serverEventTypes = () => {
  return (dispatch, getState) => {
    dispatch(toggleIsLoadingEventTypes());
    const { marketFilter } = getState().toJS();
    const filter = marketFilter.filter;
    const request = { body: { filter },
                      type: 'GET_EVENTTYPES' };
    send(request);
  };
}

export const setEventTypes = (action) => {
  return {
    type: RESP_EVENTTYPES,
    eventTypes: action.eventTypes
  };
};

