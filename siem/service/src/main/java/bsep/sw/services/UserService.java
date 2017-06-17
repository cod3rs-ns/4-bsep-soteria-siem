package bsep.sw.services;

import bsep.sw.domain.User;
import bsep.sw.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(final UserRepository repository) {
        this.repository = repository;
    }

    public User save(final User newUser) {
        final User user = repository.findOneByUsername(newUser.getUsername());
        if (newUser.getPassword() != null && (user == null || !newUser.getPassword().equals(user.getPassword()))) {
            final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            final String hashedPassword = passwordEncoder.encode(newUser.getPassword());
            newUser.setPassword(hashedPassword);
        }

        return repository.save(newUser);
    }

    @Transactional(readOnly = true)
    public User findOne(final Long id) {
        return repository.findOne(id);
    }

    public void delete(final Long id) {
        repository.delete(id);
    }

    @Transactional(readOnly = true)
    public User findUserByUsername(final String username) {
        return repository.findOneByUsername(username);
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(final String email) {
        return repository.findOneByEmail(email);
    }

}
