import {useHistory} from "react-router-dom";
import Cookies from "js-cookie";

export function Logout(){

    const history = useHistory();

    const logoutHandler = () => {
        Cookies.remove("Authentication");

        history.push("/");
    }

    setTimeout(()=> logoutHandler(),2000);

    return(
        <div>
        <div>YOU HAVE SUCCESSFULLY LOGGED OUT</div>
        <div>NOW YOU'RE BEING DIRECTED TO THE MAIN PAGE</div>
        </div>
    );
}