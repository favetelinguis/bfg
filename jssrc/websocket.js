import {setEventTypes} from './action_creators.js';

function output(text){
  console.log(text);
}

let createSocket = () => {
  let uri = "ws://localhost:8085";
  // let uri = "ws://" + location.host + location.pathname;
  // uri = uri.substring(0, uri.lastIndexOf('/'));
  return new WebSocket(uri);
};

export let socket = createSocket();

export function setupSocketEventHandlers(store) {
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
    output(action);
    switch (action.type) {
      case "RESP_EVENTTYPES":
      store.dispatch(setEventTypes(action));
    }
  };
}

