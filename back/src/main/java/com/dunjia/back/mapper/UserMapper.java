package com.dunjia.back.mapper;

import com.dunjia.back.pojo.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from users where name = #{name} and password = #{password}")
    User getByUserNameAndPassword(User user);

    @Select("select * from users")
    List<User> getAllUsers();

    @Select("select * from users where id = #{id}")
    User getUserById(Integer id);

    void addUser(User user);

    void updateUser(User user);

    @Delete("delete from users where id = #{id}")
    void deleteUserById(Integer id);

}
