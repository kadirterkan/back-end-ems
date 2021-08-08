import {UserQueryResponse} from "../../api/ModApi";
import MaterialTable from "material-table";

interface Props {
    eventName:string;
    users:UserQueryResponse[];
}

const columns= [
    {title:'First Name',field:'firstName'},
    {title:'Last Name',field: 'lastName'},
    {title:'TC Kimlik Number',field: 'tcKimlikNumber'}
        ];


export function List(props:Props) {
    return(
      <div>
          <MaterialTable
              title={"USERS THAT HAVE JOINED THIS EVENT " + props.eventName}
              columns={columns}
              data={props.users}
              options={{sorting:true}}
          />
      </div>
    );
}