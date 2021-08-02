import {EventModel} from "../EventAdder/EventAdder";
import {MessageResponse} from "../../common/dto/MessageResponse";
import axios from "axios";
import Cookies from "js-cookie";

export interface EventQueryResponse{
    id:number;
    eventName:string;
    startTime:Date;
    endTime:Date;
    quota:number;
    attending:number;
}

export class ModApi {

    async eventAdder(eventModel: EventModel): Promise<MessageResponse> {
        const response = await axios.post<MessageResponse>("/controller/addevent",eventModel);
        return response.data;
    }

    async eventEdit(eventModel: EventModel): Promise<MessageResponse> {
        const response = await axios.post<MessageResponse>("/controller/editevent",eventModel);
        return response.data;
    }

    async getEvents(): Promise<EventQueryResponse[]>{
        const response = await axios.get<EventQueryResponse[]>("/controller/list")
        return response.data;
    }

    async deleteEvent(eventModel: EventModel): Promise<MessageResponse> {
        const response = await axios.post<MessageResponse>("/controller/delete",eventModel);
        return response.data;
    }
}