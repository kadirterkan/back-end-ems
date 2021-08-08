import React, {useEffect, useState} from 'react';
import './App.css';
import {BrowserRouter, Route, Link, useLocation, useHistory, Redirect} from "react-router-dom";
import {UserRegistration} from "./registration/UserRegistration";
import {Login} from "./login/Login";
import {ModView} from "./moderator/ModView";
import {LoginApi, RoleEnum} from "./login/api/LoginAPI";
import {Logout} from "./logout/Logout";
import {EventView} from "./view/EventView";
import {toast} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import Cookies from "js-cookie";
import {Button} from "@material-ui/core";
import JoinedEvents from "./joinedEvents/JoinedEvents";


function App() {

    const [modRole,setModRole] = useState("USER");
    const [logged,setLogged] = useState(false);
    const [isLoginModelOpen,setIsLoginModelOpen] = useState(false);

    const loginApi = new LoginApi();

    const loggedIn = async () => {
        const response = await loginApi.loggedIn();

        if(response){
            // Cookies.remove("Authentication");
            setLogged(true);
        }
    }


    const handleLogOut = () => {
        setLogged(false);
        Cookies.remove("Authority");

        return <Redirect to={"/logout"}/>
    }

    const GuestScreen = () => {
        return(
            <BrowserRouter>
                <div className={"nav-bar"}>
                    <div className={"page-links"}>
                        <Button color={"primary"} onClick={() => setIsLoginModelOpen(true)}>Login</Button>
                        <Login handleClose={() => setIsLoginModelOpen(false)} isOpen={isLoginModelOpen} loggedIn={()=>setLogged(true)} role={(value)=>value}/>
                        <Link className={"navigation-link"} to={"/registration"}>Registration</Link>
                        <Link className={"navigation-link"} to={"/mod"}>Mod</Link>
                        <Link className={"navigation-link"} to={"/list"}>Event List</Link>
                    </div>
                </div>
                <div>

                </div>
                <div className={"app-layer"}>
                    <Route path={"/registration"} exact={true} component={UserRegistration}/>
                    <Route path={"/login"} exact={true} component={Login}/>
                    <Route path={"/mod"} exact={true} component={ModView}/>
                    <Route path={"/list"} exact={true} component={EventView}/>
                </div>
            </BrowserRouter>
        );
    }

    const ModScreen = () => {
        return(
            <BrowserRouter>
                <div className={"nav-bar"}>
                    <div className={"page-links"}>
                        <Button color={"primary"} onClick={() => handleLogOut()}>Logout</Button>
                        <Link className={"navigation-link"} to={"/mod"}>Mod </Link>
                        <Link className={"navigation-link"} to={"/list"}>Event List</Link>
                    </div>
                </div>

                <div className={"app-layer"}>
                    <Route path={"/mod"} exact={true} component={ModView}/>
                    <Route path={"/list"} exact={true} component={EventView}/>
                </div>
            </BrowserRouter>
        );
    }

    const UserScreen = () => {
        return (
            <BrowserRouter>
                <div className={"nav-bar"}>
                    <div className={"page-links"}>
                        <Button color={"primary"} onClick={() => handleLogOut()}>Logout</Button>
                        <Link className={"navigation-link"} to={"/list"}>Event List</Link>
                        <Link className={"navigation-link"} to={"/joined-list"}>Joined Event List</Link>
                    </div>
                </div>
                <div className={"app-layer"}>
                    <Route path={"/list"} exact={true} component={EventView}/>
                    <Route path={"/joined-list"} exact={true} component={JoinedEvents}/>
                </div>
            </BrowserRouter>
        );
    }

    useEffect(()=>{
        toast.configure();
        loggedIn();
            },[]);


  return (
      <div className={"App"}>
      {logged ?
          <div>
          { modRole == "MOD" ?
              <div>
              <ModScreen/>
              </div>
              :
              <div>
              <UserScreen/>
              </div>

      } </div>:
              <div>
          <GuestScreen/>
              </div>
           }
      </div>
  );


}

export default App;
