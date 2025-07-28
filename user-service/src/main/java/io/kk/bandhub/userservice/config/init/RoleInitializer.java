package io.kk.bandhub.userservice.config.init;

import io.kk.bandhub.userservice.model.Role;
import io.kk.bandhub.userservice.repository.JpaRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

    private final JpaRoleRepository roleRepository;

    @Override
    public void run(String... args) {
        initialize();
    }

    public void initialize() {
        if (roleRepository.count() == 0) {
            roleRepository.saveAll(List.of(
                    new Role("ADMIN"),
                    new Role("USER")
            ));
        }
    }
}
