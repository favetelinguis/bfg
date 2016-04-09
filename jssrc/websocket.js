function output(text){
  console.log(text);
}

function setupSocket() {
  let socket;
  let uri = "ws://" + "localhost:8085" + location.pathname;
  // let uri = "ws://" + location.host + location.pathname;
  uri = uri.substring(0, uri.lastIndexOf('/'));
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

  socket.registerEventHandler = (eventHandler) => {
    socket.onmessage = (event) => {
      const message = JSON.parse(event.data);
      const dispatch = message.event;
      const payload = message.payload;
      eventHandler(dispatch, payload);
    };
  };

  socket.emit = (event, payload) => {
    const json = JSON.stringify({ event, payload });
    socket.send(json);
  };

  return socket;
}

export default setupSocket();
