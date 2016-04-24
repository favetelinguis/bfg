import { send } from '../websocket';
import { RESP_COMPETITIONS, TOGGLE_IS_LOADING_COMPETITIONS } from '../constants/competitions';

export const toggleIsLoadingCompetitions = () => {
  return {
    type: TOGGLE_IS_LOADING_COMPETITIONS
  };
};

export const serverCompetitions = () => {
  return (dispatch, getState) => {
    dispatch(toggleIsLoadingCompetitions());
    const filter = getState().getIn(['marketFilter', 'filter']).toJS();
    const request = { body: { filter },
                      type: 'GET_COMPETITIONS' };
    send(request);
  };
}

export const setCompetitions = (action) => {
  return {
    type: RESP_COMPETITIONS,
    competitions: action.competitions
  };
};

