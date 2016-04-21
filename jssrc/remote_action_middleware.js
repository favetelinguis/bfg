export default socket => store => next => action => {
  if (action.meta && action.meta.remote) {
    // Add a line in the sent object called type: action
    const json = JSON.stringify(action);
    socket.send(json);
  }
  // if we dont call next the action will not be sent to the
  // reducer but just to this middleware
  // might be nice to do with actions that should only talk to the server?
  // se the middleware tutorial for more info
  return next(action);
};
