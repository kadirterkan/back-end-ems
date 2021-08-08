export interface MessageResponse {
    messageType: MessageType;
    message: string
}

export enum MessageType {
    SUCCESS = "SUCCESS", INFO = "INFO" , WARNING = "WARNING" , ERROR= "ERROR",LOGIN_MOD="LOGIN_MOD",LOGIN_USER="LOGIN_USER",
    ERROR_400 = "ERROR_400", ERROR_403 = "ERROR_403" , ERROR_500 = "ERROR_500"
}