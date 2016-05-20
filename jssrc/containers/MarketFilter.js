import { connect } from 'react-redux';
import MarketFilter from '../components/MarketFilter.jsx';
import { getMarketFilter } from '../reducers/marketFilter';
import { actions } from 'react-redux-form';

const mapStateToProps = (state) => {
  return {
    marketFilter: getMarketFilter(state.get('marketFilter'))
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    handleSubmit: (form) => {
      console.log(form);
    }
  }
};

const MarketFilterContainer = connect(mapStateToProps, mapDispatchToProps)(MarketFilter);

export default MarketFilterContainer;
