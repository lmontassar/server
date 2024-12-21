package com.backend.server.repository;
import com.backend.server.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.server.entity.User;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    public User getUserById(Long id);
    public Optional<User> findOneByEmail(String email);
    public User findOneByUsername(String username);
    public List<User> findAllByRole(User.Role role);
    public List<User> findAllByRoleAndId(User.Role role,Long user);
    public List<User> findAllByRoleIn(List<User.Role> roles);
}
