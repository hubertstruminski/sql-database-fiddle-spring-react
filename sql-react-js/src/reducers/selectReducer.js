import { GET_TABLE } from '../actions/types';

export default function(state = [], action) {
    switch(action.type) {
        case GET_TABLE:
            return [
                ...state, ...action.payload
            ]
        default:
            return state;
    }
}