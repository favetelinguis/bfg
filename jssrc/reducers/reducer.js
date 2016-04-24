import { combineReducers } from 'redux-immutable';

import listEventTypes from './eventTypes';
import contries from './contries';
import venues from './venues';
import marketTypes from './marketTypes';
import competitions from './competitions';
import timeRanges from './timeRanges';
import events from './events';
import marketFilter from './marketFilter';

export default combineReducers({
  listEventTypes,
  contries,
  venues,
  marketTypes,
  competitions,
  timeRanges,
  events,
  marketFilter
});
