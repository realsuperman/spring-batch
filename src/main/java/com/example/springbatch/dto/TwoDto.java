package com.example.springbatch.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TwoDto {
    private String one;
    private String two;
}

// 111, 222 인 경우 one에는 111이 들어가고 two에는 222가 들어간다