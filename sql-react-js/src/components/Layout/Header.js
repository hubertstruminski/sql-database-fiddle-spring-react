import React from 'react';
import { Link } from 'react-router-dom';

class Header extends React.Component {
    render() {
        return (
            <header>
                <nav className="navbar navbar-expand-md navbar-dark bg-dark">
                    <div><Link to="/fiddle" className="navbar-brand">SQL Fiddle database</Link></div>
                    <ul className="navbar-nav">
                        <li>
                            <Link to="/run" className="nav-link">Run</Link>
                        </li>
                       <li>
                            <Link to="/save" className="nav-link">Save</Link>
                        </li>
                    </ul>
                    <ul className="navbar-nav navbar-collapse justify-content-end">
                        <li>
                            <Link to="/login" className="nav-link">Sign In</Link>
                        </li>
                        <li>
                            <Link to="/registration" className="nav-link">Sign Up</Link>
                        </li>
                        <li>
                            <Link to="/logout" className="nav-link">Logout</Link>
                            {/* onClick={} for up tag */}
                        </li>
                    </ul>
                </nav>
            </header>
        );
    }
}

export default Header;