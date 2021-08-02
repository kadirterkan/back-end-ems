import axios from "axios";
import {MessageResponse} from "../../common/dto/MessageResponse";
import {UserLoginModel} from "../Login";

export interface UserQueryResponse{
    username:string;
    password:string;
}

export class LoginApi {

    async Login(userLoginModel: UserLoginModel): Promise<MessageResponse> {
        console.log(userLoginModel.username);
        const response = await axios.post<MessageResponse>("/login",userLoginModel);
        return response.data;
    }

    async loggedIn():Promise<boolean> {
        const response = await axios.get<boolean>("/logged");
        return response.data;
    }

    async getName():Promise<string> {
        const response = await axios.get<string>("getname");
        return response.data;
    }
}