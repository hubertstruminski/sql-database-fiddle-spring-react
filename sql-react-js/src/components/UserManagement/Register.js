import React from 'react';
import { createNewUser } from '../../actions/securityActions';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import classnames from 'classnames';

class Register extends React.Component {
    constructor() {
        super();

        this.state = {
            userName: '',
            password: '',
            matchingPassword: '',
            firstName: '',
            lastName: '',
            email: '',
            errors: {},
            successfulWelcomeMessage: false
        }
        this.onChange = this.onChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    componentWillReceiveProps(nextProps) {
        if(nextProps.errors) {
            this.setState({ errors: nextProps.errors });
        }
    }

    onChange(e) {
        this.setState({ [e.target.name]: e.target.value })
    }

    onSubmit(e) {
        e.preventDefault();

        const userRegisterValidator = {
            userName: this.state.userName,
            password: this.state.password,
            matchingPassword: this.state.matchingPassword,
            firstName: this.state.firstName,
            lastName: this.state.lastName,
            email: this.state.email            
        }
        this.setState({ successfulWelcomeMessage: true });
        this.props.createNewUser(userRegisterValidator, this.props.history);
    }

    render() {
        const { errors } = this.state
        return (
            <div className="container">
                <div className="row">
                    <div className="col-sm-9 col-md-7 col-lg-5 mx-auto">
                        <div className="card card-signin my-5">
                            <div className="card-body">
                                <h5 className="card-title text-center">Sign Up</h5>
                                <hr className="my-4" />

                                <ShowSuccessfulRegistrationMessage successfulWelcomeMessage={this.state.successfulWelcomeMessage} />
                                
                                <form onSubmit={this.onSubmit} className="form-signin">
                                    <div className="form-label-group">
                                        <input 
                                            type="text" 
                                            name="userName" 
                                            id="inputUser" 
                                            className={classnames("form-control", {
                                                "is-invalid": errors.userName
                                            })}  
                                            placeholder="User name" 
                                            autoFocus 
                                            value={this.state.userName} 
                                            onChange={this.onChange} 
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
                                            className={classnames("form-control", {
                                                "is-invalid": errors.password
                                            })}  
                                            placeholder="Password" 
                                            value={this.state.password} 
                                            onChange={this.onChange}
                                        />
                                        {
                                            errors.password && (
                                                <div className="invalid-feedback">{errors.password}</div>
                                            )
                                        }
                                        <label htmlFor="inputPassword">Password</label>
                                    </div>

                                    <div className="form-label-group">
                                        <input 
                                            type="password" 
                                            name="matchingPassword" 
                                            id="matchingPassword" 
                                            className={classnames("form-control", {
                                                "is-invalid": errors.matchingPassword
                                            })} 
                                            placeholder="Confirm password" 
                                            value={this.state.matchingPassword} 
                                            onChange={this.onChange} 
                                        />
                                        {
                                            errors.confirmPassword && (
                                                <div className="invalid-feedback">{errors.matchingPassword}</div>
                                            )
                                        }
                                        <label htmlFor="matchingPassword">Confirm password</label>
                                    </div>

                                    <div className="form-label-group">
                                        <input 
                                            type="text" 
                                            name="firstName" 
                                            id="firstName" 
                                            className={classnames("form-control", {
                                                "is-invalid": errors.firstName
                                            })} 
                                            placeholder="First name" 
                                            value={this.state.firstName} 
                                            onChange={this.onChange} 
                                        />
                                        {
                                            errors.firstName && (
                                                <div className="invalid-feedback">{errors.firstName}</div>
                                            )
                                        }
                                        <label htmlFor="firstName">First name</label>
                                    </div>

                                    <div className="form-label-group">
                                        <input 
                                            type="text" 
                                            name="lastName" 
                                            id="lastName" 
                                            className={classnames("form-control", {
                                                "is-invalid": errors.lastName
                                            })} 
                                            placeholder="Last name" 
                                            value={this.state.lastName} 
                                            onChange={this.onChange} 
                                        />
                                        {
                                            errors.lastName && (
                                                <div className="invalid-feedback">{errors.lastName}</div>
                                            )
                                        }
                                        <label htmlFor="lastName">Last name</label>
                                    </div>

                                    <div className="form-label-group">
                                        <input 
                                            type="text" 
                                            name="email" 
                                            id="email" 
                                            className={classnames("form-control", {
                                                "is-invalid": errors.email
                                            })} 
                                            placeholder="Email" 
                                            value={this.state.email} 
                                            onChange={this.onChange} 
                                        />
                                        {
                                            errors.email && (
                                                <div className="invalid-feedback">{errors.email}</div>
                                            )
                                        }
                                        <label htmlFor="email">Email</label>
                                    </div>

                                    <hr className="my-4" />
                                    <button className="btn btn-lg btn-primary btn-block text-uppercase" type="submit">Sign up</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

function ShowSuccessfulRegistrationMessage(props) {
    if(props.successfulWelcomeMessage) {
        return <div className="alert alert-success">User has been registered successfully</div>;
    }
    return null;
}

Register.propTypes = {
    createNewUser: PropTypes.func.isRequired,
    errors: PropTypes.object.isRequired,
    security: PropTypes.object.isRequired
}

const mapStateToProps = state => ({
    errors: state.errors,
    security: state.security
});

export default connect(mapStateToProps, { createNewUser })(Register);