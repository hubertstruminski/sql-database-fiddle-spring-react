import React from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { logout } from '../../actions/securityActions';

class Header extends React.Component {
    logout() {
        this.props.logout();
        window.location.href = "/login";
    }


    render() {
        const { validToken, user } = this.props.security
        const userIsAuthenticated = (
            <div className="collapse navbar-collapse">
                <ul className="navbar-nav">
                    <li>
                        <Link to="/save" className="nav-link">Save</Link>
                    </li>
                </ul>
                <ul className="navbar-nav navbar-collapse justify-content-end">
                    <li className="nav-item">
                        <Link className="nav-link " to="/fiddle">
                            <i className="fas fa-user-circle mr-1" />
                            {user.firstName} {user.lastName}
                        </Link>
                    </li>
                    <li>
                        <Link to="/logout" className="nav-link" onClick={this.logout.bind(this)}>Logout</Link>
                    </li>
                </ul>
            </div>
        );

        const userIsNotAuthenticated = (
            <div className="collapse navbar-collapse">
                <ul className="navbar-nav navbar-collapse justify-content-end">
                    <li>
                        <Link to="/login" className="nav-link">Sign In</Link>
                    </li>
                    <li>
                        <Link to="/registration" className="nav-link">Sign Up</Link>
                    </li>
                </ul>
            </div>
        );

        let menu;

        if(validToken && user) {
            menu = userIsAuthenticated;
        } else {
            menu = userIsNotAuthenticated;
        }
        return (
            <header>
                <nav className="navbar navbar-expand-md navbar-dark bg-dark margin">
                    <Link to="/fiddle" className="navbar-brand">SQL Fiddle database</Link>
                    {menu}
                </nav>
            </header>
        );
    }
}
Header.propTypes = {
    logout: PropTypes.func.isRequired,
    security: PropTypes.object.isRequired
}

const mapStateToProps = state => ({
    security: state.security
})

export default connect(mapStateToProps, {logout})(Header);