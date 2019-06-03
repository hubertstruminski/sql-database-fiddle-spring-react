import { DISPLAY_BUTTONS, DISPLAY_BUTTON } from '../actions/types';

const initialState = {
    buttons: [],
    button: {}
}

export default function(state = initialState, action) {
    switch(action.type) {
        case DISPLAY_BUTTONS:
            return {
                ...state,
                buttons: action.payload
            }
        case DISPLAY_BUTTON:
            return {
                ...state,
                button: action.payload
            }
        default:
            return state;
    }
}