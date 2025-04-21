package com.BudgetEase.Controller;

import com.BudgetEase.Models.Goal;
import com.BudgetEase.BudgetEaseService.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @GetMapping
    public List<Goal> getAllGoals() {
        return goalService.getAllGoals();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Goal> getGoalById(@PathVariable String id) {
        try {
            Goal goal = goalService.getGoalById(id);
            return ResponseEntity.ok(goal);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/{id}/purpose")
    public ResponseEntity<?> getGoalPurpose(@PathVariable String id) {
        try {
            Goal goal = goalService.getGoalById(id);
            return ResponseEntity.ok(goal.getPurpose());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/createGoal")
    public ResponseEntity<Goal> createGoal(@RequestBody Goal goal) {
        if (!goalService.checkStartAndEndDate(goal)) {
            return ResponseEntity.badRequest().body(null);
        }
        Goal createdGoal = goalService.createGoal(goal);
        return ResponseEntity.ok(createdGoal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGoal(@PathVariable String id, @RequestBody Goal goalDetails) {
        try {
            goalDetails.setGoalId(id);
            Goal goal = goalService.updateGoal(goalDetails);
            System.out.println(goal);
            return ResponseEntity.ok().body(goal);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGoal(@PathVariable String id) {
        try {
            Goal deletedGoal = goalService.deleteGoal(id);
            return ResponseEntity.ok("Deleted goal: " + deletedGoal.getName());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/findGoalByName/{name}")
    public ResponseEntity<?> findGoalByName(@PathVariable String name){

        System.out.println(goalService.getGoalByName(name));
        return ResponseEntity.ok(goalService.getGoalByName(name));
    }
}
