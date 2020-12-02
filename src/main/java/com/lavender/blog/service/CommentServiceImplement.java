package com.lavender.blog.service;

import com.lavender.blog.dao.CommentRepository;
import com.lavender.blog.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImplement implements CommentService {

    @Autowired
    private CommentRepository commentRepository;



    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by(Sort.Direction.ASC,"createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
        return eachComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if(parentCommentId != -1){
            comment.setParentComment(commentRepository.findById(parentCommentId).orElse(null));
        }else{
            comment.setParentComment(null);
        }
        String nickname = comment.getNickname();
        System.out.println(nickname);
//        System.out.println(commentRepository==null);
        Comment temp = commentRepository.findByNickname(nickname);
//        System.out.println(temp==null);
//        System.out.println(temp.toString());
        if(temp==null||temp.getNickname().equals("")||temp.getNickname()==null) {//说明没有此昵称的用户评论过，重新设置头像
            String avatar_link = "https://picsum.photos/id/"+(int)(Math.random()*1084)+"/200/200";
//            System.out.println(avatar_link);
            comment.setAvatar(avatar_link);
        }else{
//            System.out.println(temp.getAvatar());
            comment.setAvatar(temp.getAvatar());
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

//    public static void main(String[] args) {
//        String avatar_link = "https://picsum.photos/id/"+(int)(Math.random()*1084)+"/200/200";
//        System.out.println(avatar_link);
//        avatar_link = "https://picsum.photos/id/"+(int)(Math.random()*1084)+"/200/200";
//        System.out.println(avatar_link);
//        avatar_link = "https://picsum.photos/id/"+(int)(Math.random()*1084)+"/200/200";
//        System.out.println(avatar_link);
//    }
    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     *
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {

        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplyComments();
            for(Comment reply1 : replys1) {
                //循环迭代，找出子代，存放在tempReplys中
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();
    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(Comment comment) {
        tempReplys.add(comment);//顶节点添加到临时存放集合
        if (comment.getReplyComments().size()>0) {
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size()>0) {
                    recursively(reply);
                }
            }
        }
    }

}
