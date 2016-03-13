import { List, Map, fromJS } from 'immutable';
import { expect } from 'chai';

import reducer from '../jssrc/reducer';
import { setState } from '../jssrc/action_creators';

describe('reducer', () => {
  it('handles SET_STATE', () => {
    const initialState = fromJS({
      test: "old"
    });
    const updateState = fromJS({
      test: "new"
    });
    const action = setState(updateState);
    const nextState = reducer(initialState, action);

    expect(nextState).to.equal(updateState);
  });
});
