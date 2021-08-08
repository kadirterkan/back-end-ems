import {Button, Dialog, DialogActions, DialogContent, DialogTitle} from "@material-ui/core";
import React, {useEffect, useState} from "react";
import {EventQueryResponse} from "../../moderator/api/ModApi";
import {EventViewApi} from "../api/EventViewApi";

interface Props {
    isOpen: boolean;
    handleClose: () => void;
    selectedEvent:EventQueryResponse;
}


export function QRCodeShow(props: Props){

    const api = new EventViewApi();

    const [loading,setLoading] = useState(false);
    const [image,setImage] = useState<string>("");

    const bringQrCode = async () => {
        setLoading(false);
        const response = await api.bringQRCodeData(props.selectedEvent.title);
        if(response!=null){
            setImage(response);
        }
        setLoading(true);
    }

    useEffect(() => {
        if(props.isOpen){
            console.log("test");
            bringQrCode();
        }
    },[props.isOpen])


    return(
        <Dialog
            open={props.isOpen}
            onClose={props.handleClose}>
            <DialogTitle>QR CODE</DialogTitle>
            <DialogContent>
                {loading && <img src={`data:image/jpeg;base64,${image}`} />}
            </DialogContent>
            <DialogActions>
                <Button onClick={props.handleClose}>Close</Button>
            </DialogActions>
        </Dialog>
    );
}