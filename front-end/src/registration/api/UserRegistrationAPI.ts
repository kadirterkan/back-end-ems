import {UserModel} from "../UserRegistration";
import axios from "axios";
import {MessageResponse} from "../../common/dto/MessageResponse";

export interface UserQueryResponse{
    firstName:string;
    lastName:string;
    email:string;
    tcKimlikNumber:string;
    password:string;
}

export class UserApi {

    async addUser(userModel: UserModel): Promise<MessageResponse> {
        const response = await axios.post<MessageResponse>("/registration",userModel);
        return response.data;
    }

    async getUser(): Promise<UserQueryResponse[]> {
        const response = await axios.get<UserQueryResponse[]>("/users");
        return response.data;
    }
}