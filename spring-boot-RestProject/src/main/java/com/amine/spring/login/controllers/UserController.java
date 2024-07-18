package com.amine.spring.login.controllers;

import com.amine.spring.login.models.User;
import com.amine.spring.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  private UserRepository userDao;
  @GetMapping("/all")
  public List<User> getAllUsers( ) {
    System.out.println("get all users");
    return userDao.findAll();
  }
  //creating a get mapping that retrieves the detail of a specific user
  @GetMapping("/{userid}")
  private User getUserById(@PathVariable("userid") Integer userid)
  {
    return userDao.getById(userid);
  }

  //creating put mapping that updates the user detail
  @PutMapping("/{id}")
  public ResponseEntity<User> updateTutorial(@PathVariable("id") Integer id, @RequestBody User user) {
    Optional<User> userData = userDao.findById(id);

    if (userData.isPresent()) {
      User  _user = userData.get();
      _user.setUsername(user.getUsername());
      _user.setEmail(user.getEmail());
      _user.setPassword(user.getPassword());
      return new ResponseEntity<>(userDao.save(_user), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") Integer id) {
    try {
      userDao.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @PostMapping("/adduser")
  public ResponseEntity<User> createTutorial(@RequestBody User user) {
    try {
      System.out.println("//////adduser");

      User addedUser = userDao
              .save(new User(user.getUsername(),user.getEmail(), user.getPassword()));
      System.out.println("/////////// added user id:");
      return new ResponseEntity<>(addedUser, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
