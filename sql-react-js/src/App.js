import React from 'react';
import './App.css';
import './light.png';
import Header from './components/Layout/Header';
import Guide from './components/Layout/Guide';
import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import Login from './components/UserManagement/Login';
import Register from './components/UserManagement/Register';

import store from './store';
import { Provider } from 'react-redux';

import jwt_decode from 'jwt-decode';
import setJWTToken from '../src/securityUtils/setJWTToken';
import { SET_CURRENT_USER } from './actions/types';
import { logout } from './actions/securityActions';
import SecuredRoute from '../src/securityUtils/SecureRoute';
import Board from './components/Board';
import MessageSuccess from './components/Layout/MessageSuccess';

const jwtToken = localStorage.jwtToken;

if(jwtToken) {
  setJWTToken(jwtToken);
  const decoded_jwtToken = jwt_decode(jwtToken);
  store.dispatch({
      type: SET_CURRENT_USER,
      payload: decoded_jwtToken
  });

  const currentTime = Date.now() / 1000;
  if(decoded_jwtToken.exp < currentTime) {
      store.dispatch(logout());
      window.location.href = "/";
  }

}

function App() {
  return (
    <Provider store={store}>
      <Router>
        <div className="App">
          <Header />

          <Route exact path="/" component={Login} />
          <Route exact path="/login" component={Login} />
`         <Route exact path="/registration" component={Register} />
          <Route exact path="/guide" component={Guide} />

          <Route exact path="/success" component={MessageSuccess} />
          
          <SecuredRoute exact path="/fiddle" component={Board} />
 
        </div>
      </Router>
    </Provider>
  );
}

export default App;
