import axios from 'axios';
import { GET_ERRORS, PROCESS_QUERY } from './types';

export const processQueries = (query, history) => async dispatch => {
    try {
        console.log(query);
        await axios.post("/fiddle/run", query);
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