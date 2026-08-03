package tomato.com.tomato.security.user;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tomato.com.tomato.model.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails{
    private Long id;
    private String email;
    private String password;
    private Collection<GrantedAuthority> authorities;

    public static CustomUserDetails buildUserDetails(User user) {
        List<GrantedAuthority> authorities = Stream.concat(
            user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())), 
            user.getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermissionName())))
            .collect(Collectors.toList());
        return new CustomUserDetails(
            user.getId(),
            user.getEmail(),
            user.getPassword(),
            authorities
        );
    }

    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
       return password;
    }

    //non-generated methods

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}


// things i learnt.

// we make customUserDetails object instead of using user object in spring security because user object contains
// many others attributes and complex relation so hiberante can mess this up.
// that is why we extract id, email and password and build a custom user.
// we are not using setters because this custom user object should be immutable once it is created

//Declared GrantedAuthoriy authorities obeject and access it in a method making it localthread  .
//  SimpleGrantAuthorities is wrapper which wraps the raw string for GrantedAuthority.
//  It takes raw Roles from db and  translates it to GrantedAuthority wrapper

// GrantedAuthority is an interface which represents a specific privilage, permission etc. Spring Security framework uses this(String based flag) to 
//decided if user has the authority to access request or not.


/*
Stream behaviour
When Stream class or utility is called it doesn't stream the "insides of collection" but "collection" itself so that is 
why .stream() is used again. 

Also since  all roles and perms have to be of type GrantedAuthority, every perm & role is stream, casted to SimpleAuthority , Stream.concat the stream
and collect is used accumulate it and then concate them.

*/