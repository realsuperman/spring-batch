package com.example.springbatch.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Dept2 {
    @Id
    Integer deptNo;
    String dName;
    String loc;
}
