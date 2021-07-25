import {TextField} from "@material-ui/core";
import {ChangeEvent, useState} from "react";

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


    return(
        <form className={"entity"} noValidate autoComplete="off">
            <div>
                <TextField id="firstName" label="First Name" />
                <TextField id="lastName" label="Last Name" />
                <TextField id="tcKimlikNumber" label="TC NO" />
                <TextField id="email" label="Email" />
                <TextField id="password" label="Password" />
            </div>
        </form>
    );
}