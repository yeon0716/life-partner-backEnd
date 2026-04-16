package com.life.routine.vo;

import java.util.Date;

import lombok.Data;

@Data
public class RoutineVO {
    private Long routineId;
    private Long memberId;
    private String title;
    private String content;
    private Date createdAt;
}
