import axios from 'axios';
import { DISPLAY_BUTTONS, DISPLAY_BUTTON } from './types';

export const getButtons = () => async dispatch => {
    const res = await axios.get("/fiddle/all");
    dispatch({
        type: DISPLAY_BUTTONS,
        payload: res.data
    });
};

export const getButton = (id, history) => async dispatch => {
    try {
        const res = await axios.get(`/fiddle/button/${id}`);
        dispatch({
            type: DISPLAY_BUTTON,
            payload: res.data
        });
    } catch(error) {
        history.push("/fiddle");
    }
}