import React from 'react';
import NavLink from './NavLink.jsx';
import PureRenderMixin from 'react-addons-pure-render-mixin';
import ButtonGroup from 'react-bootstrap/lib/ButtonGroup';
import Button from 'react-bootstrap/lib/Button';
import Panel from 'react-bootstrap/lib/Panel';

const menu = (
  <ButtonGroup>
    <Button><NavLink to="/" onlyActiveOnIndex>Home</NavLink></Button>
    <Button><NavLink to="selector1">Selector1</NavLink></Button>
    <Button><NavLink to="selector2">Selector2</NavLink></Button>
  </ButtonGroup>
);

export default React.createClass({
  mixins: [PureRenderMixin],

  render() {
    return (
      <Panel header={menu}>
          {this.props.children}
      </Panel>
    );
  }
});
