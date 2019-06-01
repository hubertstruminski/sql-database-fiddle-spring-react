import axios from 'axios';
import { GET_ERRORS, PROCESS_QUERY } from './types';

export const processQueries = (query, user, history) => async dispatch => {
    try {
        console.log(query);
        await axios.post("/fiddle", query, user);
        history.push("/fiddle");

        dispatch({
            type: PROCESS_QUERY,
            payload: ''
        })
    } catch(error) {
        dispatch({
            type: GET_ERRORS,
            payload: error.response.data
        })
    }
}