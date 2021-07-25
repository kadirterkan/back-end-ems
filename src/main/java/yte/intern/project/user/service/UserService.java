package yte.intern.project.user.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yte.intern.project.user.entities.User;
import yte.intern.project.user.registration.request.RegisterRequest;
import yte.intern.project.user.repository.UserRepository;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı Bulunamadı"));
    }

    public UserDetails loadUserByUserId(Long id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(()->new Exception("Kullanıcı Bulunamadı"));
    }

    public User newUserRegistration(RegisterRequest registerRequest) throws Exception{
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new Exception("USER ALREADY EXISTS");
        }
        else{

            User user = new User();
            user.setEmail(registerRequest.getEmail());
            user.setUsername(registerRequest.getFirstName()+"."+registerRequest.getLastName());
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setTcKimlikNumber(registerRequest.getTcKimlikNumber());
            user.setPassword(registerRequest.getPassword());

            return userRepository.save(user);
        }
    }


}