import { combineReducers } from 'redux-immutable';

import eventTypes from './eventTypes';
import contries from './contries';
import venues from './venues';
import marketTypes from './marketTypes';
import competitions from './competitions';
import timeRanges from './timeRanges';
import events from './events';

export default combineReducers({
  eventTypes,
  contries,
  venues,
  marketTypes,
  competitions,
  timeRanges,
  events
});
