package com.backend.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.server.entity.Notifications;
import com.backend.server.entity.User;
import com.backend.server.repository.NotificationsRepo;

@Service
public class NotificationsService {
    @Autowired
    NotificationsRepo nRepo;

    public List<Notifications> GetNotificationByUser(User Client) {
        List<Notifications> notifs = nRepo.findBySeenAndClient(false,Client);
        nRepo. updateSeenByClient(true,Client);
        return notifs;
    }
    public void AddNotification(User Client,String message){
        Notifications n = new Notifications();
        n.setClient(Client);
        n.setMessage(message);
        n.setSeen(false);
        nRepo.save(n);
    }

}
