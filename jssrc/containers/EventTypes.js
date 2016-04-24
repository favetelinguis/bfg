import { connect } from 'react-redux';
import { getEventTypes, getIsLoading } from '../reducers/eventTypes';
import { serverEventTypes } from  '../actions/eventTypes';
import EventTypes from '../components/EventTypes.jsx';

const mapStateToProps = (state) => {
  return {
    eventTypes: getEventTypes(state.get('eventTypes')).toJS(),
    isLoading: getIsLoading(state.get('eventTypes'))
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    onGetEventTypesClick: () => {
      dispatch(serverEventTypes());
    }
  }
};

const EventTypesContainer = connect(mapStateToProps, mapDispatchToProps)(EventTypes);

export default EventTypesContainer;
