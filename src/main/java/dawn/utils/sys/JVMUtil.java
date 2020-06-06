package dawn.utils.sys;

public class JVMUtil {

    /**
     * whether is multi instance
     *
     * @return whether multi
     */
    public static Boolean isMultiInstance() {
        return isMultiInstance;
    }

    private static Boolean isMultiInstance = false;
    private static final String TRUE = "true";

    static {
        String multiDeploy = System.getProperty("isMultiInstance", "false");
        if (TRUE.equals(multiDeploy)) {
            isMultiInstance = true;
        }
    }
}
