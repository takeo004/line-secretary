package com.example.api.controller.request.googlecalendar;

import com.example.api.controller.request.LineRequestBase;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class GoogleCalendarRequestBase extends LineRequestBase {
    
    /**
     * 予定開始日
     */
    private String startDate;

    /**
     * 予定終了日
     */
    private String endDate;
}
