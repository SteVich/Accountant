package com.example.biblio.security.permission;

import com.example.biblio.exception.ResourceNotFoundException;
import com.example.biblio.model.Borrowed;
import com.example.biblio.model.User;
import com.example.biblio.repository.BorrowedRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BorrowedPermissionEvaluator implements PermissionEvaluator {

    BorrowedRepository borrowedRepository;

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        boolean permit = false;

        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            permit = false;
        }

        Borrowed borrowed = borrowedRepository.findById((Long) targetId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowed", "id", targetId));

        User bookOwner = borrowed.getUser();

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        String principalLogin = userDetails.getUsername();

        if (Objects.equals(bookOwner.getUsername(), principalLogin)) {
            permit = true;
        }

        return permit;
    }

}