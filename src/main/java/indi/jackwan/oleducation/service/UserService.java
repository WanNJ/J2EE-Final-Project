package indi.jackwan.oleducation.service;


import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import indi.jackwan.oleducation.models.User;
import indi.jackwan.oleducation.repositories.UserRepository;
import indi.jackwan.oleducation.utils.Enums.LoginResult;
import indi.jackwan.oleducation.utils.Enums.RegisterResult;
import indi.jackwan.oleducation.utils.Login.LoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Service("userService")
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailService emailService;

    /**
     * Log an user in.
     * @param email       User inputed email.
     * @param rawPassword User inputed raw password.
     * @return LoginResult
     */
    public LoginResult login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return LoginResult.NO_SUCH_ACCOUNT;
        boolean match = bCryptPasswordEncoder.matches(rawPassword, user.getPassword());

        return LoginUtil.getLoginResult(match, user.isEnabled());

    }


    /**
     * Register an user.
     * @param user The user who wants to register.
     * @return RegisterResult
     */
    public RegisterResult register(User user) {
        User userExists = userRepository.findByEmail(user.getEmail());

        // In case that one's account is registered by someone unknown
        if (userExists != null) {
            return RegisterResult.ALREADY_REGISTERED;
        }

        Zxcvbn passwordCheck = new Zxcvbn();
        Strength strength = passwordCheck.measure(user.getPassword());

        if (strength.getScore() < 3) {
            return RegisterResult.PASSWORD_TOO_WEAK;
        } else {
            // Set bCrpyted Password to improve security
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            // Disable user until they click on confirmation link in email
            user.setEnabled(false);
            user.setVip(false);
            // Generate random 36-character string token for confirmation link
            user.setConfirmationToken(UUID.randomUUID().toString());
            userRepository.save(user);

            emailService.sendConfirmationEmail(user);
            return RegisterResult.SUCCESS;
        }
    }

    /**
     * Confirm an user's registeration.
     * @param confirmationToken String
     * @param session HttpSession
     * @return False if there is no such confirmationToken, else true.
     */
    public boolean confirm(String confirmationToken, HttpSession session) {
        User user = userRepository.findByConfirmationToken(confirmationToken);
        if(user == null) {
            return false;
        } else {
            user.setEnabled(true);
            user.setVip(true);
            userRepository.save(user);

            session.setAttribute("user", user);
            // TODO Set different roles to different user types.
            session.setAttribute("role", "USER");
            return true;
        }
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByConfirmationToken(String confirmationToken) {
        return userRepository.findByConfirmationToken(confirmationToken);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}