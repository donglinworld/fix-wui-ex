package easyfix;

public class MsgUtil {
    private static final String SOH = String.valueOf('\001');

    public static simplefix.Message convertToFIX(String msg) {
        return new simplefix.quickfix.Message(msg.replace("|", SOH));
    }
}
