import React, { PropTypes } from 'react';
import PureRenderMixin from 'react-addons-pure-render-mixin';
import Button from 'react-bootstrap/lib/Button';
import Griddle from 'griddle-react';

const FilterSelector = React.createClass({

  propTypes: {
    isLoading: PropTypes.bool.isRequired,
    error: PropTypes.bool.isRequired,
    types: PropTypes.array.isRequired,
    message: PropTypes.string.isRequired,
    cols: PropTypes.array.isRequired,
    onClick: PropTypes.func.isRequired,
    onRowClick: PropTypes.func.isRequired
  },

  mixins: [PureRenderMixin],

  render() {
    return (
      <div>
        <Button
          bsStyle="primary"
          disabled={this.props.isLoading}
          onClick={!this.props.isLoading ? this.props.onClick : null}
        >
          {this.props.isLoading ? 'Fetching...' : 'List ' + this.props.message }
        </Button>
        {this.props.error ? 'Timed out while fetching!' :
          <Griddle results={this.props.types}
            showFilter={true}
            resultsPerPage={30}
            columns={ this.props.cols }
            onRowClick={ this.props.onRowClick }
          />
        }
      </div>
    );
  }
});

export default FilterSelector;
