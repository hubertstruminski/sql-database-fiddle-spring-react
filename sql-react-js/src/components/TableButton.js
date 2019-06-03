import React from 'react';

class TableButton extends React.Component {
    constructor() {
        super();

        this.onSubmit = this.onSubmit.bind(this);
    }

    onSubmit(e) {
        e.preventDefault();
        alert('Hubert Strumiński');
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

export default TableButton;