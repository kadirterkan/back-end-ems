import React from 'react';
import './App.css';
import {BrowserRouter, Route, Link} from "react-router-dom";
import {UserRegistration} from "./registration/UserRegistration";
import {Login} from "./login/Login";

function App() {
  return (
      <BrowserRouter>
        <div className={"nav-bar"}>
          <div className={"page-links"}>
            <Link className={"navigation-link"} to={"/registration"}>Registration</Link>
            <Link className={"navigation-link"} to={"/login"}>Login</Link>
          </div>
        </div>

        <div className={"app-layer"}>
          <Route path={"/registration"} exact={true} component={UserRegistration}/>
          <Route path={"/login"} exact={true} component={Login}/>
        </div>
      </BrowserRouter>
  );
}

export default App;
