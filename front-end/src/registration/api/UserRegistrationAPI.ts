import {ModModel, UserModel} from "../UserRegistration";
import axios from "axios";
import {MessageResponse} from "../../common/dto/MessageResponse";

export interface UserQueryResponse{
    firstName:string;
    lastName:string;
    email:string;
    tcKimlikNumber:string;
    password:string;
}

export class UserRegistrationApi {

    async addUser(userModel: UserModel): Promise<MessageResponse> {
        const response = await axios.post<MessageResponse>("webapi/user/userreg",userModel);
        return response.data;
    }

    async addMod(mod: ModModel): Promise<MessageResponse> {
        const response = await axios.post<MessageResponse>("webapi/user/modreg",mod);
        return response.data;
    }

    async getUser(): Promise<UserQueryResponse[]> {
        const response = await axios.get<UserQueryResponse[]>("webapi/user/users");
        return response.data;
    }
}