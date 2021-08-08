import {MessageResponse} from "../../common/dto/MessageResponse";
import axios, {AxiosResponse} from "axios";
import {EventQueryResponse, LittleEventRequest, ModApi} from "../../moderator/api/ModApi";

interface EventIdRequest {
    eventId:number;
}


export class API {

    async leaveEvent(eventId:number): Promise<MessageResponse | void>{
        let eventIdRequest: EventIdRequest = {eventId:eventId};
        return axios.post("/controller/leave",eventIdRequest)
            .then((value:AxiosResponse<MessageResponse>) => {
                return value.data;
            })
            .catch((reason)=>{
                ModApi.responseFromServerToast(reason);
            })
    }

    async joinedEvents():Promise<EventQueryResponse[] | void> {
        return axios.get("/controller/getjoinedevents")
            .then((value:AxiosResponse<EventQueryResponse[]>)=>{
                return value.data;
            })
            .catch((reason) => {
                ModApi.responseFromServerToast(reason);
            })
    }

    async getEvent(eventname:string): Promise<EventQueryResponse | void>{
        let littleRequest: LittleEventRequest = {eventName:eventname}
        return axios.post("controller/getevent",littleRequest)
            .then((value:AxiosResponse<EventQueryResponse>) => {
                return value.data;
            })
            .catch((reason) => {
                return ModApi.responseFromServerToast(reason);
            });
    }
}