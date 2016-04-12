export const setEventTypes = () => {
  return {
    meta: { remote: true },
    type: 'SET_EVENTTYPE'
  };
};

export const setState = (state) => {
  return {
    type: 'SET_EVENT_TYPES',
    state
  };
};
