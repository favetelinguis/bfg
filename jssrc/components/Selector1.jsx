import React from 'react'
import PureRenderMixin from 'react-addons-pure-render-mixin';
import { connect } from 'react-redux';
import ListGroup from 'react-bootstrap/lib/ListGroup';
import Button from 'react-bootstrap/lib/Button';
import ListGroupItem from 'react-bootstrap/lib/ListGroupItem';
import * as actionCreators from '../action_creators.js';

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
        <ListGroup>
          {this.props.eventTypes.map(event =>
            <ListGroupItem key={event.getIn(['eventType', 'id'])}>{event.get('name')}</ListGroupItem>
          )}
        </ListGroup>
      </div>
    );
  }
});

function mapStateToProps(state) {
  return {
    eventTypes: state.getIn(['listEventTypes', 'events']),
    isLoading: state.getIn(['listEventTypes', 'isLoading'])
  };
}

// Passes all action creators, and dispatches the actions to the store!
export const Selector1Container = connect(mapStateToProps, actionCreators)(Selector1);
