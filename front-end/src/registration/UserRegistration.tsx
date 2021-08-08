import {
    Button,
    FormControlLabel,
    FormLabel,
    Radio,
    RadioGroup,
    TextField
} from "@material-ui/core";
import {ChangeEvent, useState} from "react";
import {MessageType} from "../common/dto/MessageResponse";
import {toast} from "react-toastify";
import {UserRegistrationApi} from "./api/UserRegistrationAPI";
import {useHistory} from "react-router-dom";






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

export interface ModModel{
    firstName:string;
    lastName:string;
    email:string;
    password:string;
    companyName:string;
    departmentName:string;
}

const initialStateMod: ModModel = {
    firstName:"",
    lastName:"",
    email:"",
    companyName:"",
    password:"",
    departmentName:""
};

const initialStateUser: UserModel = {
    firstName:"",
    lastName:"",
    email:"",
    tcKimlikNumber:"",
    password:""
};

export function UserRegistration(props: Props){

    const history = useHistory();

    const [userModel, setUserModel] = useState<UserModel>(initialStateUser);
    const [modModel,setModeModel] = useState<ModModel>(initialStateMod);
    const [simpleUserReg,setSimpleUserReg] = useState("true");

    const userRegistrationApi = new UserRegistrationApi();

    async function addUser(){
        if(simpleUserReg == "true"){
            console.log("TEST");
            const messageResponse = await userRegistrationApi.addUser(userModel);
            if(messageResponse.messageType === MessageType.SUCCESS){
                toast.success(messageResponse.message);
                toast.info("YOU ARE BEING DIRECTED TO THE MAIN PAGE");
                history.push("/");
            } else {
                toast.error(messageResponse.message);
            }
        }else {
            const messageResponse = await userRegistrationApi.addMod(modModel);
            if(messageResponse.messageType === MessageType.SUCCESS){
                toast.success(messageResponse.message);
                toast.info("YOU ARE BEING DIRECTED TO THE MAIN PAGE");
                history.push("/");
            } else {
                toast.error(messageResponse.message);
            }
        }
    }

    // const addUser = async (model:UserModel) => {
    //     if(simpleUserReg == "true"){
    //         const messageResponse = await userRegistrationApi.addUser(model);
    //         if(messageResponse.messageType === MessageType.SUCCESS){
    //             toast.success(messageResponse.message);
    //         } else {
    //             toast.error(messageResponse.message);
    //         }
    //     }else {
    //         const messageResponse = await userRegistrationApi.addMod(model);
    //         if(messageResponse.messageType === MessageType.SUCCESS){
    //             toast.success(messageResponse.message);
    //         } else {
    //             toast.error(messageResponse.message);
    //         }
    //     }
    //
    // }

    const onFormChange = (event: ChangeEvent<HTMLInputElement>) => {
        const field = event.target.name;
        const value = event.target.value;

        if(simpleUserReg == "true"){
            setUserModel(updateFormState(field,value));
        }
        else{
            setModeModel(updateModFormState(field,value));
        }
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

    function updateModFormState(field: string,value:string){
        const newModModelState = {...modModel};
        switch(field){
            case "firstName":
                newModModelState.firstName = value;
                break;
            case "lastName":
                newModModelState.lastName = value;
                break;
            case "email":
                newModModelState.email = value;
                break;
            case "companyName":
                newModModelState.companyName = value;
                break;
            case "departmentName":
                newModModelState.departmentName = value;
                break;
            case "password":
                newModModelState.password = value;
                break;
        }
        return newModModelState;
    }

    const Submit = () => {
        return(
            <div className={"button"}>
                <Button onClick={() => addUser()}
                        color={"primary"}>Submit</Button>
            </div>
        )
    }


    return(
        <form className={"entity"} noValidate autoComplete="off">
            <FormLabel className={"entity"}>User Type</FormLabel>
            <RadioGroup aria-label ={"usertype"} name={"user-type"} value={simpleUserReg} onChange={(event) => setSimpleUserReg(event.target.value)}>
                <FormControlLabel name={"simpleUser"} value={"true"} control={<Radio />} label={"User"}/>
                <FormControlLabel name={"corporateUser"} value={"false" } control={<Radio />} label={"Corporate User"}/>
            </RadioGroup>
            <div>
                { simpleUserReg == "true" ?
                    <div>
                        <div><TextField onChange={onFormChange} name="firstName" label="First Name" /></div>
                        <div><TextField onChange={onFormChange} name="lastName" label="Last Name" /></div>
                        <div><TextField onChange={onFormChange} name="tcKimlikNumber" label="TC NO" /></div>
                        <div><TextField onChange={onFormChange} name="email" label="Email" /></div>
                        <div><TextField onChange={onFormChange} type={'password'} name="password" label="Password" /></div>
                    </div>
                    :
                    <div>
                        <div><TextField onChange={onFormChange} name="firstName" label="First Name" /></div>
                        <div><TextField onChange={onFormChange} name="lastName" label="Last Name" /></div>
                        <div><TextField onChange={onFormChange} name="companyName" label="Company Name" /></div>
                        <div><TextField onChange={onFormChange} name="departmentName" label="Department" /></div>
                        <div><TextField onChange={onFormChange} name="email" label="Email" /></div>
                        <div><TextField onChange={onFormChange} type={'password'} name="password" label="Password" /></div>
                    </div>
                }

        </div>
            <Submit/>
        </form>
    );
}