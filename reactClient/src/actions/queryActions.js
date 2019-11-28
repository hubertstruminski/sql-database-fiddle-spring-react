import axios from 'axios';
import { GET_ERRORS, PROCESS_QUERY, BACKEND_URL } from './types';

export const processQueries = (query, history) => async dispatch => {
    try {
        await axios.post(`${BACKEND_URL}fiddle/run`, query);
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