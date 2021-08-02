import React, {useState} from 'react';
import './App.css';
import {BrowserRouter, Route, Link} from "react-router-dom";
import {UserRegistration} from "./registration/UserRegistration";
import {Login} from "./login/Login";
import {AdminView} from "./admin/AdminView";
import {ModView} from "./moderator/ModView";
import {LoginApi} from "./login/api/LoginAPI";
import {Logout} from "./logout/Logout";
import {EventView} from "./view/EventView";
import {Button} from "@material-ui/core";
import {toast} from "react-toastify";
import axios from "axios";
import 'react-toastify/dist/ReactToastify.css';
import Cookies from "js-cookie";
import {MessageResponse, MessageType} from "./common/dto/MessageResponse";



function App() {

    toast.configure();

    // const getGuestPermission = async() => {
    //     const response = await axios.get<MessageResponse>("/guest");
    //     if(response.data.messageType === MessageType.SUCCESS){
    //         Cookies.set("Authentication",response.data.message);
    //     }
    //     else {
    //         toast.error("SOME ERROR OCCURRED. PLEASE RELOAD THE PAGE");
    //     }
    // }



    const workworkwork = () => {
        toast.success("HEY THERE");
    }

    const [logged,setLogged] = useState(false);

    const loginApi = new LoginApi();

    const loggedIn = async () => {
        const response = await loginApi.loggedIn();

        if(response){
            // Cookies.remove("Authentication");
            setLogged(true);
        }
    }

    // const ifNotLogged = () => {
    //     if(!logged){
    //         getGuestPermission();
    //     }
    // }

    // ifNotLogged();

    loggedIn();

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
                  </div>
              </div>

              <div className={"app-layer"}>
                  <Route path={"/registration"} exact={true} component={UserRegistration}/>
                  <Route path={"/login"} exact={true} component={Login}/>
                  <Route path={"/admin"} exact={true} component={AdminView}/>
                  <Route path={"/mod"} exact={true} component={ModView}/>
              </div>
          </BrowserRouter>
      }
      <Button onClick={workworkwork}>CLICK</Button>
      </div>
  );
}

export default App;
