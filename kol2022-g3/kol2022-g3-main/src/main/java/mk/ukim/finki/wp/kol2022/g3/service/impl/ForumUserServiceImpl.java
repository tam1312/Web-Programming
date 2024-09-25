package mk.ukim.finki.wp.kol2022.g3.service.impl;

import mk.ukim.finki.wp.kol2022.g3.model.ForumUser;
import mk.ukim.finki.wp.kol2022.g3.model.ForumUserType;
import mk.ukim.finki.wp.kol2022.g3.model.exceptions.InvalidForumUserIdException;
import mk.ukim.finki.wp.kol2022.g3.repository.ForumUserRepository;
import mk.ukim.finki.wp.kol2022.g3.service.ForumUserService;
import mk.ukim.finki.wp.kol2022.g3.service.InterestService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForumUserServiceImpl implements ForumUserService, UserDetailsService {
    private final ForumUserRepository forumUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final InterestService interestService;
    public ForumUserServiceImpl(ForumUserRepository forumUserRepository, PasswordEncoder passwordEncoder, InterestService interestService) {
        this.forumUserRepository = forumUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.interestService = interestService;
    }

    @Override
    public List<ForumUser> listAll() {
        return this.forumUserRepository.findAll();
    }

    @Override
    public ForumUser findById(Long id) {
        return this.forumUserRepository.findById(id).orElseThrow(InvalidForumUserIdException::new);
    }

    @Override
    public ForumUser create(String name, String email, String password,
                            ForumUserType type, List<Long> interestId, LocalDate birthday) {
        ForumUser forumUser = new ForumUser(
                name,
                email,
                passwordEncoder.encode(password),
                type,
                interestId.stream().map(id -> this.interestService.findById(id)).collect(Collectors.toList()),
                birthday
        );

        return this.forumUserRepository.save(forumUser);
    }

    @Override
    public ForumUser update(Long id, String name, String email,
                            String password, ForumUserType type, List<Long> interestId, LocalDate birthday) {
        ForumUser forumUser = this.findById(id);
        forumUser.setName(name);
        forumUser.setEmail(email);
        forumUser.setPassword(passwordEncoder.encode(password));
        forumUser.setType(type);
        forumUser.setInterests(interestId.stream().map(this.interestService::findById).collect(Collectors.toList()));
        forumUser.setBirthday(birthday);

        return this.forumUserRepository.save(forumUser);
    }

    @Override
    public ForumUser delete(Long id) {
        ForumUser forumUser = this.findById(id);
        this.forumUserRepository.delete(forumUser);
        return forumUser;
    }

    @Override
    public List<ForumUser> filter(Long interestId, Integer age) {
        if(interestId != null && age != null){
            return this.forumUserRepository.findAllByInterestsContainsAndBirthdayBefore(
                    this.interestService.findById(interestId),
                    LocalDate.now().minusYears(age)
            );
        }else if(interestId != null){
            return this.forumUserRepository.findAllByInterestsContains(
                    this.interestService.findById(interestId)
            );
        }else if(age != null){
            return this.forumUserRepository.findAllByBirthdayBefore(
                    LocalDate.now().minusYears(age)
            );
        }else{
            return this.forumUserRepository.findAll();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ForumUser forumUser = this.forumUserRepository.findByEmail(username);
        return User.builder()
                .username(forumUser.getEmail())
                .password(forumUser.getPassword())
                .roles(forumUser.getType().name())
                .build();
    }
}
