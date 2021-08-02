import React, {useState} from "react";
import {Button, Dialog, DialogActions, DialogContent, DialogTitle, TextField} from "@material-ui/core";
import {ModApi} from "../api/ModApi";
import {MessageType} from "../../common/dto/MessageResponse";
import {toast} from "react-toastify";

interface Props {
    isOpen: boolean;
    handleClose: () => void,
    addEvent: (model:EventModel) => void
}

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
};



export function EventAdder(props: Props){

    const eventAdderApi = new ModApi();

    const [eventModel,setEventModel] = useState<EventModel>(initialstate);

    const eventAdder = async (model: EventModel) => {
        const messageResponse = await eventAdderApi.eventAdder(model);
        if(messageResponse.messageType === MessageType.SUCCESS){
            toast("SUCCESSFULLY ADDED EVENT");
        }
        else{
            toast("AN ERROR OCCURRED DURING THE PROCESS")
        }
    }



    const nameHandler = (event:React.ChangeEvent<{value: string}>) =>{
        let newSetModel = {...eventModel};

        newSetModel.eventName = event.target.value;

        setEventModel(newSetModel);
    }

    const quotaHandler = (event:React.ChangeEvent<{value: string}>) =>{
        let newSetModel = {...eventModel}

        newSetModel.quota = +event.target.value;

        setEventModel(newSetModel);
    }

    const timeHandler = (event: React.ChangeEvent<{name: string;value: string}>) => {
        let newSetModel = {...eventModel};


        let date = new Date(event.target.value);

        console.log(date);

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
                className={"text-field"}
                inputProps={{
                    name:"eventName",
                }}
                />
            <TextField
                onChange={timeHandler}
                // label="Event-Start-Time"
                type="datetime-local"
                className={"text-field"}
                inputProps={{
                    name:'startTime',
                }}
            /><TextField
                onChange={timeHandler}
                // label={"Start-Time"}
                name={"endTime"}
                type={"datetime-local"}
                className={"text-field"}
            />
                <TextField
                    onChange={quotaHandler}
                    label={"Event Quota"}
                    name={"quota"}
                    className={"text-field"}
                    />
            </DialogContent>
            <DialogActions>
                <Button onClick={props.handleClose}>Cancel</Button>
                <Button onClick={() => eventAdder(eventModel)}>Submit</Button>
            </DialogActions>
        </Dialog>
    );
}