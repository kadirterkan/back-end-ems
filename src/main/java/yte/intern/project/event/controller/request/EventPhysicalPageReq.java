package yte.intern.project.event.controller.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventPhysicalPageReq {

    private String eventLocationName;

    private Long lat;

    private Long lng;
}
