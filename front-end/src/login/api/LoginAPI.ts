import axios from "axios";
import {MessageResponse} from "../../common/dto/MessageResponse";
import {UserLoginModel} from "../Login";

export interface UserQueryResponse{
    username:string;
    password:string;
}
export enum RoleEnum {
    MOD="MOD",
    USER="USER"
}

export class LoginApi {

    async Login(userLoginModel: UserLoginModel): Promise<MessageResponse> {
        const response = await axios.post<MessageResponse>("/login",userLoginModel);
        return response.data;
    }

    async loggedIn():Promise<boolean> {
        const response = await axios.get<boolean>("/logged");
        return response.data;
    }

}