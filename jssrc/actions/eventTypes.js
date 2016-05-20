import { send } from '../websocket';
import { makeActionCreator } from '../util/actions';
import { RESP_EVENTTYPES,
         SET_TIMEOUT_ID_EVENTTYPES,
         CLEAR_TIMEOUT_ID_EVENTTYPES,
         TOGGLE_ERROR_EVENTTYPES,
         TOGGLE_IS_LOADING_EVENTTYPES } from '../constants/eventTypes';

export const toggleIsLoadingEventTypes = makeActionCreator(TOGGLE_IS_LOADING_EVENTTYPES);
export const toggleErrorEventTypes = makeActionCreator(TOGGLE_ERROR_EVENTTYPES);
export const clearTimeoutIdEventTypes = makeActionCreator(CLEAR_TIMEOUT_ID_EVENTTYPES);
export const setEventTypes = makeActionCreator(RESP_EVENTTYPES, 'eventTypes');
export const setTimeoutIdEventTypes = makeActionCreator(SET_TIMEOUT_ID_EVENTTYPES, 'timeoutId');

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
    const eventTypes = getState().get('eventTypes').toJS();
    if (eventTypes.timeoutId) {
      window.clearTimeout(eventTypes.timeoutId);
      dispatch(setEventTypes(action.eventTypes));
      dispatch(clearTimeoutIdEventTypes());
      dispatch(toggleIsLoadingEventTypes());
    }
  };
}

