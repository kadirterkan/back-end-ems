import {EventModel} from "../EventView";
import {Dialog, DialogContent, DialogTitle} from "@material-ui/core";
import {QRCodeWriter} from "@zxing/library";
import QRcode from 'qrcode.react'
import {useState} from "react";

interface Props {
    isOpen: boolean;
    handleClose: () => void,
    model: EventModel
}


export function QRCodeShow(props: Props){

    const modelToString = (model:EventModel) => {

        let newValue = "EVENT NAME \n";
        newValue+=model.eventName;
        newValue+="EVENT START DATE \n";
        newValue+=model.startTime.toDateString();
        newValue+="EVENT END DATE \n";
        newValue+=model.endTime.toDateString();
        newValue+="EVENT QUOTA";
        newValue+=model.quota.toString();

        return newValue;
    }

    return(
        <Dialog open={props.isOpen}
        onClose={props.handleClose}>
            <DialogTitle>QR CODE</DialogTitle>
            <DialogContent>
                <QRcode value={modelToString(props.model)}
                        id="myqr"
                        size={320}
                        includeMargin={true}/>
            </DialogContent>

        </Dialog>

    );
}