import {JoinedEventsList} from "./joinedEventsList/JoinedEventsList";
import {JoinedEventsDetails} from "./joinedEventsDetails/JoinedEventsDetails";
import {useEffect, useState} from "react";
import {EventQueryResponse} from "../moderator/api/ModApi";
import {API} from "./api/API";
import {EventClickArg} from "@fullcalendar/core";


export function JoinedEvents() {


    const api = new API();
    const [loading,setLoading] = useState(false);
    const [joinedEvents,setJoinedEvents] = useState<EventQueryResponse[]>([]);
    const [isJoinedEventsDetailsOpen,setIsJoinedEventsDetailsOpen] = useState(false);
    const [selectedEvent,setSelectedEvent] = useState<EventQueryResponse>({id:"0",
        title:"",
        start:new Date(),
        end:new Date(),
        quota:0,
        attending:0});


    const fetchJoinedEvents = async () => {
        setLoading(true);

        const response = await api.joinedEvents();

        if(response!=null){
            setJoinedEvents(response);
        }

        setLoading(false);

    }

    async function getEvent(click: EventClickArg) {
        setLoading(false);
        const response = await api.getEvent(click.event.title);

        if(response!=null){
            setSelectedEvent(response);
        }
        setLoading(true);
    }

    useEffect(() => {
            fetchJoinedEvents();
    },[]);

    return(<div>
            <JoinedEventsList
                joinedEvents={joinedEvents}
                eventSelected={() => setIsJoinedEventsDetailsOpen(true)}
                selectedEvent={(click) => getEvent(click)}/>
            <JoinedEventsDetails
                isOpen={isJoinedEventsDetailsOpen}
                handleClose={() => {fetchJoinedEvents();setIsJoinedEventsDetailsOpen(false);}}
                selectedEvent={selectedEvent}/>
        </div>
    );
    
}

export default JoinedEvents;