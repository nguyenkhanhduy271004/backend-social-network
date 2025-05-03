package com.project.social_network.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardDto {
    private int totalUsers;
    private int totalPosts;
    private int totalComments;
    private int totalLikes;
    private int totalGroups;
    private int totalStories;
    private int totalReels;
    private int totalMessages;

    private Map<String, Integer> userRegistrationsByMonth;
    private Map<String, Integer> postsByMonth;
}