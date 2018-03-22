package indi.jackwan.oleducation.service;


import indi.jackwan.oleducation.models.User;
import indi.jackwan.oleducation.repositories.UserRepository;
import indi.jackwan.oleducation.utils.Enums.LoginResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Log an user in.
     * @param email User inputed email.
     * @param rawPassword User inputed raw password.
     * @return LoginResult
     */
    public LoginResult login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return LoginResult.NO_SUCH_ACCOUNT;

        // You should not encrypt the raw password yourself.
        boolean match = bCryptPasswordEncoder.matches(rawPassword, user.getPassword());

        if (match) {
            if (user.getEnabled()) {
                return LoginResult.SUCCESS;
            } else {
                return LoginResult.NOT_ACTIVATED;
            }
        } else {
            return LoginResult.WRONG_PASSWORD;
        }

    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByConfirmationToken(String confirmationToken) {
        return userRepository.findByConfirmationToken(confirmationToken);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}