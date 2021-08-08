import React, {ChangeEvent, useEffect, useState} from "react";
import {EventQueryResponse, ModApi, UpdateEventRequest} from "../api/ModApi";
import {MessageResponse, MessageType} from "../../common/dto/MessageResponse";
import {toast} from "react-toastify";
import FullCalendar, {DateSelectArg, EventChangeArg} from "@fullcalendar/react";
import {Button, Dialog, DialogActions, DialogContent, DialogTitle, Slide, TextField} from "@material-ui/core";
import moment from 'moment';
import {TransitionProps} from "@material-ui/core/transitions";
import allLocales from "@fullcalendar/core/locales-all";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin, {EventDragStopArg} from '@fullcalendar/interaction';
import {EventClickArg} from "@fullcalendar/core";
import DateFnsUtils from "@date-io/date-fns";
import {DateTimePicker, MuiPickersUtilsProvider} from "@material-ui/pickers";
import {MaterialUiPickersDate} from "@material-ui/pickers/typings/date";
import momentTimezonePlugin from '@fullcalendar/moment-timezone';



const Transition = React.forwardRef(function Transition(
    props: TransitionProps & { children?: React.ReactElement },
    ref: React.Ref<unknown>,
) {
    return <Slide direction="up" ref={ref} {...props} />;
});

export interface EventRequest{
    quota:number;
    eventName:string;
    startTime:Date;
    endTime:Date;
    oldEventName?:string;
}

const initialstate: EventRequest = {
    quota:0,
    eventName:"",
    startTime:new Date(),
    endTime:new Date(),
};

export interface EventModel {
    quota:number;
    eventName:string;
    startTime:Date;
    endTime:Date;
}


