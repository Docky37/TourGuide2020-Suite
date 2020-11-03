package com.tripmaster.TourGuideV2.helper;

/**
 * This class only contains one private static attribute, the number of internal
 * users to create for tests (default value = 100), and its getter & setter.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
public class InternalTestHelper {

    /**
     * The number of internal users to create for tests.
     * (Set this default up to 100,000 for testing)
     */
    private static int internalUserNumber = 10;

    /**
     * Setter of internalUserNumber.
     *
     * @param internalUserNumber
     */
    public static void setInternalUserNumber(int internalUserNumber) {
        InternalTestHelper.internalUserNumber = internalUserNumber;
    }

    /**
     * Getter of internalUserNumber.
     *
     * @return an int
     */
    public static int getInternalUserNumber() {
        return internalUserNumber;
    }
}
