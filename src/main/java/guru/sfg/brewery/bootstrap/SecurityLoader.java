package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) throws Exception {

        if(authorityRepository.count()==0){
            Authority admin= authorityRepository.save(Authority.builder().Role("ROLE_ADMIN").build());
            Authority user= authorityRepository.save(Authority.builder().Role("ROLE_USER").build());
            Authority costumer= authorityRepository.save(Authority.builder().Role("ROLE_CUSTOMER").build());

            userRepository.save(User.builder()
                    .username("crinnger")
                    .password("{bcrypt}$2a$10$sN8YQ2lptpyyyPctH/azF.rjf6VBbB3OAEAyIQYI50gNZ8D35C3Iy")
                    .authority(admin)
                    .build());
            userRepository.save(User.builder()
                    .username("samuel")
                    .password("{sha256}14d69bec076cfcc9f6a9b232dd9a534b6259432a90739db3609084f50aab230b3b9d96bf1f8860e3")
                    .authority(user)
                    .build());
            userRepository.save(User.builder()
                    .username("kleyver")
                    .password("{bcrypt15}$2a$10$JR/RtM4yX8hR56YFNV.dYuffKR.k/kMZPR03gZFK25CWrOnmiaQoS")
                    .authority(costumer)
                    .build());
        }
    }
}
