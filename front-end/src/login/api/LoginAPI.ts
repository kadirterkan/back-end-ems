import axios from "axios";
import {MessageResponse} from "../../common/dto/MessageResponse";
import {UserLoginModel} from "../Login";

export interface UserQueryResponse{
    username:string;
    password:string;
}

export class UserApi {

    async Login(userLoginModel: UserLoginModel): Promise<MessageResponse> {
        console.log(userLoginModel.username);
        const response = await axios.post<MessageResponse>("/login",userLoginModel);
        return response.data;
    }

}