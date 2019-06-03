import { GET_TABLE } from '../actions/types';

const initialState = {
    table: {}
}

export default function(state = initialState, action) {
    switch(action.type) {
        case GET_TABLE:
            return {
                ...state,
                table: action.payload
            }
        default:
            return state;
    }
}