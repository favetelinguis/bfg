import { connect } from 'react-redux';
import { getCompetitions, getIsLoading, getError } from '../reducers/competitions';
import { requestCompetitions } from '../actions/competitions';
import FilterSelector from '../components/FilterSelector.jsx';

const mapStateToProps = (state) => {
  return {
    types: getCompetitions(state.get('competitions')).toJS(),
    isLoading: getIsLoading(state.get('competitions')),
    error: getError(state.get('competitions')),
    message: 'Competitions',
    cols: ['name', 'competitionRegion', 'marketCount']
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    onClick: () => {
      dispatch(requestCompetitions());
    }
  }
};


const CompetitionsContainer = connect(mapStateToProps, mapDispatchToProps)(FilterSelector);

export default CompetitionsContainer;
