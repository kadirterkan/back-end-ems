import FullCalendar, {EventMountArg} from "@fullcalendar/react";
import {useState} from "react";
import {EventQueryResponse} from "../../moderator/api/ModApi";
import interactionPlugin from "@fullcalendar/interaction";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import momentTimezonePlugin from "@fullcalendar/moment-timezone";
import listPlugin from "@fullcalendar/list";
import {EventClickArg} from "@fullcalendar/core";
import tippy from "tippy.js";

interface Props {
    joinedEvents:EventQueryResponse[];
    selectedEvent:(clicked:EventClickArg) => void;
    eventSelected:() => void;
}




export function JoinedEventsList(props:Props) {

    const [loading,setLoading] = useState(false);

    const eventDidMountHandler = (selected:EventMountArg) => {

        let event = props.joinedEvents.find((eventId) => eventId.id ==selected.event.id);

        if (event!=undefined) {
            let content:string = event.title+ "\n" + " quota: " + event.quota.toString() + " people attending: " + event.attending.toString();

            tippy(selected.el, {
                content: content
            });
        }
    }



    return(<div>
            <FullCalendar
            events={props.joinedEvents}
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
            editable={false}
            selectable={true}
            eventClick={(value:EventClickArg) => {props.eventSelected();props.selectedEvent(value)}}
            />
        </div>

    );
}