package yte.intern.project.common.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import yte.intern.project.common.enums.MessageType;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private final MessageType messageType;
    private final String message;

    private String base64QrCode;

    public boolean hasError(){
        return messageType == MessageType.ERROR;
    }
}
