package com.dunjia.back.service;

import com.dunjia.back.mapper.AnnouncementMapper;
import com.dunjia.back.pojo.Announcement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementMapper announcementMapper;

    public List<Announcement> getAllAnnouncement() {
        return announcementMapper.getAllAnnouncement();
    }

    public Announcement getAnnouncementById(Integer id) {
        return announcementMapper.getAnnouncementById(id);
    }

    public void addAnnouncement(Announcement announcement) {
        announcement.setCreateTime(LocalDateTime.now());
        announcementMapper.addAnnouncement(announcement);
    }

    public void deleteAnnouncementById(Integer id) {
        announcementMapper.deleteAnnouncementById(id);
    }
}