export function ModEventList(){


    // const [isMounted,setIsMounted] = useState(false);
    const [isAddEventModelOpen,setIsAddEventModelOpen] = useState(false);
    const [eventQuery,setEventQuery] = useState<EventQueryResponse[]>([]);
    const [eventRequest,setEventRequest] = useState<EventRequest>(initialstate);
    const [isEditEventModelOpen,setIsEditEventModelOpen] = useState(false);
    const modApi = new ModApi();



    async function fetchEvents() {

        // setIsMounted(false);


        const response = await modApi.getEvents();

        if (response!=null) {
            setEventQuery(response);
        }

        // setIsMounted(true);

    }

    const getEvents = async () => {
        const response = await modApi.getEvents();

        return response;
    }

    const editEvent = async () => {

        if(eventRequest.oldEventName!=undefined){
            let updateEventRequest: UpdateEventRequest = {eventName:eventRequest.eventName,
                startTime:eventRequest.startTime,
                endTime:eventRequest.endTime,
                quota:eventRequest.quota,
                oldEventName:eventRequest.oldEventName}

            console.log("TEST TEST" + updateEventRequest.startTime.toString());

            const messageResponse = await modApi.eventEdit(updateEventRequest);

            await response(messageResponse);

        }
    }

    const deleteEvent = async () => {
        const messageResponse = await modApi.deleteEvent(eventRequest);

        await response(messageResponse);

    }

    const addEvent = async (event:EventRequest) => {
        let eventModel : EventModel = {eventName:event.eventName,
                                        quota: event.quota,
                                        startTime:event.startTime,
                                        endTime:event.endTime}

        const messageResponse = await modApi.eventAdder(eventModel);

        await response(messageResponse);
    }

    const addFromCalender = (selected:DateSelectArg) => {
        let newEventModel = {...eventRequest};

        if ("start" in selected && "end" in selected) {
            newEventModel.startTime = selected.start;
            newEventModel.endTime = selected.end;
        }
        console.log("FROM CALENDER" + selected.start.toString());
        setEventRequest(newEventModel);

        setIsAddEventModelOpen(true);
    }

    const editFromCalender = async (selected: EventChangeArg) => {
        let newEventModel = {...eventRequest};

        const responseModel = await modApi.getEvent(selected.event.title);

        if (responseModel!=null) {
            newEventModel.eventName = responseModel.title;
            newEventModel.oldEventName = responseModel.title;
            newEventModel.quota = responseModel.quota;

            if (selected.event.start != null && selected.event.end != null) {
                console.log("IN FUNCTION" + selected.event.start);
                console.log("IN FUNCTION" + selected.event.end);

                newEventModel.startTime = selected.event.start;
                newEventModel.endTime = selected.event.end;


                setEventRequest(newEventModel);

                console.log("TEST2   " + newEventModel.startTime.toString());


                setIsEditEventModelOpen(true);
            }
        } else {
        }
    }

    // const handleOldValue = async (oldValue:EventDragStopArg) => {
    //     let newEventModel = {...eventRequest};
    //
    //
    //     console.log("TEST1   "+ newEventModel.startTime.toString());
    //
    //     setEventRequest(newEventModel);
    // }

    const eventStartDateHandler = (date: moment.Moment | string) => {
        let newSetModel = {...eventRequest};

        if(typeof date == "string"){
            console.log("string" + date);

            newSetModel.startTime = new Date(date);

        }else{
            console.log(date.toString());

            newSetModel.startTime = date.toDate();
        }

        setEventRequest(newSetModel);
    }


    const eventEndDateHandler = (date: moment.Moment | string) => {
        let newSetModel = {...eventRequest};

        if(typeof date == "string"){
            newSetModel.endTime = new Date(date);

        }else{
            newSetModel.endTime = date.toDate();
        }

        setEventRequest(newSetModel);

    }

    // const handleCancel = () => {
    //     setIsEditEventModelOpen(false);
    //     if(selected != null){
    //         selected.revert();
    //     }
    // }


    const eventHandler = (event:  ChangeEvent<HTMLInputElement>) => {
        let newSetModel = {...eventRequest};

        switch(event.target.name){
            case "eventName":
                newSetModel.eventName = event.target.value;
                break;
            case "quota":
                let val : number = +event.target.value;
                newSetModel.quota = val;
                break;
            case "startTime":
                newSetModel.startTime = new Date(event.target.value);
                break;
            case "endTime":
                newSetModel.endTime = new Date(event.target.value);
                break;
        }

        setEventRequest(newSetModel);
    }

    const addEventFromCalender = (newEventModel:EventRequest) => {
        let isAddEventModelOpen = true;

    }

    const response = async(messageResponse:MessageResponse) => {

        if(messageResponse.messageType===MessageType.SUCCESS){
            if(isAddEventModelOpen) {
                setIsAddEventModelOpen(false);
            }
            if(isEditEventModelOpen){
                setIsEditEventModelOpen(false);
            }
            toast.success(messageResponse.message);
        }else{
            toast.error(messageResponse.message);
        }

        await fetchEvents();
    }

    const handleSelect = async (selected:EventClickArg) => {
        let newEventModel = {...eventRequest};

        const responseModel = await modApi.getEvent(selected.event.title);

        if (responseModel!=null) {
            newEventModel.eventName = responseModel.title;
            newEventModel.oldEventName = responseModel.title;
            newEventModel.quota = responseModel.quota;

            if (selected.event.start != null && selected.event.end != null) {

                newEventModel.startTime = selected.event.start;
                newEventModel.endTime = selected.event.end;


                setEventRequest(newEventModel);


                setIsEditEventModelOpen(true);
            }
        }
    }

    const onStartTimeChange = (value:MaterialUiPickersDate) => {
        let newEventModel = {...eventRequest};

        if(value!=null){
            newEventModel.startTime = value;
            setEventRequest(newEventModel);
        }
    }

    const onEndTimeChange = (value:MaterialUiPickersDate) => {
        let newEventModel = {...eventRequest};

        if(value!=null){
            newEventModel.endTime = value;
            setEventRequest(newEventModel);
        }
    }

    useEffect(() => {
        fetchEvents();
    },[]);

    return(
        <div >
            <Dialog
                open={isAddEventModelOpen}
                TransitionComponent={Transition}
            >

                <DialogTitle >Add Event</DialogTitle>
                <DialogContent >
                    <TextField
                        onChange={eventHandler}
                        label={"Event Name"}
                        className={"text-field"}
                        inputProps={{
                            name:"eventName",
                        }}
                    />
                    <MuiPickersUtilsProvider utils={DateFnsUtils}>
                        <DateTimePicker label={"Start Time"}
                                        name={"startTime"}
                                        value={eventRequest.startTime}
                                        onChange={onStartTimeChange}
                                        format="yyyy/MM/dd HH:mm"/>
                        <DateTimePicker label={"End Time"}
                                        name={"endTime"}
                                        value={eventRequest.endTime}
                                        onChange={onEndTimeChange}
                                        format="yyyy/MM/dd HH:mm"/>
                    </MuiPickersUtilsProvider>
                    <TextField
                        onChange={eventHandler}
                        label={"Event Quota"}
                        name={"quota"}
                        className={"text-field"}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setIsAddEventModelOpen(false)}>Cancel</Button>
                    <Button onClick={() => addEvent(eventRequest)}>Submit</Button>
                </DialogActions>
            </Dialog>
            <Dialog
                open={isEditEventModelOpen}
                TransitionComponent={Transition}
            >

                <DialogTitle >Edit Event</DialogTitle>
                <DialogContent >
                    <TextField
                        onChange={eventHandler}
                        label={"Event Name"}
                        className={"text-field"}
                        value={eventRequest.eventName}
                        name={"eventName"}
                    />
                    <MuiPickersUtilsProvider utils={DateFnsUtils}>
                        <DateTimePicker label={"Start Time"}
                                        name={"startTime"}
                                        value={eventRequest.startTime}
                                        onChange={onStartTimeChange}
                                        format="yyyy/MM/dd HH:mm"/>
                        <DateTimePicker label={"End Time"}
                                        name={"endTime"}
                                        value={eventRequest.endTime}
                                        onChange={onEndTimeChange}
                                        format="yyyy/MM/dd HH:mm"/>
                    </MuiPickersUtilsProvider>
                    <TextField
                        onChange={eventHandler}
                        label={"Event Quota"}
                        value={eventRequest.quota}
                        name={"quota"}
                        type={'number'}
                        className={"text-field"}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => deleteEvent()}>DELETE</Button>
                    <Button onClick={() => setIsEditEventModelOpen(false)}>Cancel</Button>
                    <Button onClick={() => editEvent()}>Submit</Button>
                </DialogActions>
            </Dialog>
            <FullCalendar
                headerToolbar={{
                    left: 'prev,next today',
                    center: 'title',
                    right: 'timeGridMonth,timeGridWeek,timeGridDay'
                }}
                dayMaxEventRows={10}
                validRange={
                    {start:new Date()}
                }
                droppable={true}
                select={addFromCalender}
                eventResizableFromStart={true}
                eventDurationEditable={true}
                plugins={[interactionPlugin,dayGridPlugin,timeGridPlugin,momentTimezonePlugin]}
                eventClick={handleSelect}
                selectable={true}
                editable={true}
                events={eventQuery}
                locales={allLocales}
                locale={"tr"}
                firstDay={1}
                eventChange={editFromCalender}
                initialView="dayGridMonth"
                dragScroll={true}
                eventResize={(sds) => console.log(sds)}
                eventResizeStart={(sd) => console.log(sd)}
            />
        </div>
    );


}