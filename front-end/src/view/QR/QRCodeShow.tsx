import {EventModel} from "../EventView";
import {Button, Dialog, DialogActions, DialogContent, DialogTitle} from "@material-ui/core";
import QRcode from 'qrcode.react'
import React, {useState} from "react";

interface Props {
    isOpen: boolean;
    handleClose: () => void,
    model: EventModel
}


export function QRCodeShow(props: Props){

    const [image,SetImage] = useState();

    const modelToString = (model:EventModel) => {

        let newValue = "EVENT NAME \n";
        newValue+=model.eventName;
        newValue+="EVENT START DATE \n";
        newValue+=model.startTime.toString();
        newValue+="EVENT END DATE \n";
        newValue+=model.endTime.toString();
        newValue+="EVENT QUOTA";
        newValue+=model.quota.toString();

        return newValue;
    }

    return(
        <Dialog
            open={props.isOpen}
            onClose={props.handleClose}>
            <DialogTitle>QR CODE</DialogTitle>
            <DialogContent>
                <QRcode value={modelToString(props.model)}
                        id="qr"
                        size={320}
                        includeMargin={true}/>
            </DialogContent>
            <DialogActions>
                <Button onClick={props.handleClose}>Close</Button>
            </DialogActions>
        </Dialog>
    );
}