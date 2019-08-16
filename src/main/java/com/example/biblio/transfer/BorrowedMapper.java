package com.example.biblio.transfer;

import com.example.biblio.dto.BorrowedDTO;
import com.example.biblio.model.Borrowed;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BorrowedMapper {

    BorrowedDTO toBorrowedDTO(Borrowed book);

    List<BorrowedDTO> toBorrowedDTOs(List<Borrowed> borroweds);

    Borrowed toBorrowed(BorrowedDTO borrowedDTO);

}