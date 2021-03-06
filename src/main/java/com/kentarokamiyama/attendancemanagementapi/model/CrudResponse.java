package com.kentarokamiyama.attendancemanagementapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonFormat
public class CrudResponse {

    @Nullable
    private Integer number;

    private String message;

    private boolean ok;

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
