import axios from "axios";
import {MessageResponse} from "../../common/dto/MessageResponse";
import {UserModel} from "../Login";

export interface UserQueryResponse{
    username:string;
    password:string;
}

export class UserApi {

    async Login(userModel: UserModel): Promise<MessageResponse> {
        const response = await axios.post<MessageResponse>("/login",userModel);
        return response.data;
    }

}