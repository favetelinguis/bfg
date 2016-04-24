import React, { PropTypes } from 'react';
import PureRenderMixin from 'react-addons-pure-render-mixin';
import Button from 'react-bootstrap/lib/Button';
import Griddle from 'griddle-react';

const Competitions = React.createClass({

  propTypes: {
    isLoading: PropTypes.bool.isRequired,
    competitions: PropTypes.array.isRequired,
    onGetCompetitionsClick: PropTypes.func.isRequired
  },

  mixins: [PureRenderMixin],

  render() {
    return (
      <div>
        <Button
          bsStyle="primary"
          disabled={this.props.isLoading}
          onClick={!this.props.isLoading ? this.props.onGetCompetitionsClick : null}
        >
          {this.props.isLoading ? 'Fetching...' : 'List Competitions'}
        </Button>
        <Griddle results={this.props.competitions}
          showFilter={true}
          resultsPerPage={30}
          columns={['name', 'competitionRegion', 'marketCount']}
        />
      </div>
    );
  }
});

export default Competitions;
