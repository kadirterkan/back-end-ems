import React, {ChangeEvent, useState} from 'react';
import classes from "*.module.css";
import {Button, TextField} from "@material-ui/core";

interface Props{
    Login: (model: UserModel) => void
}

export interface UserModel{
    username: string;
    password: string;
}

const initialState: UserModel = {
    username: "",
    password: ""
};


export function Login(){

    const [userModel, setUserModel] = useState(initialState);

    const onFormChange = (event: ChangeEvent<HTMLInputElement>) => {
        const field = event.target.name;
        const value = event.target.value;

        setUserModel(updateFormState(field,value));
    }

    function updateFormState(field: string, value: string){
        const newModelState = {...userModel};
        switch(field){
            case "username":
                userModel.username = value;
                break;
            case "password":
                userModel.password = value;
                break;
        }
        return newModelState;
    }

    return (
        <form className={"entity"} noValidate autoComplete="off">
            <div><TextField onChange={onFormChange} name="username" label="Username" /></div>
            <div><TextField onChange={onFormChange} name="password" label="Password"/></div>
            <div><Button variant="contained">Login</Button> </div>
        </form>
    );
}

export default Login;