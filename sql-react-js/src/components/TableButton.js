import React from 'react';

class TableButton extends React.Component {
    constructor() {
        super();

        this.state = {

        }
        this.onSubmit = this.onSubmit.bind(this);
    }

    onSubmit(e) {
        e.preventDefault();
        alert('Hubert Strumiński');
    }
    render() {
        return (
            <form onSubmit={this.onSubmit}>
                <input type="submit" className="generatedButton" value="Button" />
            </form>
        );
    }
}

export default TableButton;