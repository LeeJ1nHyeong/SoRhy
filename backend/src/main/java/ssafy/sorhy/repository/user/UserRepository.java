package ssafy.sorhy.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ssafy.sorhy.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickname(String nickname);

    boolean existsByNickname(String nickname);

    @Query("select u from User u join u.company c where c.id = :companyId order by u.totalScore desc")
    Page<User> findUserRankInCompany(Long companyId, Pageable pageable);

    @Query("select u from User u " +
            "join u.company c " +
            "where c.id = :companyId " +
            "and " +
            "u.totalScore = (select max(u2.totalScore) from User u2 join u2.company c2 where c2.id = :companyId)")
    Optional<User> findCompanyFirstRankUser(Long companyId);
}
