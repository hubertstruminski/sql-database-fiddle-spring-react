import React from 'react';
import TableButton from './TableButton';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { processQueries } from '../actions/queryActions';
import { getButtons } from '../actions/tableActions';
import { getTable } from '../actions/selectActions';

class Board extends React.Component {
    constructor() {
        super();

        this.state = {
            query: 'Write here your SQL query...',
            isClickedButton: false
        }
        this.onChange = this.onChange.bind(this);
        this.onSubmitRun = this.onSubmitRun.bind(this);
        this.setButtonProperties = this.setButtonProperties.bind(this);
        this.onSubmitClear = this.onSubmitClear.bind(this);
    }

    componentDidMount() {
        this.props.getButtons();
    }

    componentDidUpdate() {
        this.props.getTable();
    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value });
    }

    onSubmitRun() {
        this.props.processQueries(this.state.query, this.props.history);
    }

    setButtonProperties() {
        this.setState({ isClickedButton: true });
    }

    createTable = (...table) => {
        let result = [];
        for(let i=0; i<table.length; i++) {
            for(let j=0; j<table[i].length; j++) {
                let children = [];
                for(let k=0; k<table[i][j].length; k++) {
                    children.push(<td key={table[0][j][k]}>{ table[i][j][k] }</td>);
                }
                result.push(<tr>{children}</tr>);
            }  
        }
        return result;
    }

    onSubmitClear(e) {
        this.tbody.innerHTML = "";
    }

    render() {
        const { buttons } = this.props.button;
        
        const { table } = this.props;
        const isClickedButton = this.state.isClickedButton;

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
                    <h3>Your tables:</h3>
                    <br />
                    {
                        buttons.map(button => (
                            <TableButton key={button.id} button={button} setButtonProperties={this.setButtonProperties} onSubmitClear={this.onSubmitClear}/>
                        ))
                    }
                    <hr className="my-4" /> 
                    <form onSubmit={this.onSubmitClear}>
                        <input type="submit" value="Clear" className="clearButton" />
                    </form>
                </div>
                <div className="red largeClass">
                    <div className="table-responsive-sm">
                        <table className="table table-striped table-dark table-radius table-hover table-margin">
                            <tbody ref={(el) => this.tbody = el}>
                                {isClickedButton && this.createTable(table).slice()}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        );
    }
}

Board.propTypes = {
    query: PropTypes.string,
    button: PropTypes.object.isRequired,
    getButtons: PropTypes.func.isRequired,
    table: PropTypes.array.isRequired
}

const mapStateToProps = state => ({
    query: state.query,
    button: state.button,
    table: state.table
})

export default connect(mapStateToProps, { processQueries, getButtons, getTable })(Board);