import React from 'react';
import ReactDOM from 'react-dom';
import {
  renderIntoDocument,
  scryRenderedDOMComponentsWithClass,
  Simulate
} from 'react-addons-test-utils';
import { List, Map, fromJS } from 'immutable';
import { expect } from 'chai';

import { ListEventTypes } from '../../jssrc/components/ListEventTypes';

describe('ListEventTypes', () => {
  it('renders a list of EventTypes', () => {
    const eventTypes = fromJS([
      { eventType: {id: "123", name: "Test" },
        name: "name2" },
      { eventType: {id: "123", name: "Test" },
        name: "name2" }
    ]);
    const component = renderIntoDocument(
      <ListEventTypes eventTypes={eventTypes} />
    );

    const entries = scryRenderedDOMComponentsWithClass(component, 'event');

    expect(entries.length).to.equal(2);
  });
});
