import React, {ChangeEvent, useState} from "react";
import {Button, Dialog, DialogActions, DialogContent, DialogTitle, TextField} from "@material-ui/core";
import {ModApi} from "../api/ModApi";
import {MessageType} from "../../common/dto/MessageResponse";
import {toast} from "react-toastify";
import {EventModel} from "../EventAdder/EventAdder";
import {GridRowParams} from "@material-ui/data-grid";

interface Props {
    isOpen: boolean;
    handleClose: () => void,
    addEvent: (model:EventModel) => void
    oldEventModel:EventModel;
}

export function EventEdit(props: Props){

    const modApi = new ModApi();


    const [eventModel,setEventModel] = useState<EventModel>(() => props.oldEventModel);

    const eventEdit = async (model: EventModel) => {
        const messageResponse = await modApi.eventEdit(model);
        if(messageResponse.messageType === MessageType.SUCCESS){
            toast("SUCCESSFULLY EDITED EVENT");
        }
        else{
            toast("AN ERROR OCCURRED DURING THE PROCESS")
        }
    }


    const nameHandler = (event:ChangeEvent<HTMLInputElement>) =>{
        let newSetModel = {...eventModel};

        newSetModel.eventName = event.target.value;

        setEventModel(newSetModel);
    }

    const quotaHandler = (event:ChangeEvent<HTMLInputElement>) =>{
        let newSetModel = {...eventModel}

        newSetModel.quota = +event.target.value;

        setEventModel(newSetModel);
    }

    const timeHandler = (event: ChangeEvent<HTMLInputElement>) => {
        let newSetModel = {...eventModel};

        let date = new Date(event.target.value);

        switch(event.target.name){
            case "startTime":
                newSetModel.startTime = date;
                break;
            case "endTime":
                newSetModel.endTime = date;
                break;
        }

        setEventModel(newSetModel);
    }


    return(
        <Dialog
            open={props.isOpen}
            onClose={props.handleClose}>
            <DialogTitle>Add Event</DialogTitle>
            <DialogContent>
                <TextField
                    onChange={nameHandler}
                    label={"Event Name"}
                    value={eventModel.eventName}
                    inputProps={{
                        name:"eventName",
                    }}
                />
                <TextField
                    onChange={timeHandler}
                    label="Event-Start-Time"
                    type="datetime-local"
                    value={eventModel.startTime}
                    inputProps={{
                        name:'startTime',
                    }}
                /><TextField
                onChange={timeHandler}
                label={"Event-End-Time"}
                name={"endTime"}
                type={"datetime-local"}
                value={eventModel.endTime}
            />
                <TextField
                    onChange={quotaHandler}
                    label={"Event Quota"}
                    name={"quota"}
                    value={eventModel.quota}
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={props.handleClose}>Cancel</Button>
                <Button onClick={() => eventEdit(eventModel)}>Submit</Button>
            </DialogActions>
        </Dialog>
    );
}