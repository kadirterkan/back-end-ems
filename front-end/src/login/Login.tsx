import React, {ChangeEvent, useState} from 'react';
import {Button, Dialog, DialogActions, DialogContent, DialogTitle, TextField} from "@material-ui/core";
import {LoginApi, RoleEnum} from "./api/LoginAPI";
import {MessageType} from "../common/dto/MessageResponse";
import {toast} from "react-toastify";
import {useHistory} from "react-router-dom";


export interface Props{
    isOpen:boolean;
    handleClose:() => void;
    loggedIn:()=>void;
    role:(value:string) => void;
}

export interface UserLoginModel{
    username: string;
    password: string;
}

const initialState: UserLoginModel = {
    username: "",
    password: ""
};


export function Login(props: Props) {

    let isValid = true;

    const history = useHistory();

    const loginApi = new LoginApi();

    // const loggedIn = async() => {
    //     let response = await loginApi.loggedIn();
    //
    //     if (response) {
    //         setUserLoggedIn(true);
    //     }
    //                      TODO: HANDLE MOUNTING
    // }
    //
    // loggedIn();

    const [userLoggedIn, setUserLoggedIn] = useState(false);
    const [userLoginModel, setUserLoginModel] = useState<UserLoginModel>(initialState);



    const userLogin = async (model: UserLoginModel) => {
        const messageResponse = await loginApi.Login(model);
        if (messageResponse.messageType === MessageType.LOGIN_USER) {
            isValid = true;
            toast.success(messageResponse.message);
            history.push('/');
            props.handleClose();
            props.loggedIn();
            props.role(RoleEnum.USER);
        } else if(messageResponse.messageType === MessageType.LOGIN_MOD){
            isValid = true;
            toast.success(messageResponse.message);
            history.push('/');
            props.handleClose();
            props.loggedIn();
            props.role(RoleEnum.MOD);
        }else
        {
            isValid = false;
            toast.error(messageResponse.message);
        }
    }



    const onFormChange = (event: ChangeEvent<HTMLInputElement>) => {
        const field = event.target.name;
        const value = event.target.value;

        setUserLoginModel(updateFormState(field, value));
    }

    function updateFormState(field: string, value: string) {
        const newModelState = {...userLoginModel};
        switch (field) {
            case "username":
                newModelState.username = value;
                setUserLoggedIn(true);
                break;
            case "password":
                newModelState.password = value;
                break;
        }
        return newModelState;
    }

    let UsernameField = () => {
        return (
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
        return (
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
        return (
            <div>
                <TextField onChange={onFormChange} name="password" label="Password"/>
            </div>
        )
    }

    const ErrorPasswordField = () => {
        return (
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
        return (
            <div>
                <Button onClick={() => userLogin(userLoginModel)}
                        color={"primary"}
                >
                    Login
                </Button>
            </div>
        )
    }


    return(
        <Dialog
            open={props.isOpen}
            onClose={props.handleClose}>
            <DialogTitle>Login</DialogTitle>
            <DialogContent>
                <TextField
                    onChange={onFormChange}
                    name={"username"}
                    label={"Username"}
                    />
                <TextField
                    onChange={onFormChange}
                    name={"password"}
                    type={'password'}
                    label={"Password"}
                    />
            </DialogContent>
            <DialogActions>
                <Button onClick={props.handleClose}>Cancel</Button>
                <LoginButton/>
            </DialogActions>
        </Dialog>
    );
}