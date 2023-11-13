package ssafy.sorhy.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ssafy.sorhy.dto.user.UserDto;
import ssafy.sorhy.dto.user.UserEachGameScore;
import ssafy.sorhy.dto.user.UserRankInfoDto;
import ssafy.sorhy.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickname(String nickname);

    boolean existsByNickname(String nickname);

    @Query("select u from User u " +
            "join u.company c " +
            "where c.id = :companyId " +
            "and " +
            "u.totalScore = (select max(u2.totalScore) from User u2 join u2.company c2 where c2.id = :companyId and u2.totalScore != 0)"
    )
    Optional<User> findCompanyFirstRankUser(Long companyId);

    @Query(value = "select new ssafy.sorhy.dto.user.UserRankInfoDto(count(u), (select count(u)+1 from User u where u.totalScore > (select u2.totalScore from User u2 where u2.nickname = :nickname))) from User u")
    UserRankInfoDto findUserRankInfo(String nickname);

    @Query("select new ssafy.sorhy.dto.user.UserEachGameScore(r.gameTitle, r.score) " +
            "from Ranking r " +
            "join r.user u " +
            "where u.nickname = :nickname")
    List<UserEachGameScore> findEachGameTopScore(String nickname);
}
