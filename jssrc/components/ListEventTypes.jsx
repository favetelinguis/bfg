import React from 'react';
import PureRenderMixin from 'react-addons-pure-render-mixin';
import { connect } from 'react-redux';
import EventType from './EventType';
import * as actionCreators from '../action_creators';

export const ListEventTypes = React.createClass({

  mixins: [PureRenderMixin],

  render: function() {
    return (
      <div>
        <h1>TEST!!!</h1>
        {/* <EventType eventTypes={this.props.eventTypes} /> */}
      </div>
      )}
});

function mapStateToProps(state) {
  return {
    eventTypes: state.get('eventTypes')
  };
}

export const ListEventTypesContainer = connect(
  mapStateToProps,
  actionCreators
)(ListEventTypes);
