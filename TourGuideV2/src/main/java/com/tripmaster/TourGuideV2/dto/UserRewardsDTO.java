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
     * @param pUserName
     */
    public void setUserName(final String pUserName) {
        userName = pUserName;
    }

    /**
     * Getter of userRewardsDTO attribute.
     *
     * @return a List<UserRewardDTO>
     */
    public List<UserRewardDTO> getUserRewardsDTO() {
        return userRewardsDTO;
    }

    /**
     * Method used to add a new userRewardDTO to the list of user Rewards.
     *
     * @param pUserRewardDTO
     */
    public void addUserRewardDTO(final UserRewardDTO pUserRewardDTO) {
        userRewardsDTO.add(pUserRewardDTO);
    }

    /**
     * Serialization method.
     */
    @Override
    public String toString() {
        return "UserRewardsDTO [userName=" + userName + ", userRewardsDTO="
                + userRewardsDTO + "]";
    }

}
