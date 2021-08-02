import {Button, FormControl, FormHelperText, InputLabel, NativeSelect, TextField} from "@material-ui/core";
import { useState} from "react";
import {MessageType} from "../../common/dto/MessageResponse";
import {toast} from "react-toastify";
import {UserAuthorityAdder} from "./api/UserAuthorityAddApi";


export interface AuthorityAdd{
    username:string;
    authorityName:string;
}

const initialstate: AuthorityAdd = {
    username:"",
    authorityName:""
};


export function UserAuthorityAdd(){

    const userAuthorityAdder = new UserAuthorityAdder();

    const [model,setModel]=useState<AuthorityAdd>(initialstate);

    const userAuthority = async (model: AuthorityAdd) => {
        const messageResponse = await userAuthorityAdder.AddAuthorityToUser(model);
        if(messageResponse.messageType === MessageType.SUCCESS){
            toast.success("SUCCESFULLY LOGGED IN");
        } else {
            toast.error(messageResponse.message);
        }
    }


    const handleSelect = (event: React.ChangeEvent<{ value: string }>) => {
        let newSetModel = {...model};

        newSetModel.authorityName = event.target.value;

        setModel(newSetModel);
    };

    const handleText = (event: React.ChangeEvent<{value: string}>) => {
        let newSetModel = {...model};

        newSetModel.username = event.target.value;

        setModel(newSetModel);
    }




    return(
        <div>
            <form className={"entry-label"}>
                <div>
                    <TextField onChange={handleText} name={"username"} label={"Username"}></TextField>
                </div>
            </form>
            <FormControl className={"form-control"}>
                <InputLabel >
                    Authorities
                </InputLabel>
                <NativeSelect
                    name={"authority"}
                    onChange={handleSelect}
                >
                    <option value="">None</option>
                    <option value={"ADMIN"}>Administrator</option>
                    <option value={"MOD"}>Moderator</option>
                </NativeSelect>
                <FormHelperText>Choose the Authority you want to add for this user.</FormHelperText>
            </FormControl>
            <div><Button onClick={()=>userAuthority(model)}>Submit</Button></div>
        </div>
    )
}