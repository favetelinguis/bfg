import React from 'react';
import PureRenderMixin from 'react-addons-pure-render-mixin';

export default React.createClass({
  mixins: [PureRenderMixin],
  getEvents: () => {
    return this.props.eventTypes || [];
  },
  render: () => {
    // Nice destructing!
    const { eventTypes } = this.props;
    return (
      <ul>
        {eventTypes.map(entry =>
          <li className="entry" key={entry.eventType.name}>{entry}</li>
         )}
      </ul>
    );
  }
});
