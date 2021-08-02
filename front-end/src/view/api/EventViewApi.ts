import axios from "axios";
import {EventQueryResponse} from "../../moderator/api/ModApi";
import {MessageResponse} from "../../common/dto/MessageResponse";
import {EventModel} from "../EventView";


export class EventViewApi{
    async getEvents():Promise<EventQueryResponse[]> {
        const response = await axios.get<EventQueryResponse[]>("/controller/list");
        return response.data;
    }

    async addEventToUser(model: EventModel):Promise<MessageResponse>{
        const response = await axios.post<MessageResponse>("/controller/addeventuser",model);
        return response.data;
    }

}