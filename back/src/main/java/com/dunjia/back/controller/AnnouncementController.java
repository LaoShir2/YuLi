package com.dunjia.back.controller;

import com.dunjia.back.pojo.Announcement;
import com.dunjia.back.pojo.Result;
import com.dunjia.back.service.AnnouncementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/announcements")
    public Result getAllAnnouncement() {
        log.info("Get all announcements");
        List<Announcement> announcements = announcementService.getAllAnnouncement();
        return Result.success(announcements);
    }

    @GetMapping("/announcements/{id}")
    public Result getAnnouncementById(@PathVariable Integer id) {
        log.info("Get announcement by id");
        Announcement announcement = announcementService.getAnnouncementById(id);
        return Result.success(announcement);
    }

    @PostMapping("/announcements")
    public Result addAnnouncement(@RequestBody Announcement announcement) {
        log.info("Add announcement");
        announcementService.addAnnouncement(announcement);
        return Result.success();
    }

    @DeleteMapping("/announcements/{id}")
    public Result deleteAnnouncement(@PathVariable Integer id) {
        log.info("Delete announcement by id");
        announcementService.deleteAnnouncementById(id);
        return Result.success();
    }
}
