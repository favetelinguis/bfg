function output(text){
  console.log(text);
}

function setupSocket() {
  let socket;
  let uri = "ws://localhost:8085";
  // let uri = "ws://" + location.host + location.pathname;
  // uri = uri.substring(0, uri.lastIndexOf('/'));
  socket = new WebSocket(uri);

  socket.onerror = (error) => {
    output(error);
  };

  socket.onopen = (event) => {
    output("Connected to " + event.currentTarget.url);
  };

  socket.onclose = (event) => {
    output("Disconnected: " + event.code + " " + event.reason);
    socket = undefined;
  };

  socket.onmessage = (event) => {
    const action = JSON.parse(event.data);
    // store.dispatch(action);
  };

  return socket;
}

export default setupSocket();
