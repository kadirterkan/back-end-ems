import React, {ChangeEvent, useState} from 'react';
import {Button, TextField} from "@material-ui/core";
import {UserApi} from "./api/LoginAPI";
import {MessageType} from "../common/dto/MessageResponse";
import {toast} from "react-toastify";
import Cookies from "js-cookie";


export interface UserLoginModel{
    username: string;
    password: string;
}

const initialState: UserLoginModel = {
    username: "",
    password: ""
};


export function Login(){

    let isValid = true;

    const userApi = new UserApi();

    const [userLoginModel, setUserLoginModel] = useState<UserLoginModel>(initialState);

    const userLogin = async (model: UserLoginModel) => {
        const messageResponse = await userApi.Login(model);
        console.log(messageResponse.messageType);
        if(messageResponse.messageType === MessageType.SUCCESS){
            isValid=true;
            toast.success("SUCCESFULLY LOGGED IN");
            Cookies.set("Authentication",messageResponse.message);
        } else {
            isValid=false;
            toast.error(messageResponse.message);
        }
    }

    const onFormChange = (event: ChangeEvent<HTMLInputElement>) => {
        const field = event.target.name;
        const value = event.target.value;

        setUserLoginModel(updateFormState(field,value));
    }

    function updateFormState(field: string, value: string){
        const newModelState = {...userLoginModel};
        switch(field){
            case "username":
                console.log(value);
                userLoginModel.username = value;
                break;
            case "password":
                console.log(value);
                userLoginModel.password = value;
                break;
        }
        return newModelState;
    }

    let UsernameField = () => {
        return(
            <div>
                <TextField
                    onChange={onFormChange}
                    name="username"
                    label="Username"
                />
            </div>
        )
    }

    const ErrorUsernameField = () => {
        return(
            <div>
                <TextField
                    onChange={onFormChange}
                    name={"username"}
                    error
                    label="Username"
                    helperText="Incorrect Username or Password"
                    variant="filled"
                />
            </div>
        )
    }

    let PasswordField = () => {
        return(
            <div>
                <TextField onChange={onFormChange} name="password" label="Password"/>
            </div>
        )
    }

    const ErrorPasswordField = () => {
        return(
            <div>
                <TextField
                    onChange={onFormChange}
                    name={"password"}
                    error
                    label={"Password"}
                    value={"password"}
                    helperText={"Incorrect Username or Password"}
                    variant={"filled"}
                />
            </div>
        )
    }

    const LoginButton = () => {
        return(
            <div>
                <Button onClick={() => userLogin(userLoginModel)}
                        color={"primary"}
                >
                    Login
                </Button>
            </div>
        )
    }

    return (
        <form className={"entity"} noValidate autoComplete="off">
                <div className={"field-containers"}>
                    <TextField
                        onChange={onFormChange}
                        name="username"
                        label="Username"
                    />
                    <TextField onChange={onFormChange} name="password" label="Password" />
                </div>
            <LoginButton/>
        </form>
    );
}