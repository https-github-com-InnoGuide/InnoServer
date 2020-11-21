package com.example.GuideServer.controller;

import com.example.GuideServer.Entity.User;
import com.example.GuideServer.Event;
import com.example.GuideServer.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class EventController {
    @Autowired
    private UserRepo userRepo;
    Map<String, Event> eventsMap = new HashMap<String, Event>(){
        {
            put("1", new Event("Sport fest", "17.06.2020", "sport", "sport complex", 1));
            put("2",new Event("Food fest", "12.07.2020", "food", "Behetle", 2));
        }
    };
    List<Event> eventList = new ArrayList<Event>(){
        {
            add(new Event("Art-therapy meeting", "05.11.2020", "Emotional Intelligence. The relationship between the Body and the Emotional World.", "ArtSpace", 0));
            add(new Event("AI Journey 2020", "11.11.2020", "Online International Conference on Artificial Intelligence and Data Analysis AI Journey 2020" +
                    "3 days of exciting presentations by recognized world experts in the development and implementation of AI technologies in various areas of business and life.", "Techno park", 1));
            add(new Event("IU Integration Bee", "29.11.2020", "The University is going to join this tradition and start a new IU competition in Math - IU Integration Bee." +
                    "The goal is to solve integrals faster than your opponents. Winners will have prizes." +
                    "Everyone is welcome to the first Integration Bee.", "Innopolis University", 2));
            add(new Event("Inno Stand Up", "09.12.2020", "If you have something to joke about and want to try yourself in front of people, who don't judge bad humor, you're welcome.", "Bar 108", 3));
            add(new Event("Toastmasters Public Speaking", "17.06.2020", "If you want to develop your public speaking skills and meet like-minded people, you should surely come!" +
                    "You can come as a guest and participate in spontaneous speeches or prepare a speech in advance (please do it in advance with event hosts)", "Komunalka", 4));
            add(new Event("Food fest", "15.12.2020", "Go and try different kinds of food", "Behetle", 5));
        }
    };


    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('users:read')")
    @ResponseBody
    public List<User> maine(){
        List<User> user = new ArrayList<User>();
        Iterable<User> users = userRepo.findAll();
        for(User u : users)
            user.add( u);
        return user;
    }


    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('users:write')")
    public String addUser(@RequestBody User user){
        Iterable<User> users= userRepo.findAll();
        for( User u: users){
            if(u.getEmail().equals(user.getEmail()))
                return "such email already exist";
            if(u.getFirstName().equals(user.getFirstName()) && u.getLastName().equals(user.getLastName()))
                return "such user already exist";
        }
        userRepo.save(user);
        return "success";
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('users:write')")
    public void remove(@PathVariable Long id){
        userRepo.deleteById(id);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('users:read')")
    @ResponseBody
    public User getUser(@PathVariable Long id){
        Optional<User> user=  userRepo.findById(id);
        return user.get();
    }



    //@RequestMapping(value = "/events", method = RequestMethod.GET)
    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public Map<String, Event> getEvents() {
        return eventsMap;
    }

    @RequestMapping(value = "/eventsList", method = RequestMethod.GET)
    public List<Event> getEventsList() {
        return eventList;
    }
}
