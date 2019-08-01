package com.example.biblio.transfer;

import com.example.biblio.dto.UserDTO;
import com.example.biblio.model.User;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDTO(User user);

    List<UserDTO> toUserDTOs(List<User> products);

    User toUser(User userDto);

}