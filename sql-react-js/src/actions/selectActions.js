import axios from 'axios';
import { GET_ERRORS, GET_TABLE } from './types';

export const getTable = (id, history) => async dispatch => {
    try {
        const res = await axios.get(`/fiddle/table/${id}`);
        dispatch({
            type: GET_TABLE,
            payload: res.data
        });
    } catch(error) {
        history.push("/fiddle");
    }
}