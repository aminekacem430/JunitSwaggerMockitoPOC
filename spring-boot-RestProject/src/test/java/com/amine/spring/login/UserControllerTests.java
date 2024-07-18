package com.amine.spring.login;

import com.amine.spring.login.controllers.UserController;
import com.amine.spring.login.models.User;
import com.amine.spring.login.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTests {
  @MockBean
  private UserRepository userRepository;
 // private  List<User> baseAssets = new ArrayList<>();


  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldCreateUser() throws Exception {
    User user = new User("amino2", "Spring2@gmail.com", "passcodepwd");

    mockMvc.perform(post("/api/user/adduser").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isCreated())
        .andDo(print());
    System.out.println("user id : "+user.getId());
  }

  @Test
  void shouldReturnUser() throws Exception {
    long id = 1L;
    User user = new User(id, "amino", "aminos@gmail.fr","pwd2");

    when(userRepository.findById((int)id)).thenReturn(Optional.of(user));
    mockMvc.perform(get("/api/user/{userid}", id)).andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.username").value(user.getUsername()))
        .andExpect(jsonPath("$.email").value(user.getEmail()))
        .andExpect(jsonPath("$.password").value(user.getPassword()))
        .andDo(print());
  }


  @Test
  void shouldReturnListOfTutorials() throws Exception {
    List<User> users = new ArrayList<>(  Arrays.asList((new User(1L, "kacem", "aaaa@gmil.com", "PASSc0$$ee"))));

    when(userRepository.findAll()).thenReturn(users);
    mockMvc.perform(get("/api/user/all"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(users.size()))
            .andDo(print());
  }


  @Test
  void shouldReturnNotFoundUser() throws Exception {
    long id = 1L;

    when(userRepository.findById((int)id)).thenReturn(Optional.empty());
    mockMvc.perform(get("/api/user/{id}", id))
         .andExpect(status().isNotFound())
         .andDo(print());
  }



  @Test
  void shouldUpdateUser() throws Exception {
    long id = 1L;

    User user = new User(id, "amino", "amino@live.com", "passnewPWD@@");
    User updateduser = new User(id, "user5", "user5@yahoo.fr", "passco$$detrue");

    when(userRepository.findById((int)id)).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenReturn(updateduser);

    mockMvc.perform(put("/api/user/{id}", id).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateduser)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(updateduser.getUsername()))
        .andExpect(jsonPath("$.email").value(updateduser.getEmail()))
        .andExpect(jsonPath("$.password").value(updateduser.getPassword()))
        .andDo(print());
  }
  

  @Test
  void shouldDeleteUser() throws Exception {
    long id = 1L;

    doNothing().when(userRepository).deleteById((int)id);
    mockMvc.perform(delete("/api/user/{id}", id))
         .andExpect(status().isNoContent())
         .andDo(print());
  }

 /* @ParameterizedTest
  @CsvFileSource(resources = {"/mock_File.csv"}, numLinesToSkip = 1)
  public void whenAssetsQueried_thenDetailsAsExpected(String queriedBy, String queryValue) {
    List<User> objAssets = new ArrayList<>();

    boolean isParamTitle = queriedBy.equals("Amine");

    if (isParamTitle) {
    //  Mockito.when(this.userRepository.findByUsername(Mockito.any(String.class)))
            //  .thenReturn(this.baseAssets);

      objAssets = this.userRepository.findByUsername(queryValue).stream().toList();
    }

    Assertions.assertEquals(this.baseAssets.size(), objAssets.size());
    for (int idx = 0; idx < this.baseAssets.size(); idx++) {
      Assertions.assertEquals(this.baseAssets.get(idx).getUsername(), objAssets.get(idx).getUsername());
    }
  }*/

}
