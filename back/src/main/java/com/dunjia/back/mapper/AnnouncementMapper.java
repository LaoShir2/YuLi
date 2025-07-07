package com.dunjia.back.mapper;

import com.dunjia.back.pojo.Announcement;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnnouncementMapper {

    @Select("select * from announcements")
    List<Announcement> getAllAnnouncement();

    @Select("select * from announcements where id = #{id}")
    Announcement getAnnouncementById(Integer id);

    @Insert("insert into announcements (title, content, createTime) values (#{title}, #{content}, #{createTime})")
    void addAnnouncement(Announcement announcement);

    @Delete("delete from announcements where id = #{id}")
    void deleteAnnouncementById(Integer id);
}
