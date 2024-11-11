package com.ssafy.runit.domain.record.dto.response;

import com.ssafy.runit.domain.record.entity.Record;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record RecordGetCalendarResponse(

        List<RecordGetListResponse> mondayList,
        List<RecordGetListResponse> tuesdayList,
        List<RecordGetListResponse> wednesdayList,
        List<RecordGetListResponse> thursdayList,
        List<RecordGetListResponse> fridayList,
        List<RecordGetListResponse> saturdayList,
        List<RecordGetListResponse> sundayList

        ) {

    public static RecordGetCalendarResponse fromEntity(List<List<RecordGetListResponse>> weekLists) {
        return RecordGetCalendarResponse.builder()
                .mondayList(weekLists.get(0))
                .tuesdayList(weekLists.get(1))
                .wednesdayList(weekLists.get(2))
                .thursdayList(weekLists.get(3))
                .fridayList(weekLists.get(4))
                .saturdayList(weekLists.get(5))
                .sundayList(weekLists.get(6))
                .build();
    }
}
