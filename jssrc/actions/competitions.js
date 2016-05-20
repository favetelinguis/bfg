import { send } from '../websocket';
import { makeActionCreator } from '../util/actions';
import { RESP_COMPETITIONS,
         SET_TIMEOUT_ID_COMPETITIONS,
         CLEAR_TIMEOUT_ID_COMPETITIONS,
         TOGGLE_ERROR_COMPETITIONS,
         TOGGLE_IS_LOADING_COMPETITIONS } from '../constants/competitions';

export const toggleIsLoadingCompetitions = makeActionCreator(TOGGLE_IS_LOADING_COMPETITIONS);
export const toggleErrorCompetitions = makeActionCreator(TOGGLE_ERROR_COMPETITIONS);
export const clearTimeoutIdCompetitions = makeActionCreator(CLEAR_TIMEOUT_ID_COMPETITIONS);
export const setCompetitions = makeActionCreator(RESP_COMPETITIONS, 'competitions');
export const setTimeoutIdCompetitions = makeActionCreator(SET_TIMEOUT_ID_COMPETITIONS, 'timeoutId');

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
    const competitions = getState().get('competitions').toJS();
    if (competitions.timeoutId) {
      window.clearTimeout(competitions.timeoutId);
      dispatch(setCompetitions(action.competitions));
      dispatch(clearTimeoutIdCompetitions());
      dispatch(toggleIsLoadingCompetitions());
    }
  };
}
