import React, { PropTypes } from 'react';
import PureRenderMixin from 'react-addons-pure-render-mixin';
import Button from 'react-bootstrap/lib/Button';
import Griddle from 'griddle-react';

const EventTypes = React.createClass({

  propTypes: {
    isLoading: PropTypes.bool.isRequired,
    error: PropTypes.bool.isRequired,
    eventTypes: PropTypes.array.isRequired,
    onGetEventTypesClick: PropTypes.func.isRequired
  },

  mixins: [PureRenderMixin],

  render() {
    return (
      <div>
        <Button
          bsStyle="primary"
          disabled={this.props.isLoading}
          onClick={!this.props.isLoading ? this.props.onGetEventTypesClick : null}
        >
          {this.props.isLoading ? 'Fetching...' : 'List Event Types'}
        </Button>
        {this.props.error ? 'Timed out while fetching!' :
          <Griddle results={this.props.eventTypes}
            showFilter={true}
            resultsPerPage={30}
            columns={['name', 'marketCount']}
          />
        }
      </div>
    );
  }
});

export default EventTypes;
