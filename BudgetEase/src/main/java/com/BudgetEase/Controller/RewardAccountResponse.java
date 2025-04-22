package com.BudgetEase.Controller;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RewardAccountResponse {

    private String userId;
    private int totalPoints;
    private List<RewardDTO> rewards;

    public RewardAccountResponse(String userId, int totalPoints, List<RewardDTO> rewards){
        this.userId=userId;
        this.totalPoints=totalPoints;
        this.rewards=rewards;
    }

}
