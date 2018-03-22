package indi.jackwan.oleducation.utils.Login;

import indi.jackwan.oleducation.utils.Enums.LoginResult;

public class LoginUtil {
    public static LoginResult getLoginResult(boolean match, boolean enabled) {
        if (match) {
            if (enabled) {
                return LoginResult.SUCCESS;
            } else {
                return LoginResult.NOT_ACTIVATED;
            }
        } else {
            return LoginResult.WRONG_PASSWORD;
        }
    }
}
