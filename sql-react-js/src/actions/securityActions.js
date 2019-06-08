import axios from 'axios';
import { GET_ERRORS, SET_CURRENT_USER } from './types';
import setJWTToken from '../securityUtils/setJWTToken';
import jwt_decode from 'jwt-decode';

export const createNewUser = (userRegisterValidator, history) => async dispatch => {
    try {
        await axios.post("/register", userRegisterValidator);
        history.push("/success");
        
        dispatch({
            type: GET_ERRORS,
            payload: {}
        });
    } catch(error) {
        dispatch({
            type: GET_ERRORS,
            payload: error.response.data
        })
    }
}

export const login = invalidLoginResponse => async dispatch => {
    try {
        const res = await axios.post("/login", invalidLoginResponse);
        
        const { token } = res.data;
        localStorage.setItem("jwtToken", token);
        
        setJWTToken(token);
        const decoded = jwt_decode(token);

        dispatch({
            type: SET_CURRENT_USER,
            payload: decoded
        });
    } catch(error) {
        dispatch({
            type: GET_ERRORS,
            payload: error.response.data
        });
    }
};

export const logout = () => dispatch => {
    localStorage.removeItem("jwtToken");
    setJWTToken(false);
    dispatch({
        type: SET_CURRENT_USER,
        payload: {}
    });
};