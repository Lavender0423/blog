package com.lavender.blog.dao;

import com.lavender.blog.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long>, JpaSpecificationExecutor {
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);

    // only get one
    @Query(value="select * from t_comment c where c.nickname = ?1 limit 0,1",nativeQuery = true)
    Comment findByNickname(String nickname);
}
