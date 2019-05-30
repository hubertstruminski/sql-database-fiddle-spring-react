import React from 'react';

class Login extends React.Component {
    constructor() {
        super();

        this.state = {
            userName: '',
            password: ''
        }
        this.onChange = this.onChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value })
    }

    onSubmit() {

    }

    render() {
        return (
            <div className="box">
                <div className="container">
                    <div className="row">
                        <div className="col-sm-9 col-md-7 col-lg-5 mx-auto">
                            <div className="card card-signin my-5">
                                <div className="card-body">
                                    <h5 className="card-title text-center">Sign In</h5>
                                    <hr className="my-4" />
                                    <form onSubmit={this.onSubmit} className="form-signin">
                                        <div className="form-label-group">
                                            <input 
                                                type="text" 
                                                name="userName" 
                                                id="inputUser" 
                                                value={this.state.userName} 
                                                onChange={this.onChange} 
                                                className="form-control" 
                                                placeholder="User name" 
                                                autoFocus 
                                            />
                                            <label htmlFor="inputUser">User name</label>
                                        </div>

                                        <div className="form-label-group">
                                            <input 
                                                type="password" 
                                                name="password" 
                                                id="inputPassword" 
                                                value={this.state.password} 
                                                onChange={this.onChange} 
                                                className="form-control" 
                                                placeholder="Password" 
                                            />
                                            <label htmlFor="inputPassword">Password</label>
                                        </div>

                                        <hr className="my-4" />
                                        <button className="btn btn-lg btn-primary btn-block text-uppercase" type="submit">Sign in</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Login;