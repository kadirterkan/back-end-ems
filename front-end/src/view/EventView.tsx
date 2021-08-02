import EventList from "./EventList/EventList";
import {useEffect, useState} from "react";
import {GridRowParams} from "@material-ui/data-grid";
import {EventQueryResponse} from "../moderator/api/ModApi";
import {EventViewApi} from "./api/EventViewApi";
import {MessageType} from "../common/dto/MessageResponse";
import {toast} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import {Button} from "@material-ui/core";
import {QRCodeShow} from "./QR/QRCodeShow";



export interface EventModel{
    quota:number;
    eventName:string;
    startTime:Date;
    endTime:Date;
}

const initialstate: EventModel = {
    quota:0,
    eventName:"",
    startTime:new Date(),
    endTime:new Date(),
}


export function EventView(){


    const eventViewApi = new EventViewApi();

    const [isQRViewerOpen, setIsQRViewerOpen] = useState(false);


    const [eventQueryResponses, setEventQueryResponses] = useState<EventQueryResponse[]>([]);
    const [selected,setSelected]=useState(false);
    const [selectedItem,setSelectedItem] = useState<EventModel>(initialstate);



    const addEventToUser = async () => {

        console.log(selectedItem.eventName);
        const messageResponse = await eventViewApi.addEventToUser(selectedItem);
        if(messageResponse.messageType === MessageType.SUCCESS){
            toast.success(messageResponse.message);
            setIsQRViewerOpen(true);
            fetchEvents();
        }
        else {
            toast.error(messageResponse.message);
        }

    }

    const toEventModel = (value: GridRowParams) => {
        if(selected){
            let newEventModel= {...selectedItem};

            newEventModel.eventName = value.getValue(value.id, "eventName") as string;
            newEventModel.quota = value.getValue(value.id, "quota") as number;
            newEventModel.startTime = value.getValue(value.id,"startTime") as Date;
            newEventModel.endTime = value.getValue(value.id, "endTime") as Date;

            setSelectedItem(newEventModel);
        }
    }

    function fetchEvents(){
        eventViewApi.getEvents()
            .then(data => setEventQueryResponses(data));
    }

    useEffect(() => {
        fetchEvents();
    }, []);

    return(
        <div>
            <EventList  events={eventQueryResponses} selected={(value) => toEventModel(value)} selectedItem={(value) => setSelected(value)}/>
            {selected ?
                <Button onClick={addEventToUser}>JOIN</Button>
                :
                <div>CLICK TO AN EVENT TO JOIN</div>
            }
            {
                isQRViewerOpen ?
                    <QRCodeShow isOpen={isQRViewerOpen} handleClose={() => setIsQRViewerOpen(false)} model={selectedItem}/>
                    :
                    <div/>
            }
        </div>
    );
}

export default EventView;