import { connect } from 'react-redux';
import { getEventTypes, getIsLoading, getError } from '../reducers/eventTypes';
import { requestEventTypes } from  '../actions/eventTypes';
import EventTypes from '../components/EventTypes.jsx';

const mapStateToProps = (state) => {
  return {
    eventTypes: getEventTypes(state.get('eventTypes')).toJS(),
    isLoading: getIsLoading(state.get('eventTypes')),
    error: getError(state.get('eventTypes'))
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    onGetEventTypesClick: () => {
      dispatch(requestEventTypes());
    }
  }
};

const EventTypesContainer = connect(mapStateToProps, mapDispatchToProps)(EventTypes);

export default EventTypesContainer;
