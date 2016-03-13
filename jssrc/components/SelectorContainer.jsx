import React from 'react'

export default React.createClass({
  render() {
    return (
      <div className="SelectorContainer">
        Selector Container
        <ul role="nav">
          <li><NavLink to="/" onlyActiveOnIndex >Home</NavLink></li>
          <li><NavLink to="/selector">Selector2</NavLink></li>
        </ul>
        {this.props.children}
      </div>
    )
  }
})
