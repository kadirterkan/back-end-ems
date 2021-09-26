package yte.intern.project.event.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventCoordinates {

    private Long lat;
    private Long lng;

    private String eventLocationName;

}
