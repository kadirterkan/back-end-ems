import {
    Button,
    CircularProgress,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Typography
} from "@material-ui/core";
import {useState} from "react";
import {EventQueryResponse} from "../../moderator/api/ModApi";
import {EventViewApi} from "../api/EventViewApi";
import {toast} from "react-toastify";
import {EventModel} from "../EventView";

interface Props {
    handleClose:() => void;
    isJoinEventOpen:boolean;
    selectedEvent:EventQueryResponse;
    qrCodeShow:() => void;
}

const initialstate: EventModel = {
    quota:0,
    eventName:"",
    startTime:new Date(),
    endTime:new Date(),
}

export function JoinEvent(props:Props){

    const api = new EventViewApi();
    const [loading,setLoading] = useState(false);

    const joinEvent = async () => {
        setLoading(true);
        let name = props.selectedEvent.title;
        const response = await api.addEventToUser(name);

        if(response!=null){
            if(response.messageType === "SUCCESS"){
                toast.success(response.message);
                props.handleClose();
                props.qrCodeShow();
            }else {
                toast.error(response.message);
            }
        }
        setLoading(false);
    }


    return(<div>
            <Dialog
             open={props.isJoinEventOpen}
             onClose={props.handleClose}>
                <DialogTitle>Event Details</DialogTitle>
                <DialogContent>
                    <Typography>Event Name = {props.selectedEvent.title}</Typography>
                    <Typography>Event Start Date ={props.selectedEvent.start.toString()}</Typography>
                    <Typography>Event End Date = {props.selectedEvent.end.toString()}</Typography>
                    <Typography>Event Quota = {props.selectedEvent.quota}</Typography>
                    <Typography>People Attending to this Event = {props.selectedEvent.attending}</Typography>
                </DialogContent>
                <DialogActions>
                    <Button disabled={loading} onClick={props.handleClose}>Cancel</Button>
                    <Button disabled={loading} onClick={joinEvent}>Join</Button>
                    {loading && <CircularProgress size={24} />}
                </DialogActions>


            </Dialog>
        </div>

    );
}