import React from 'react';
import TableButton from './TableButton';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { processQueries } from '../actions/queryActions';
import { getButtons } from '../actions/tableActions';

class Board extends React.Component {
    constructor() {
        super();

        this.state = {
            query: 'Write here your SQL query...'
        }
        this.onChange = this.onChange.bind(this);
        this.onSubmitRun = this.onSubmitRun.bind(this);
    }

    componentDidMount() {
        this.props.getButtons();
    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value });
    }

    onSubmitRun() {
        this.props.processQueries(this.state.query, this.props.history);
    }

    render() {
        const { buttons } = this.props.button;
        console.log(buttons);
        return (
            <div className="box flex-stretch">
                <div className="mediumClass">
                    <form onSubmit={this.onSubmitRun}>
                        <textarea 
                            name="query" 
                            className="txtArea"
                            value={this.state.query}
                            onChange={this.onChange}
                            rows="27"
                        >
                            Write here your SQL queries...
                        </textarea>
                        <input type="submit" value="Run" className="runButton"/>
                    </form>
                </div>
                <div className="blue smallClass">
                    {
                        buttons.map(button => (
                            <TableButton key={button.id} button={button} />
                        ))
                    }
                </div>
                <div className="red largeClass">
                    
                </div>
            </div>
        );
    }
}

Board.propTypes = {
    query: PropTypes.string,
    button: PropTypes.object.isRequired,
    getButtons: PropTypes.func.isRequired
}

const mapStateToProps = state => ({
    query: state.query,
    button: state.button
})

export default connect(mapStateToProps, { processQueries, getButtons })(Board);