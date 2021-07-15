package yte.intern.project.user.enumer;

public enum RolePermisionsEnum {
    EVENT_WRITE("EVENT_WRITE"),
    EVENT_JOIN("EVENT_JOIN"),
    EVENT_READ("EVENT_READ");

    private final String permision;

    RolePermisionsEnum(String event_read) {
        this.permision = event_read;
    }

    public String getPermision(){
        return permision;
    }
}
