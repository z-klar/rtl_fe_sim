package tables;

import commonEnum.TestrackAvailability;
import commonEnum.TestrackVehicle;

public class JanusOverviewTable {
    public String sessionId;
    public String handleId;
    public String handleData;
    public String streamId;
    public String streamData;
    public String componentId;
    public String componentData;


    public JanusOverviewTable(String sessionId, String handleId, String handleData,
                              String streamId, String streamData,
                              String componentId, String componentData) {
        this.sessionId = sessionId;
        this.handleId = handleId;
        this.handleData = handleData;
        this.streamData = streamData;
        this.componentData = componentData;
        this.streamId = streamId;
        this.componentId = componentId;
    }

    public Object[] toObject() {
        return new Object[]{sessionId, handleId, handleData, streamId,
                            streamData, componentId, componentData };
    }
}
