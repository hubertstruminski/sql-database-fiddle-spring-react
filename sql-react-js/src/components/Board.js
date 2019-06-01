import React from 'react';
import TableButton from './TableButton';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { processQueries } from '../actions/queryActions';

class Board extends React.Component {
    constructor() {
        super();

        this.state = {
            query: 'Write here your SQL query...'
        }
        this.onChange = this.onChange.bind(this);
        this.resetField = this.resetField.bind(this);
        this.onSubmitRun = this.onSubmitRun.bind(this);
    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value });
    }

    resetField(e) {
        this.setState({ query: '' });
    }

    onSubmitRun(e) {
        e.preventDefault();
        console.log(this.state.query);
        this.props.processQueries(this.state.query, this.props.history);
    }

    render() {
        return (
            <div className="box flex-stretch">
                <div className="blue smallClass">
                    <TableButton />
                </div>
                <div className="mediumClass">
                    <form onSubmit={this.onSubmitRun}>
                        <textarea 
                            name="query" 
                            className="txtArea"
                            value={this.state.query}
                            onChange={this.onChange}
                            onClick={this.resetField}
                            rows="27"
                        >
                            Write here your SQL queries...
                        </textarea>
                        <input type="submit" value="Run" className="runButton"/>
                    </form>
                </div>
                <div className="red largeClass">
                    One of three columns
                </div>
            </div>
        );
    }
}

Board.propTypes = {
    query: PropTypes.string
}

const mapStateToProps = state => ({
    query: state.query
})

export default connect(mapStateToProps, { processQueries })(Board);