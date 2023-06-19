package com.vinplay.api.backend.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserOfAgentResponse extends BaseResponse {
    private Long total_nap;
    private Long total_rut;
    private Long total_doanhthu;

    public UserOfAgentResponse(boolean success, String errorCode) {
        super(success, errorCode);
    }

    public UserOfAgentResponse(boolean success, String errorCode, Object data) {
        super(success, errorCode, data);
    }

    public UserOfAgentResponse(boolean success, String errorCode, int totalData) {
        super(success, errorCode, totalData);
    }

    public UserOfAgentResponse(boolean success, String errorCode, Object data, int totalData) {
        super(success, errorCode, data, totalData);
    }

    public UserOfAgentResponse(boolean success, String errorCode, Object data, int totalData, Long total_nap, Long total_rut, Long total_doanhthu) {
        super(success, errorCode, data, totalData);
        this.total_nap = total_nap;
        this.total_rut = total_rut;
        this.total_doanhthu = total_doanhthu;
    }

    public Long getTotal_nap() {
        return total_nap;
    }

    public void setTotal_nap(Long total_nap) {
        this.total_nap = total_nap;
    }

    public Long getTotal_rut() {
        return total_rut;
    }

    public void setTotal_rut(Long total_rut) {
        this.total_rut = total_rut;
    }

    public Long getTotal_doanhthu() {
        return total_doanhthu;
    }

    public void setTotal_doanhthu(Long total_doanhthu) {
        this.total_doanhthu = total_doanhthu;
    }

    @Override
    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString((Object)this);
        }
        catch (JsonProcessingException mapper) {
            return "{\"success\":false,\"errorCode\":\"1001\",\"totalData\":\"0\",\"totalNap\":\"0\",\"totalRut\":\"0\",\"totalDoanhThu\":\"0\"}";
        }
    }

}
