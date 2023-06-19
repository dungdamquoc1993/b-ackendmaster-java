package com.vinplay.api.processors.events;

import com.vinplay.api.processors.events.response.DSEventMoonResponse;
import com.vinplay.dal.service.EventsService;
import com.vinplay.dal.service.impl.EventsServiceImpl;
import com.vinplay.vbee.common.cp.BaseProcessor;
import com.vinplay.vbee.common.cp.Param;
import com.vinplay.vbee.common.response.MoonEventResponse;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class GetEventProcessor  implements BaseProcessor<HttpServletRequest, String> {
    private Logger logger = Logger.getLogger((String)"api");

    public String execute(Param<HttpServletRequest> param) {
        DSEventMoonResponse response = new DSEventMoonResponse(false, "1001");
        EventsService service = new EventsServiceImpl();

        try {
            List<MoonEventResponse> results = service.listEventsMoon();
            response.setLstMoonEvents(results);
            response.setSuccess(true);
            response.setErrorCode("0");
        }
        catch (Exception e) {
            this.logger.error((Object)"List event moon Error: ", (Throwable)e);
            return e.getMessage();
        }

        return response.toJson();
    }
}
