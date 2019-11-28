import axios from 'axios';
import { GET_TABLE, BACKEND_URL } from './types';

export const getTable = (id, history) => async dispatch => {
    const res = await axios.get(`${BACKEND_URL}fiddle/table/${id}`);
    console.log(res);
    dispatch({
        type: GET_TABLE,
        payload: res.data
    });
}