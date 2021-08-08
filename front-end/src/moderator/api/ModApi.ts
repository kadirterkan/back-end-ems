import {EventRequest} from "../EventAdder/EventAdder";
import {MessageResponse, MessageType} from "../../common/dto/MessageResponse";
import axios, {AxiosResponse} from "axios";
import {EventModel} from "../EventList/ModEventList";
import {toast} from "react-toastify";

export interface EventQueryResponse{
    id:string,
    title:string,
    start:Date,
    end:Date,
    quota:number;
    attending:number;
}

export interface LittleEventRequest{
    eventName:string
}

export interface UpdateEventRequest{
    eventName:string;
    quota:number;
    startTime:Date;
    endTime:Date;
    oldEventName:string;
}

export interface UserQueryResponse{
    firstName:string;
    lastName:string;
    tcKimlikNumber:string;
}

export class ModApi {

    async responseFromServer(reason:any): Promise<MessageResponse> {

        if (reason.response!.status === 400) {
            let response: MessageResponse = {
                messageType: MessageType.ERROR_400,
                message: "YOU HAVE MADE A BAD REQUEST"
            };
            return response;
        } else if (reason.response!.status === 403) {
            let response: MessageResponse = {
                messageType: MessageType.ERROR_403,
                message: "YOU DON'T HAVE PERMISSION FOR THIS"
            };
            return response;
        } else if (reason.response!.status === 500) {
            let response: MessageResponse = {
                messageType: MessageType.ERROR_500,
                message: "SERVER DOESN'T RESPONSE"
            };
            return response;
        } else {
            let response: MessageResponse = {
                messageType: MessageType.ERROR,
                message: "SOMETHING HAPPENED"
            };
            return response;
        }
        let response: MessageResponse = {
            messageType: MessageType.ERROR,
            message: "SOMETHING HAPPENED"
        };
        return response;
    }

    static async responseFromServerToast(reason:any): Promise<void> {
        if (reason.response!.status === 400) {
            toast.error("YOU HAVE MADE A BAD REQUEST");
        } else if (reason.response!.status === 403) {
            toast.error("YOU DON'T HAVE PERMISSION FOR THIS");
        } else if (reason.response!.status === 500) {
            toast.error("SERVER DOESN'T RESPONSE");
        } else {
            toast.error("SOMETHING HAPPENED");
        }
    }


    async eventAdder(eventModel: EventModel): Promise<MessageResponse> {
        return axios.post<MessageResponse>("/controller/addevent",eventModel)
            .then((value:AxiosResponse<MessageResponse>) => {
                return value.data
            })
            .catch((reason) => {
                return this.responseFromServer(reason);
            });
    }

    async eventEdit(eventModel: UpdateEventRequest): Promise<MessageResponse> {
        return axios.post("/controller/editevent", eventModel)
            .then((value: AxiosResponse<MessageResponse>) => {
                return value.data;
            })
            .catch((reason) => {
                return this.responseFromServer(reason);
            });
    }

    async getEvents(): Promise<EventQueryResponse[] | void>{
        return axios.get("/controller/list")
            .then((value:AxiosResponse<EventQueryResponse[]>) => {
                return value.data;
            })
            .catch((reason) => {
                return ModApi.responseFromServerToast(reason);
            });
    }

    async deleteEvent(eventModel: EventRequest): Promise<MessageResponse> {
        return axios.post("/controller/delete",eventModel)
            .then((value:AxiosResponse<MessageResponse>) => {
                return value.data;
            })
            .catch((reason) => {
                return this.responseFromServer(reason);
            });
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