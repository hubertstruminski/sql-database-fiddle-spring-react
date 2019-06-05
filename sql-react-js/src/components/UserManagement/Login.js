import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import classnames from 'classnames';
import { login, passWelcomeMessage } from '../../actions/securityActions';


class Login extends React.Component {
    constructor() {
        super();

        this.state = {
            userName: '',
            password: '',
            isRegistrationMessage: false,
            errors: {}
        }
        this.onChange = this.onChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value })
    }

    componentWillReceiveProps(nextProps) {
        if(nextProps.security.validToken) {
            this.props.history.push("/fiddle");
        }
 
        if(nextProps.errors) {
            this.setState({ errors: nextProps.errors });
        }
    }

    componentDidMount() {
        if(this.props.security.validToken) {
            this.props.history.push("/fiddle")
        }
    }

    setRegistrationMessage() {
        this.setState({ isRegistrationMessage: true });
    }

    onSubmit(e) {
        e.preventDefault();

        const invalidLoginResponse = {
            userName: this.state.userName,
            password: this.state.password
        }
        this.props.login(invalidLoginResponse);
    }

    render() {
        const { errors } = this.state;

        const { welcomeMessage } = this.props;
        let isRegistrationMessage = this.state.isRegistrationMessage;
        isRegistrationMessage = welcomeMessage["welcomeMessage"];
        console.log(welcomeMessage);
        return (
            <div className="box">
                <div className="container">
                    <div className="row">
                        <div className="col-sm-9 col-md-7 col-lg-5 mx-auto">
                            <div className="card card-signin my-5">
                                <div className="card-body">
                                    <h5 className="card-title text-center">Sign In</h5>
                                    <hr className="my-4" />

                                    {
                                        isRegistrationMessage && <ShowSuccessfulRegistrationMessage message={welcomeMessage} />
                                    }

                                    <form onSubmit={this.onSubmit} className="form-signin">
                                        <div className="form-label-group">
                                            <input 
                                                type="text" 
                                                name="userName" 
                                                id="inputUser" 
                                                value={this.state.userName} 
                                                onChange={this.onChange} 
                                                className={classnames("form-control", {
                                                    "is-invalid": errors.userName
                                                })}  
                                                placeholder="User name" 
                                                autoFocus 
                                            />
                                            {
                                                errors.userName && (
                                                    <div className="invalid-feedback">{errors.userName}</div>
                                                )
                                            }
                                            <label htmlFor="inputUser">User name</label>
                                        </div>

                                        <div className="form-label-group">
                                            <input 
                                                type="password" 
                                                name="password" 
                                                id="inputPassword" 
                                                value={this.state.password} 
                                                onChange={this.onChange} 
                                                className={classnames("form-control", {
                                                    "is-invalid": errors.password
                                                })} 
                                                placeholder="Password" 
                                            />
                                            {
                                                errors.password && (
                                                    <div className="invalid-feedback">{errors.password}</div>
                                                )
                                            }
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

function ShowSuccessfulRegistrationMessage(message) {
    if(message) {
        return <div className="alert alert-success">User has been registered successfully</div>;
    }
    return null;
}

Login.propTypes = {
    login: PropTypes.func.isRequired,
    errors: PropTypes.object.isRequired,
    security: PropTypes.object.isRequired,
    welcomeMessage: PropTypes.bool.isRequired
}

const mapStateToProps = state => ({
    security: state.security,
    errors: state.errors,
    welcomeMessage: state.welcomeMessage
})

export default connect(mapStateToProps, {login, passWelcomeMessage})(Login);