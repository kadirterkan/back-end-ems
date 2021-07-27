import {AuthorityAdd} from "../UserAuthorityAdd";
import {MessageResponse} from "../../common/dto/MessageResponse";
import axios from "axios";

export class UserAuthorityAdder{

    async AddAuthorityToUser(authorityAdd:AuthorityAdd): Promise<MessageResponse>{
        const response = await axios.post<MessageResponse>("/addauthority",authorityAdd);
        return response.data;
    }

}