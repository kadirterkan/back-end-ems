import axios, {AxiosResponse} from "axios";
import {EventQueryResponse, LittleEventRequest, ModApi} from "../../moderator/api/ModApi";
import {MessageResponse} from "../../common/dto/MessageResponse";
import {BitMatrix} from "@zxing/library";
import {EventModel} from "../../moderator/EventList/ModEventList";
import {byte} from "@zxing/library/es2015/customTypings";


export class EventViewApi{

    async getEvents():Promise<EventQueryResponse[]> {
        const response = await axios.get<EventQueryResponse[]>("/controller/list");
        return response.data;
    }

    async addEventToUser(eventName: string):Promise<MessageResponse | void>{
        let request: LittleEventRequest = {eventName:eventName};
        return axios.post("/controller/addeventuser",request)
            .then((value:AxiosResponse<MessageResponse>) => {
                return value.data;
            })
            .catch((reason) => {
                return ModApi.responseFromServerToast(reason);
            });
    }

    async bringQRCodeData(eventName:string):Promise<string | void>{
        let request: LittleEventRequest = {eventName:eventName};
        return axios.post("/controller/qrcode",request)
            .then((value:AxiosResponse<string>) => {
                console.log(value.data);
                return value.data;
            })
            .catch((reason) => {
                ModApi.responseFromServerToast(reason);
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