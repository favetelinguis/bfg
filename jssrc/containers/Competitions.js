import { connect } from 'react-redux';
import { getCompetitions, getIsLoading, getError } from '../reducers/competitions';
import { requestCompetitions } from '../actions/competitions';
import Competitions from '../components/Competitions.jsx';

const mapStateToProps = (state) => {
  return {
    competitions: getCompetitions(state.get('competitions')).toJS(),
    isLoading: getIsLoading(state.get('competitions')),
    error: getError(state.get('competitions'))
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    onGetCompetitionsClick: () => {
      dispatch(requestCompetitions());
    }
  }
};

const CompetitionsContainer = connect(mapStateToProps, mapDispatchToProps)(Competitions);

export default CompetitionsContainer;
