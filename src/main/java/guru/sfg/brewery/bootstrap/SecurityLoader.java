package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SecurityLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        loadData();
    }

    private void loadData() {
        if(authorityRepository.count()==0){
            Authority createBeer= authorityRepository.save(Authority.builder().permission("beer.create").build());
            Authority updateBeer= authorityRepository.save(Authority.builder().permission("beer.update").build());
            Authority readBeer= authorityRepository.save(Authority.builder().permission("beer.read").build());
            Authority deleteBeer= authorityRepository.save(Authority.builder().permission("beer.delete").build());

            Authority createCustomer= authorityRepository.save(Authority.builder().permission("customer.create").build());
            Authority updateCustomer= authorityRepository.save(Authority.builder().permission("customer.update").build());
            Authority readCustomer= authorityRepository.save(Authority.builder().permission("customer.read").build());
            Authority deleteCustomer= authorityRepository.save(Authority.builder().permission("customer.delete").build());

            Authority readBrewery= authorityRepository.save(Authority.builder().permission("brewery.read").build());


            Role roleAdmin= roleRepository.save(Role.builder()
                    .name("ROLE_ADMIN")
                    .build());
            Role roleUser= roleRepository.save(Role.builder()
                    .name("ROLE_USER")
                    .build());
            Role roleCustomer= roleRepository.save(Role.builder()
                    .name("ROLE_CUSTOMER")
                    .build());
            roleAdmin.setAuthorities(Set.of(createBeer,updateBeer,readBeer,deleteBeer,createCustomer,updateCustomer
                    ,readCustomer,deleteCustomer,readBrewery));
            roleUser.setAuthorities(Set.of(readBeer));
            roleCustomer.setAuthorities(Set.of(readBeer,readBrewery,readCustomer));
            roleRepository.saveAll(Arrays.asList(roleUser,roleCustomer));

            User userAdmin= userRepository.save(User.builder()
                    .username("crinnger")
                    .password(passwordEncoder.encode("oliveira"))
                    .build());
            userAdmin.setRoles(Set.of(roleAdmin));

            User userUser = userRepository.save(User.builder()
                    .username("samuel")
                    .password(passwordEncoder.encode("oliveira"))
                    .build());
            userUser.setRoles(Set.of(roleUser));

            User userCustomer= userRepository.save(User.builder()
                    .username("kleyver")
                    .password(passwordEncoder.encode("oliveira"))
                    .build());
            userCustomer.setRoles(Set.of(roleCustomer));
            userRepository.saveAll(Arrays.asList(userAdmin,userUser,userCustomer));
        }
    }
}
