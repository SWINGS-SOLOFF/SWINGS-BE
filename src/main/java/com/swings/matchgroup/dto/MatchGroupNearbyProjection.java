package com.swings.matchgroup.dto;

public interface MatchGroupNearbyProjection {

    Long getMatchGroupId();
    String getGroupName();
    String getLocation();
    Double getLatitude();
    Double getLongitude();
    String getSchedule();
    String getHostUsername();
}
