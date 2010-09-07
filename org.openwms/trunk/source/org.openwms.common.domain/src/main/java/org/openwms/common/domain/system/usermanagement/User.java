/*
 * openwms.org, the Open Warehouse Management System.
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * openwms.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software. If not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.openwms.common.domain.system.usermanagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * An <code>User</code> represents the user of the system.
 * 
 * @author <a href="mailto:openwms@googlemail.com">Heiko Scherrer</a>
 * @version $Revision$
 * @since 0.1
 */
@Entity
@Table(name = "APP_USER")
@NamedQueries( { @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.findAllOrdered", query = "SELECT u FROM User u ORDER BY u.username"),
        @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = ?1") })
public class User implements Serializable {

    /**
     * The serialVersionUID
     */
    private static final long serialVersionUID = -1116645053773805413L;

    /**
     * Unique technical key.
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    /**
     * Unique identifier of this <code>User</code>.
     */
    @Column(name = "USERNAME", unique = true, nullable = false)
    private String username;

    /**
     * <code>true</code> if the <code>User</code> is authenticated by an
     * external system.
     */
    @Column(name = "EXTERN")
    private boolean extern = false;

    /**
     * Date of last password change.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_PASSWORD_CHANGE")
    private Date lastPasswordChange;

    /**
     * <code>true</code> if this <code>User</code> is locked and has no
     * permission to login.
     */
    @Column(name = "LOCKED")
    private boolean locked = false;

    /**
     * Current password of the <code>User</code>.
     */
    @Column(name = "PASSWORD")
    private String password;

    /**
     * <code>true</code> if this <code>User</code> is enabled.
     */
    @Column(name = "ENABLED")
    private boolean enabled = true;

    /**
     * Date when the account expires. After expiration, the <code>User</code>
     * cannot login anymore.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPIRATION_DATE")
    private Date expirationDate;

    /**
     * <code>User</code>s fullname. Doesn't have to be unique.
     */
    @Column(name = "FULLNAME")
    private String fullname;

    /**
     * Version field.
     */
    @Version
    @Column(name = "C_VERSION")
    private long version;

    /* ------------------- collection mapping ------------------- */
    /**
     * More detail information about the <code>User</code>.
     */
    @Embedded
    private UserDetails userDetails = new UserDetails();

    /**
     * List of all granted {@link Role}s to this <code>User</code>.
     */
    @ManyToMany(mappedBy = "users")
    private List<Role> roles = new ArrayList<Role>();

    /**
     * Password history of this <code>User</code>.
     */
    @OneToMany(mappedBy = "user")
    @JoinTable(name = "APP_USER_PASSWORD", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "PASSWORD_ID"))
    private List<UserPassword> passwords = new ArrayList<UserPassword>();

    /**
     * All {@link Preference}s of this <code>User</code>.
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "APP_USER_PREFERENCE", joinColumns = @JoinColumn(name = "ROLE_ID"), inverseJoinColumns = @JoinColumn(name = "PREF_ID"))
    private Set<Preference> preferences = new HashSet<Preference>();

    /* ----------------------------- methods ------------------- */
    /**
     * Accessed by persistence provider.
     */
    @SuppressWarnings("unused")
    private User() {}

    /**
     * Create a new User with an username.
     * 
     * @param username
     *            The unique name of the user
     */
    public User(String username) {
        this.username = username;
    }

    /**
     * Return the technical key.
     * 
     * @return The unique technical key
     */
    public Long getId() {
        return id;
    }

    /**
     * Checks if the instance is transient.
     * 
     * @return true if the entity is not present on the persistent storage,
     *         otherwise false.
     */
    public boolean isNew() {
        return this.id == null;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Is this <code>User</code> an user authenticated through an external
     * system?
     * 
     * @return <code>true</code> if this <code>User</code> was authenticated by
     *         an external system, otherwise <code>false</code>.
     */
    public boolean isExternalUser() {
        return this.extern;
    }

    /**
     * Set this <code>User</code> as authenticated through an external system.
     * 
     * @param extern
     *            <code>true</code> if this <code>User</code> was authenticated
     *            by an external system, otherwise <code>false</code>.
     */
    public void setExternalUser(boolean externalUser) {
        this.extern = externalUser;
    }

    public Date getLastPasswordChange() {
        return this.lastPasswordChange;
    }

    public void setLastPasswordChange(Date lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public List<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getFullname() {
        return this.fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * Returns a List of the last used passwords.
     * 
     * @return A List of the last used passwords.
     */
    public List<UserPassword> getPasswords() {
        return this.passwords;
    }

    /**
     * Set a List of {@link UserPassword}s to this <code>User</code>. Already
     * existing {@link UserPassword}s are removed.
     * 
     * @param passwords
     *            A List of {@link UserPassword}s
     */
    public void setPasswords(List<UserPassword> passwords) {
        this.passwords = passwords;
    }

    /**
     * @return The userDetails
     */
    public UserDetails getUserDetails() {
        return userDetails;
    }

    /**
     * @param userDetails
     *            The userDetails to set
     */
    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    /**
     * Get all {@link Preference}s of this <code>User</code>.
     * 
     * @return A Set of all {@link Preference}s
     */
    public Set<Preference> getPreferences() {
        return preferences;
    }

    /**
     * Set all {@link Preference}s of this <code>User</code>. Already existing
     * {@link Preference}s are removed.
     * 
     * @param preferences
     *            A Set of {@link Preference}s to set
     */
    public void setPreferences(Set<Preference> preferences) {
        this.preferences = preferences;
    }

    /**
     * JPA optimistic locking.
     * 
     * @return The version field
     */
    public long getVersion() {
        return this.version;
    }
}
