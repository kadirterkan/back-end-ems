import {useEffect, useState} from "react";
import {EventQueryResponse} from "../moderator/api/ModApi";
import {EventViewApi} from "./api/EventViewApi";
import 'react-toastify/dist/ReactToastify.css';
import EventList from "./EventList/EventList";
import {JoinEvent} from "./JoinEvent/JoinEvent";
import {QRCodeShow} from "./QR/QRCodeShow";
import {EventClickArg} from "@fullcalendar/core";


export interface EventModel{
    quota:number;
    eventName:string;
    startTime:Date;
    endTime:Date;
}

export function EventView(){


    const eventViewApi = new EventViewApi();

    const [isQRViewerOpen, setIsQRViewerOpen] = useState(false);
    const [isMounted,setIsMounted] = useState(false);
    const [events, setEvents] = useState<EventQueryResponse[]>([]);
    const [selectedEvent,setSelectedEvent] = useState<EventQueryResponse>({id:"0",
        title:"",
        start:new Date(),
        end:new Date(),
        quota:0,
        attending:0});


    const [isJoinEventDialogOpen,setIsJoinEventDialogOpen] = useState(false);


    function fetchEvents(){
        setIsMounted(false);
        eventViewApi.getEvents()
            .then(value => setEvents(value));
        setIsMounted(true);
    }

    useEffect(() => {
        fetchEvents();
    }, []);

    async function getEvent(click: EventClickArg) {
        const response = await eventViewApi.getEvent(click.event.title);

        if(response!=null){
            setSelectedEvent(response);
        }
    }


    return(
        <div>
            <EventList events={events}
                       selectedItem={getEvent}
                       eventSelected={() => setIsJoinEventDialogOpen(true)}/>
            <JoinEvent handleClose={() => {fetchEvents();setIsJoinEventDialogOpen(false);}}
                       isJoinEventOpen={isJoinEventDialogOpen}
                       selectedEvent={selectedEvent}
                       qrCodeShow={() => setIsQRViewerOpen(true)}/>
            <QRCodeShow isOpen={isQRViewerOpen}
                        selectedEvent={selectedEvent}
                        handleClose={() => setIsQRViewerOpen(false)}/>
        </div>
    );
}

export default EventView;
