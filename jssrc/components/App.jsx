// Renders all the markup that is common from all the components
import React from 'react';
import PureRenderMixin from 'react-addons-pure-render-mixin';
import MarketFilter from '../containers/MarketFilter';
import Market from './Markets.jsx';
import Header from './Header.jsx';
import Selectors from './Selectors.jsx';
import Grid from 'react-bootstrap/lib/Grid';
import Col from 'react-bootstrap/lib/Col';
import Row from 'react-bootstrap/lib/Row';

export default React.createClass({
  mixins: [PureRenderMixin],

  render() {
    return (
      <Grid>
        <Row>
          <Col xs={12}>
            <Header />
          </Col>
        </Row>
        <Row>
          <Col xs={12} lg={6}>
            <Selectors>
              {this.props.children}
            </Selectors>
          </Col>
          <Col xs={12} lg={6}>
            <Market />
          </Col>
        </Row>
        <Row>
          <Col xs={12}>
            <MarketFilter />
          </Col>
        </Row>
      </Grid>
    );
  }
});
