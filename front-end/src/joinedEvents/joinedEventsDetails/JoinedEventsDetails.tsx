import {
    Button,
    CircularProgress,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Typography
} from "@material-ui/core";
import {EventQueryResponse} from "../../moderator/api/ModApi";
import {useState} from "react";
import {API} from "../api/API";
import {MessageType} from "../../common/dto/MessageResponse";
import {toast} from "react-toastify";

interface Props {
    isOpen:boolean;
    handleClose:() => void;
    selectedEvent:EventQueryResponse;
}


export function JoinedEventsDetails(props:Props) {

    const api = new API();

    const [loading,setLoading]=useState(false);

    const leaveEvent = async () => {
        setLoading(true);

        const response = await api.leaveEvent(+props.selectedEvent.id);

        if(response!=null){
            if(response.messageType === MessageType.SUCCESS){
                toast.success(response.message);
                props.handleClose();
            }else{
                toast.error(response.message);
            }
        }

        setLoading(false);

    }

    return(
        <Dialog
            open={props.isOpen}
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
                <Button disabled={loading} onClick={leaveEvent}>Leave Event</Button>
                {loading && <CircularProgress size={24} />}
            </DialogActions>
        </Dialog>
    )
}