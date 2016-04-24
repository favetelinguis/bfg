const GET_EVENTTYPES = 'GET_EVENTTYPES';
export const getEventTypes = () => {
  return {
    meta: { remote: true },
    body: { filter: {} }, // This is initial state for filter state!!
    type: GET_EVENTTYPES
  };
};

const RESP_EVENTTYPES = 'RESP_EVENTTYPES';
export const setEventTypes = (action) => {
  return {
    type: RESP_EVENTTYPES,
    eventTypes: action.eventTypes
  };
};
