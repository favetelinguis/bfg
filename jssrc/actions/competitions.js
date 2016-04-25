import { send } from '../websocket';
import { RESP_COMPETITIONS,
         SET_TIMEOUT_ID_COMPETITIONS,
         CLEAR_TIMEOUT_ID_COMPETITIONS,
         TOGGLE_ERROR_COMPETITIONS,
         TOGGLE_IS_LOADING_COMPETITIONS } from '../constants/competitions';

export const toggleIsLoadingCompetitions = () => {
  return {
    type: TOGGLE_IS_LOADING_COMPETITIONS
  };
};

export const toggleErrorCompetitions = () => {
  return {
    type: TOGGLE_ERROR_COMPETITIONS
  };
};

export const clearTimeoutIdCompetitions = () => {
  return {
    type: CLEAR_TIMEOUT_ID_COMPETITIONS
  };
};

export const setCompetitions = (competitions) => {
  return {
    type: RESP_COMPETITIONS,
    competitions: competitions
  };
};

export const setTimeoutIdCompetitions = (id) => {
  return {
    type: SET_TIMEOUT_ID_COMPETITIONS,
    timeoutId: id
  };
};

export const requestCompetitions = () => {
  return (dispatch, getState) => {
    let timeoutId = window.setTimeout(() => {
      dispatch(toggleErrorCompetitions());
      dispatch(clearTimeoutIdCompetitions());
      dispatch(toggleIsLoadingCompetitions());
    }, 4000);

    dispatch(toggleIsLoadingCompetitions());
    dispatch(setTimeoutIdCompetitions(timeoutId));

    const filter = getState().getIn(['marketFilter', 'filter']).toJS();
    const request = { body: { filter },
                      type: 'GET_COMPETITIONS' };
    send(request);

  };
}

export const responseCompetitions = (action) => {
  return (dispatch, getState) => {
    // Prop extremly slow to do toJS on the whole structure use immutablejs better!!
    // change to get in like in reducers
    const { competitions } = getState().toJS();
    if (competitions.timeoutId) {
      window.clearTimeout(competitions.timeoutId);
      dispatch(setCompetitions(action.competitions));
      dispatch(clearTimeoutIdCompetitions());
      dispatch(toggleIsLoadingCompetitions());
    }
  };
}
