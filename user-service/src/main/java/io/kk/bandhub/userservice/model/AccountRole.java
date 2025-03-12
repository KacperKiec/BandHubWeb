package io.kk.bandhub.userservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "account_role")
public class AccountRole {
    @EmbeddedId
    private AccountRoleId id;

    @MapsId("accountId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public AccountRole(Account account, Role role) {
        this.account = account;
        this.role = role;
        AccountRoleId id = new AccountRoleId();
        id.setAccountId(account.getId());
        id.setRoleId(role.getId());
        setId(id);
    }

    public AccountRole() {

    }
}