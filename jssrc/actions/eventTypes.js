import { send } from '../websocket';
import { RESP_EVENTTYPES,
         SET_TIMEOUT_ID_EVENTTYPES,
         CLEAR_TIMEOUT_ID_EVENTTYPES,
         TOGGLE_ERROR_EVENTTYPES,
         TOGGLE_IS_LOADING_EVENTTYPES } from '../constants/eventTypes';

export const toggleIsLoadingEventTypes = () => {
  return {
    type: TOGGLE_IS_LOADING_EVENTTYPES
  };
};

export const toggleErrorEventTypes = () => {
  return {
    type: TOGGLE_ERROR_EVENTTYPES
  };
};

export const clearTimeoutIdEventTypes = () => {
  return {
    type: CLEAR_TIMEOUT_ID_EVENTTYPES
  };
};

export const setEventTypes = (events) => {
  return {
    type: RESP_EVENTTYPES,
    eventTypes: events
  };
};

export const setTimeoutIdEventTypes = (id) => {
  return {
    type: SET_TIMEOUT_ID_EVENTTYPES,
    timeoutId: id
  };
};

export const requestEventTypes = () => {
  return (dispatch, getState) => {
    let timeoutId = window.setTimeout(() => {
      dispatch(toggleErrorEventTypes());
      dispatch(clearTimeoutIdEventTypes());
      dispatch(toggleIsLoadingEventTypes());
    }, 4000);

    dispatch(toggleIsLoadingEventTypes());
    dispatch(setTimeoutIdEventTypes(timeoutId));

    const filter = getState().getIn(['marketFilter', 'filter']).toJS();
    const request = { body: { filter },
                      type: 'GET_EVENTTYPES' };
    send(request);

  };
}

export const responseEventTypes = (action) => {
  return (dispatch, getState) => {
    // Prop extremly slow to do toJS on the whole structure use immutablejs better!!
    const { eventTypes } = getState().toJS();
    if (eventTypes.timeoutId) {
      window.clearTimeout(eventTypes.timeoutId);
      dispatch(setEventTypes(action.eventTypes));
      dispatch(clearTimeoutIdEventTypes());
      dispatch(toggleIsLoadingEventTypes());
    }
  };
}

