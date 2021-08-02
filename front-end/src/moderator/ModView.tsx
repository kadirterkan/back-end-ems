import {useEffect, useState} from "react";
import {EventQueryResponse, ModApi} from "./api/ModApi";
import {EventAdder, EventModel} from "./EventAdder/EventAdder";
import {MessageType} from "../common/dto/MessageResponse";
import {toast} from "react-toastify";
import {Button} from "@material-ui/core";
import {ModEventList} from "./EventList/ModEventList";
import {GridRowParams} from "@material-ui/data-grid";

const initialstate: EventModel = {
    quota:0,
    eventName:"",
    startTime:new Date(),
    endTime:new Date(),
}


export function ModView(){
    const [isAddEventModelOpen, setAddEventModelOpen] = useState(false);
    const [eventQueryResponses, setEventQueryResponses] = useState<EventQueryResponse[]>([]);

    const [selectedItem,setSelectedItem] = useState(false);

    const modApi = new ModApi();

    const [selected,setSelected] = useState<EventModel>(initialstate);



    function fetchEvents(){
        modApi.getEvents()
            .then(data => setEventQueryResponses(data));
    }

    useEffect(() => {
        fetchEvents();
    }, []);

    const addEvent = async (model: EventModel) => {
        const messageResponse = await modApi.eventAdder(model);

        if(messageResponse.messageType === MessageType.SUCCESS){
            toast.success(messageResponse.message);
            setAddEventModelOpen(false);
            fetchEvents();
        }
        else {
            toast.error(messageResponse.message);
        }
    }

    const whenSelected = (value: GridRowParams) => {
        if(selectedItem){
            let newEventModel= {...selected};

            newEventModel.eventName = value.getValue(value.id, "eventName") as string;
            newEventModel.quota = value.getValue(value.id, "quota") as number;
            newEventModel.startTime = value.getValue(value.id,"startTime") as Date;
            newEventModel.endTime = value.getValue(value.id, "endTime") as Date;

            setSelected(newEventModel);
        }
    }

    return(
        <div>
            <Button color={"primary"} onClick={() => setAddEventModelOpen(true)}>Add Event</Button>
            <EventAdder isOpen={isAddEventModelOpen}
                        handleClose={() => setAddEventModelOpen(false)}
                        addEvent={addEvent}
                        />
            <ModEventList events={eventQueryResponses} selected={(value) => whenSelected(value)} selectedItem={(value) => setSelectedItem(value)}/>
        </div>
    );
}