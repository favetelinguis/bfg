import React, { PropTypes } from 'react';
import PureRenderMixin from 'react-addons-pure-render-mixin';
import { Field, Form, actions } from 'react-redux-form';
import ImmutablePropTypes from 'react-immutable-proptypes';

const MarketFilter = React.createClass({
  propTypes: {
    // change to send in the actual values
    marketFilter: ImmutablePropTypes.map.isRequired,
    handleSubmit: PropTypes.func.isRequired
  },

  mixins: [PureRenderMixin],

  render() {
    return (
      <Form model="marketFilter"
        onSubmit= { this.props.handleSubmit }
      >
        <Field model='marketFilter.eventTypes'>
          <label>Event Types</label>
          <select name='eventTypes' value={this.props.marketFilter.get('eventTypes').first()}>
            { this.props.marketFilter.get('eventTypes').map((event) => {
                return <option key={event} value={event}>{event}</option>;
              })}
          </select>
        </Field>

        <Field model="marketFilter.firstName">
          <label>First name:</label>
          <input value={ this.props.marketFilter.get('firstName') } />
        </Field>

        <Field model="marketFilter.lastName">
          <label>Last name:</label>
          <input value={ this.props.marketFilter.get('lastName') } />
        </Field>

        <button type="submit">
          Finish registration, { this.props.marketFilter.get('firstName') } { this.props.marketFilter.get('lastName') }!
        </button>
      </Form>
    )
  }
})

export default MarketFilter;
