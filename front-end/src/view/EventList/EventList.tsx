import {EventQueryResponse, ModApi} from "../../moderator/api/ModApi";
import {Component, useEffect, useState} from "react";
import FullCalendar, {DateSelectArg, EventMountArg} from "@fullcalendar/react";
import {EventClickArg} from "@fullcalendar/core";
import interactionPlugin from "@fullcalendar/interaction";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import momentTimezonePlugin from "@fullcalendar/moment-timezone";
import listPlugin from '@fullcalendar/list';
import React from "react";
import tippy from 'tippy.js';
import 'tippy.js/dist/tippy.css'; // optional for styling



const tableColumns = [
    {field: "id", headerName: "ID"},
    {field: "eventName", headerName: "Event Name"},
    {field: "startTime", headerName: "Event Start Time"},
    {field: "endTime", headerName: "Event Ending Time"},
    {field: "quota", headerName: "Quota"},
    {field: "attending",headerName: "People Attending To Event"}
];

interface Props {
    events:EventQueryResponse[];
    selectedItem:(value:EventClickArg) => void;
    eventSelected:() => void;
}

export function EventList(props:Props){
    const modApi = new ModApi();

    const [loading,setLoading] = useState(false);
    const [eventQuery,setEventQuery] = useState<EventQueryResponse[]>([]);


    async function fetchEvents() {

        setLoading(false);


        const response = await modApi.getEvents();

        if (response!=null) {
            setEventQuery(response);
        }

        setLoading(true);

    }

    const eventDidMountHandler = (selected:EventMountArg) => {

        let event = eventQuery.find((eventId) => eventId.id ==selected.event.id);

        if (event!=undefined) {
            let content:string = event.title+ "\n" + " quota: " + event.quota.toString() + " people attending: " + event.attending.toString();

            tippy(selected.el, {
                content: content
            });
        }
    }




    useEffect(() => {
        fetchEvents();
    },[]);


    return(
        <div style={{height: 400, width: '100%'}}>
            <FullCalendar
                headerToolbar={{
                    left: 'prev,next today',
                    center: 'title',
                    right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
                }}
                droppable={false}
                eventDidMount={eventDidMountHandler}
                plugins={[interactionPlugin,dayGridPlugin,timeGridPlugin,momentTimezonePlugin,listPlugin]}
                validRange={
                    {start:new Date()}
                }
                initialView="dayGridMonth"
                events={props.events}
                editable={false}
                selectable={true}
                eventClick={(value:EventClickArg) => {props.eventSelected();props.selectedItem(value)}}
            />

        </div>
 );
}

export default EventList;