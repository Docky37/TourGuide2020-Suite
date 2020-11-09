package com.tripmaster.TourGuideV2.helper;

/**
 * This class only contains one private static attribute, the number of internal
 * users to create for tests (default value = 100), and its getter & setter.
 *
 * @author TripMaster
 * @author Thierry Schreiner
 */
public final class InternalTestHelper {

    /**
     * This attribute defines the default number of users.
     */
    static final int DEFAULT_USER_NUMBER = 100;

    /**
     * The number of internal users to create for tests. (Set this default up to
     * 100,000 for testing)
     */
    private static int internalUserNumber = DEFAULT_USER_NUMBER;

    /**
     * Setter of internalUserNumber.
     *
     * @param pInternalUserNumber
     */
    public static void setInternalUserNumber(final int pInternalUserNumber) {
        InternalTestHelper.internalUserNumber = pInternalUserNumber;
    }

    /**
     * Getter of internalUserNumber.
     *
     * @return an int
     */
    public static int getInternalUserNumber() {
        return internalUserNumber;
    }

    /**
     * No argument empty class constructor.
     */
    private InternalTestHelper() {
    }
}
