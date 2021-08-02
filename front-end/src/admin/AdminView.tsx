import {BrowserRouter,Link, Route} from "react-router-dom";
import {UserAuthorityAdd} from "./AuthorityProvider/UserAuthorityAdd";

export function AdminView() {
    return(
        <BrowserRouter>
            <div className={"nav-bar"}>
                <div className={"page-links"}>
                    <Link className={"navigation-link"} to={"/authorityprovider"}>Authority Provider</Link>
                </div>
            </div>
            <div className={"app-layer"}>
                <Route path={"/authorityprovider"} exact={true} component={UserAuthorityAdd}></Route>
            </div>
        </BrowserRouter>
    )
}