package com.upc.dentify.iam.domain.model.aggregates;


import com.upc.dentify.iam.domain.model.commands.SignUpCommand;
import com.upc.dentify.iam.domain.model.entities.IdentificationType;
import com.upc.dentify.iam.domain.model.entities.Role;
import com.upc.dentify.iam.domain.model.valueobjects.BirthDate;
import com.upc.dentify.iam.domain.model.valueobjects.Email;
import com.upc.dentify.iam.domain.model.valueobjects.PersonName;
import com.upc.dentify.iam.domain.model.valueobjects.Username;
import com.upc.dentify.iam.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;


@Entity
public class User extends AuditableAbstractAggregateRoot<User> implements UserDetails {

    @Embedded
    @Column(nullable = false)
    private PersonName personName;

    @Embedded
    private Username username;

    @NotBlank
    @Size(min = 8)
    @Column(nullable = false)
    private String password;

    @Embedded
    @Column(nullable = false)
    private BirthDate birthDate;

    @Embedded
    @Column(nullable = false)
    private Email email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "identification_type_id", nullable = false)
    private IdentificationType identificationType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "clinic_id", nullable = false)
    private Long clinicId;


    public User() {}

    public User(SignUpCommand command, String encryptedPassword, IdentificationType type){
        this.personName = new PersonName(command.firstName(), command.lastName());
        this.identificationType = new IdentificationType(command.identificationTypeId());
        this.username = new Username(command.username(), type);
        this.password = encryptedPassword;
        this.birthDate = new BirthDate(command.birthDate());
        this.email = new Email(command.email());
        this.role = new Role(command.roleId());
        this.clinicId = command.clinicId();
    }

    public Role getRole() {
        return this.role;
    }

    public Long getClinicId() {
        return clinicId;
    }

    public PersonName getPersonName() {
        return this.personName;
    }

    public void setPassword(String hashedPassword) {
        this.password = hashedPassword;
    }

    public void setPersonName(PersonName personName) {
        this.personName = personName;
    }


    //user details methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("Authorities for user " + getUsername() + ": " + List.of(role));
        return List.of(role); //return user roles
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String getUsername() {
        return this.username.getValue();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public BirthDate getBirthDate() {
        return this.birthDate;
    }

    public Email getEmail() {
        return this.email;
    }
}
