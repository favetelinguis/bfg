import { connect } from 'react-redux';
import { getEventTypes, getIsLoading, getError } from '../reducers/eventTypes';
import { requestEventTypes } from  '../actions/eventTypes';
import FilterSelector from '../components/FilterSelector.jsx';

const mapStateToProps = (state) => {
  return {
    types: getEventTypes(state.get('eventTypes')).toJS(),
    isLoading: getIsLoading(state.get('eventTypes')),
    error: getError(state.get('eventTypes')),
    message: 'Event Types',
    cols: ['name', 'marketCount']
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    onClick: () => {
      dispatch(requestEventTypes());
    }
  }
};

const EventTypesContainer = connect(mapStateToProps, mapDispatchToProps)(FilterSelector);

export default EventTypesContainer;
