import { GET_WELCOME_MESSAGE } from '../actions/types';

const initialState = {
    welcomeMessage: false
}

export default function(state = initialState, action) {
    switch(action.type) {
        case GET_WELCOME_MESSAGE:
            return {
                ...state,
                welcomeMessage: action.payload
            }
        default:
            return state;
    }
}