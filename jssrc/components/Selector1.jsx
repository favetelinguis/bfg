import React from 'react'
import PureRenderMixin from 'react-addons-pure-render-mixin';
import { connect } from 'react-redux';
import ListGroup from 'react-bootstrap/lib/ListGroup';
import ListGroupItem from 'react-bootstrap/lib/ListGroupItem';

export const Selector1 = React.createClass({
  mixins: [PureRenderMixin],

  getEvents() {
    return this.props.eventTypes || [{ name: 'NO EVENTS!' }];
  },

  render() {
    return (
      <ListGroup>
        {this.getEvents().map(event =>
          <ListGroupItem key={event.getIn(['eventType', 'id'])}>{event.get('name')}</ListGroupItem>
        )}
      </ListGroup>
    );
  }
});

function mapStateToProps(state) {
  return {
    eventTypes: state.get('eventTypes')
  };
}

export const Selector1Container = connect(mapStateToProps)(Selector1);
