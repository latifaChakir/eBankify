package com.example.ebankify.domain.vm;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StatisticResponseVM {
    private long users;
    private long employes;
    private long administrators;
    private long accounts;
    private long banks;
}
