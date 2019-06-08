import React from 'react';
import createTableImg from '../../images/1.png';
import insertImg from '../../images/2.png';
import updateImg from '../../images/3.png';
import deleteImg from '../../images/4.png';

class Guide extends React.Component {
    render() {
        return (
            <div className="divGuide">
                <h1 className="card-title text-center">
                    USER GUIDE
                </h1>
                <hr className="my-4" />
                <ul>
                    <li>
                        <h5>CREATE TABLE query</h5>
                        <p>Firstly, You should keep one whitespace distance between table name and parenthesis begins declaration of columns.</p>
                        <p>Secondly, You should keep the same distance beetwen column name and type of column.</p>
                        <img 
                            className="mx-auto d-block img"
                            alt="" 
                            src={createTableImg} 
                        />
                    </li>  
                    <li>
                        <h5>INSERT INTO query</h5>
                        <p>You should keep one whitespace distance beetwen VALUES word and parenthesis begins set of values.</p>
                        <p>The distance should be keep between table name and parenthesis too.</p>
                        <img 
                            className="mx-auto d-block img" 
                            alt=""
                            src={insertImg} 
                        />
                    </li> 
                    <li>
                        <h5>UPDATE query</h5>
                        <p>You should keep one whitespace distance before equals sign and after it when You assign values to columns in SET clause.</p>
                        <p>UPDATE query will work only when you search for ID column in WHERE clause.</p>
                        <img 
                            className="mx-auto d-block img"
                            alt="" 
                            src={updateImg} 
                        />
                    </li>
                    <li>
                        <h5>DELETE query</h5>
                        <p>DELETE query will work only when you search for ID column of specified table in WHERE clause.</p>
                        <img 
                            className="mx-auto d-block img" 
                            alt=""
                            src={deleteImg} 
                        />
                    </li>
                </ul> 
                
            </div>
        );
    }
}

export default Guide;