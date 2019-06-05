import { combineReducers } from 'redux';
import errorReducer from './errorReducer';
import securityReducer from './securityReducer';
import tableReducer from './tableReducer';
import selectReducer from './selectReducer';
import welcomeMessageReducer from './welcomeMessageReducer';

export default combineReducers({
    errors: errorReducer,
    security: securityReducer,
    button: tableReducer,
    table: selectReducer,
    welcomeMessage: welcomeMessageReducer
});