package com.tripmaster.TourGuideV2.dto;

import java.util.ArrayList;
import java.util.List;

public class UserRewardsDTO {

    /**
     * This attribute store the name of the user. 
     */
    private String userName;

    /**
     * This attribute is a list of this user's rewards.
     */
    private List<UserRewardDTO> userRewardsDTO = new ArrayList<>();

    /**
     * No arguments constructor.
     */
    public UserRewardsDTO() {
    }

    /**
     * Getter of userName attribute.
     *
     * @return a String
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter of userName attribute.
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Getter of userRewardsDTO attribute.
     * @return
     */
    public List<UserRewardDTO> getUserRewardsDTO() {
        return userRewardsDTO;
    }

    /**
     * Method used to add a new userRewardDTO to the list of user Rewards.
     *
     * @param userRewardDTO
     */
    public void addUserRewardDTO(UserRewardDTO userRewardDTO) {
        userRewardsDTO.add(userRewardDTO);
    }

    @Override
    public String toString() {
        return "UserRewardsDTO [userName=" + userName + ", userRewardsDTO="
                + userRewardsDTO + "]";
    }

    
}
