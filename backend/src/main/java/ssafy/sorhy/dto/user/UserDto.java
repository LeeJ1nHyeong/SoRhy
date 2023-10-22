package ssafy.sorhy.dto.user;

import lombok.Builder;
import lombok.Data;
import ssafy.sorhy.entity.User;
import ssafy.sorhy.entity.gameresult.GameResult;

import java.util.List;

@Data
public class UserDto {

    @Data
    public static class joinReq {

        private String email;
        private String password;
        private String nickname;

        public User toEntity() {

            return User.builder()
                    .email(this.email)
                    .password(this.password)
                    .nickname(this.nickname)
                    .build();
        }
    }

    @Builder
    @Data
    public static class joinRes {

        private String nickname;
        private int totalScore;
    }

    @Data
    @Builder
    public static class findRes {

        private String nickname;
        private int totalScore;
        private List<GameResult> gameResults;

    }
}
