package yte.intern.project.event.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventOnlinePageReq {

    private String eventUrl;

    private List<String> questions;


}
