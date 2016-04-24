import React from 'react'
import PureRenderMixin from 'react-addons-pure-render-mixin';
import { connect } from 'react-redux';
import ListGroup from 'react-bootstrap/lib/ListGroup';
import Button from 'react-bootstrap/lib/Button';
import ListGroupItem from 'react-bootstrap/lib/ListGroupItem';
import Griddle from 'griddle-react';
import * as actionCreators from '../action_creators.js'; //remove autoimport and use explicit as in example on redux

export const Selector1 = React.createClass({
  mixins: [PureRenderMixin],

  getEvents() {
    // Auto assigns this callback from actionCreators???
    return this.props.getEventTypes(); //Needs to be async but set isLoading and then response also sets isLoading
  },

  render() {
    return (
      <div>
        <Button
          bsStyle="primary"
          disabled={this.props.isLoading}
          onClick={!this.props.isLoading ? this.getEvents : null}>
          {this.props.isLoading ? 'Fetching...' : 'List Event Types'}
        </Button>
        <Griddle results={this.props.eventTypes}
          showFilter={true}
          resultsPerPage={30}
        columns={['name', 'marketCount']}/>
      </div>
    );
  }
});

function mapStateToProps(state) {
  return {
    //Here i can order the list of number of markets?
    eventTypes: state.getIn(['listEventTypes', 'events']).toJS(),
    isLoading: state.getIn(['listEventTypes', 'isLoading'])
  };
}

// Passes all action creators, and dispatches the actions to the store!
export const Selector1Container = connect(mapStateToProps, actionCreators)(Selector1);
