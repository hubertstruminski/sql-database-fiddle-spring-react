import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { getTable } from '../actions/selectActions';

class TableButton extends React.Component {
    constructor() {
        super();

        this.onSubmit = this.onSubmit.bind(this);
    }

    onSubmit(e) {
        const id = button.id;
        this.props.getTable(id, this.props.history);
    }

    render() {
        const { button } = this.props;
        return (
            <form onSubmit={this.onSubmit}>
                <input type="submit" className="generatedButton" value={button.tableNameBefore} />
            </form>
        );
    }
}

TableButton.propTypes = {
    table: PropTypes.object.isRequired
}

const mapStateToProps = state => ({
    table: state.table
})

export default connect(mapStateToProps, { getTable })(TableButton);