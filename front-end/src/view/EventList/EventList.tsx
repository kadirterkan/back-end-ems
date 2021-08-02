import {EventQueryResponse, ModApi} from "../../moderator/api/ModApi";
import {DataGrid, GridRowParams} from "@material-ui/data-grid";
import {useState} from "react";

const tableColumns = [
    {field: "id", headerName: "ID"},
    {field: "eventName", headerName: "Event Name"},
    {field: "startTime", headerName: "Event Start Time"},
    {field: "endTime", headerName: "Event Ending Time"},
    {field: "quota", headerName: "Quota"},
    {field: "attending",headerName: "People Attending To Event"}
];

interface Props {
    events:EventQueryResponse[];
    selectedItem:(value:boolean) => void;
    selected:(value:GridRowParams) => void;
}

export function EventList(props:Props){
    const modApi = new ModApi();

    const [selected,setSelected] = useState(false);

    const handleRowSelection = (selection:GridRowParams) => {
        setSelected(true);
        props.selectedItem(selected);

        props.selected(selection);
    }

    const handleRowOut = () => {
        if(selected){
            setSelected(false);
            props.selectedItem(selected);
        }
    }


    return(
        <div style={{height: 400, width: '100%'}}>
            <DataGrid
                columns={tableColumns}
                rows={props.events}
                onRowOut={()=>handleRowOut()}
                onRowClick={(params)=> handleRowSelection(params)}
                pageSize={10}/>
        </div>
 );
}

export default EventList;