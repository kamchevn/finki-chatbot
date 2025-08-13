package com.example.finkichatbot.web;

import com.example.finkichatbot.model.Group;
import com.example.finkichatbot.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/group")
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<Group> getGroup(@PathVariable Long groupId){
        if(groupService.getGroup(groupId) != null){
            return ResponseEntity.ok(groupService.getGroup(groupId));
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/edit/{groupId}")
    public ResponseEntity<Group> editGroupName(@PathVariable Long groupId, @RequestParam String groupName){
        if(groupService.getGroup(groupId) != null){
            return ResponseEntity.ok(groupService.editGroup(groupId,groupName));
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/delete/{groupId}")
    public ResponseEntity<Group> deleteGroup(@PathVariable Long groupId){
        if(groupService.getGroup(groupId) != null){
            return ResponseEntity.ok(groupService.deleteGroup(groupId));
        }
        return ResponseEntity.notFound().build();
    }
}
