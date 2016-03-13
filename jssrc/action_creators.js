export const setEventTypes = () => {
  return {
    meta: { remote: true },
    type: 'SET_EVENTTYPE'
  };
};

export const setState = (state) => {
  return {
    type: 'SET_STATE',
    state
  };
};
