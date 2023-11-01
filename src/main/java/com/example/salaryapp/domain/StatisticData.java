package com.example.salaryapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticData {

    private Integer total;
    private Set<String> keys;
    private LinkedList<Map<String, String>> chartData;
    private Map<String, Integer> tableData;

}
