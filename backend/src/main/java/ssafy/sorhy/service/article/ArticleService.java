package ssafy.sorhy.service.article;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ssafy.sorhy.dto.article.ArticleDto;
import ssafy.sorhy.entity.article.Article;
import ssafy.sorhy.entity.article.SearchCond;
import ssafy.sorhy.entity.user.User;
import ssafy.sorhy.exception.NotValidUserException;
import ssafy.sorhy.repository.article.ArticleRepository;
import ssafy.sorhy.repository.user.UserRepository;
import ssafy.sorhy.service.s3.S3UploadService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final S3UploadService s3UploadService;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleDto.basicRes save(String nickname, MultipartFile file, ArticleDto.saveReq data) throws IOException {

        String imgUrl;
        User user = userRepository.findByNickname(nickname);

        if (file != null) {
            imgUrl = s3UploadService.uploadFile(file);
        } else {
            imgUrl = null;
        }

        Article article = data.toEntity(user,imgUrl);
        articleRepository.save(article);

        return article.toBasicRes();
    }

    public List<ArticleDto.basicRes> findAll() {

        List<Article> articles = articleRepository.findAll();

        return articles.stream()
                .map(Article::toBasicRes)
                .collect(Collectors.toList());
    }

    public ArticleDto.detailRes findById(Long articleId) {

        Article article = articleRepository.findById(articleId).get();
        return article.toDetailRes();
    }

    public String update(Long articleId, String nickname, ArticleDto.saveReq request) {

        Article article = articleRepository.findById(articleId).get();

        if(article.getUser().getNickname().equals(nickname)) {
            article.update(request);
            return "ok";
        } else {
            throw new NotValidUserException("글 작성자가 아닙니다.");
        }
    }

    public List<ArticleDto.basicRes> searchArticle(ArticleDto.searchReq request) {

        String word = request.getWord();

        if (SearchCond.NONE == (SearchCond.valueOf(request.getSearchCond()))) {

            return toDtoList(articleRepository.findByTitleContainingOrContentContainingOrderByIdDesc(word, word));
        }

        if (SearchCond.TITLE == (SearchCond.valueOf(request.getSearchCond()))) {

            return toDtoList(articleRepository.findByTitleContainingOrderByIdDesc(word));
        }

        if (SearchCond.NICKNAME == (SearchCond.valueOf(request.getSearchCond()))) {

            return toDtoList(articleRepository.findByNicknameOrderByDesc(word));
        }

        if (SearchCond.CONTENT == (SearchCond.valueOf(request.getSearchCond()))) {

            return toDtoList(articleRepository.findByContentContaining(word));
        }

        throw new IllegalStateException("검색 조건이 유효하지 않습니다.");
    }

    private List<ArticleDto.basicRes> toDtoList(List<Article> articleList) {

        return articleList.stream()
                .map(Article::toBasicRes)
                .collect(Collectors.toList());
    }
}
