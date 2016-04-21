// Set isLoading to true
// Make sideeffect with server call
// Needs to be async?
export const getEventTypes = () => {
  return {
    meta: { remote: true },
    type: 'GET_EVENTTYPES'
  };
};
