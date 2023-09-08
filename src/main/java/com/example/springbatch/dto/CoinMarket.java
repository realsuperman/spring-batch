package com.example.springbatch.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CoinMarket {
    String market;
    String korean_name;
    String english_name;
}