package com.lavender.blog.service;

import com.lavender.blog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {
    //保存前端传回type
    Type saveType(Type type);
    //根据id获取type
    Type getType(Long id);

    Type getTypeByName(String name);
    Page<Type> listType(Pageable pageable);
    List<Type> listType();
    List<Type> listTypeTop(Integer size);

    Type updateType(Long id,Type type);

    void deleteType(Long id);

}
