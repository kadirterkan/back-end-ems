import {Button, TextField} from "@material-ui/core";
import {ChangeEvent, useState} from "react";
import {MessageType} from "../common/dto/MessageResponse";
import {toast} from "react-toastify";
import {UserRegistrationApi} from "./api/UserRegistrationAPI";




interface Props{
    addUser: (model: UserModel) => void
}

export interface UserModel{
    firstName:string;
    lastName:string;
    email:string;
    tcKimlikNumber:string;
    password:string;
}

const initialState: UserModel = {
    firstName:"",
    lastName:"",
    email:"",
    tcKimlikNumber:"",
    password:""
};

export function UserRegistration(props: Props){

    const [userModel, setUserModel] = useState<UserModel>(initialState);

    const userRegistrationApi = new UserRegistrationApi();

    const addUser = async (model:UserModel) => {
        console.log("TEST");
        const messageResponse = await userRegistrationApi.addUser(model);
        if(messageResponse.messageType === MessageType.SUCCESS){
            toast.success(messageResponse.message);
        } else {
            toast.error(messageResponse.message);
        }
    }

    const onFormChange = (event: ChangeEvent<HTMLInputElement>) => {
        const field = event.target.name;
        const value = event.target.value;

        setUserModel(updateFormState(field,value));
    }

    function updateFormState(field: string,value:string){
        const newUserModelState = {...userModel};
        switch(field){
            case "firstName":
                newUserModelState.firstName = value;
                break;
            case "lastName":
                newUserModelState.lastName = value;
                break;
            case "email":
                newUserModelState.email = value;
                break;
            case "tcKimlikNumber":
                newUserModelState.tcKimlikNumber = value;
                break;
            case "password":
                newUserModelState.password = value;
                break;
        }
        return newUserModelState;
    }

    const Submit = () => {
        return(
            <div className={"button"}>
                <Button onClick={() => addUser(userModel)}
                        color={"primary"}>Submit</Button>
            </div>
        )
    }


    return(
        <form className={"entity"} noValidate autoComplete="off">
            <div>
                <TextField onChange={onFormChange} name="firstName" label="First Name" />
                <TextField onChange={onFormChange} name="lastName" label="Last Name" />
                <TextField onChange={onFormChange} name="tcKimlikNumber" label="TC NO" />
                <TextField onChange={onFormChange} name="email" label="Email" />
                <TextField onChange={onFormChange} name="password" label="Password" />
        </div>
            <Submit/>
        </form>
    );
}