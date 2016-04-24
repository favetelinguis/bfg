import { connect } from 'react-redux';
import { getCompetitions, getIsLoading } from '../reducers/competitions';
import { serverCompetitions } from  '../actions/competitions';
import Competitions from '../components/Competitions.jsx';

const mapStateToProps = (state) => {
  return {
    competitions: getCompetitions(state.get('competitions')).toJS(),
    isLoading: getIsLoading(state.get('competitions'))
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    onGetCompetitionsClick: () => {
      dispatch(serverCompetitions());
    }
  }
};

const CompetitionsContainer = connect(mapStateToProps, mapDispatchToProps)(Competitions);

export default CompetitionsContainer;
