import {Button, Dialog, DialogActions, DialogContent} from "@material-ui/core";
import {UserQueryResponse} from "../api/ModApi";
import {List} from "./List/List";

interface Props {
    isOpen:boolean;
    handleClose:() => void;
    users:UserQueryResponse[];
    eventName:string;
}


export function EventJoinedUsersList(props:Props) {

    return(
        <Dialog
            open={props.isOpen}
            onClose={props.handleClose}
        >
            <DialogContent>
                <List eventName={props.eventName} users={props.users}/>
            </DialogContent>
            <DialogActions>
                <Button onClick={props.handleClose}>Close</Button>
            </DialogActions>
        </Dialog>
    );
}