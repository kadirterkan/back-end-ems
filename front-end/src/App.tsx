import React, {useEffect, useState} from 'react';
import './App.css';
import {BrowserRouter, Route, Link, useLocation} from "react-router-dom";
import {UserRegistration} from "./registration/UserRegistration";
import {Login} from "./login/Login";
import {AdminView} from "./admin/AdminView";
import {ModView} from "./moderator/ModView";
import {LoginApi} from "./login/api/LoginAPI";
import {Logout} from "./logout/Logout";
import {EventView} from "./view/EventView";
import {toast} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';


function OnURLChange(){
    let location = useLocation();

    useEffect(() => {

    },[location])

}


function App() {

    // const location = useLocation();

    toast.configure();

    const [logged,setLogged] = useState(false);

    const loginApi = new LoginApi();

    const loggedIn = async () => {
        const response = await loginApi.loggedIn();

        if(response){
            // Cookies.remove("Authentication");
            setLogged(true);
        }
    }



  return (
      <div>
      {logged ? <BrowserRouter>
              <div className={"nav-bar"}>
                  <div className={"page-links"}>
                      <Link className={"navigation-link"} to={"/admin"}>Admin </Link>
                      <Link className={"navigation-link"} to={"/mod"}>Mod </Link>
                      <Link className={"navigation-link"} to={"/logout"}>Logout </Link>
                      <Link className={"navigation-link"} to={"/list"}>Event List</Link>
                  </div>
              </div>

              <div className={"app-layer"}>
                  <Route path={"/admin"} exact={true} component={AdminView}/>
                  <Route path={"/mod"} exact={true} component={ModView}/>
                  <Route path={"/logout"} exact={true} component={Logout}/>
                  <Route path={"/list"} exact={true} component={EventView}/>
              </div>
          </BrowserRouter> :
          <BrowserRouter>
              <div className={"nav-bar"}>
                  <div className={"page-links"}>
                      <Link className={"navigation-link"} to={"/registration"}>Registration</Link>
                      <Link className={"navigation-link"} to={"/login"}>Login</Link>
                      <Link className={"navigation-link"} to={"/admin"}>Admin</Link>
                      <Link className={"navigation-link"} to={"/mod"}>Mod</Link>
                      <Link className={"navigation-link"} to={"/list"}>Event List</Link>
                  </div>
              </div>

              <div className={"app-layer"}>
                  <Route path={"/registration"} exact={true} component={UserRegistration}/>
                  <Route path={"/login"} exact={true} component={Login}/>
                  <Route path={"/admin"} exact={true} component={AdminView}/>
                  <Route path={"/mod"} exact={true} component={ModView}/>
                  <Route path={"/list"} exact={true} component={EventView}/>
              </div>
          </BrowserRouter>
      }
      </div>
  );
}

export default App;
