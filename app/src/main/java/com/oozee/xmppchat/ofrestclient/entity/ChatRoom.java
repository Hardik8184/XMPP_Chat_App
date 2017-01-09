package com.oozee.xmppchat.ofrestclient.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oozee.xmppchat.ofrestclient.entity.wrapper.AdminGroups;
import com.oozee.xmppchat.ofrestclient.entity.wrapper.Admins;
import com.oozee.xmppchat.ofrestclient.entity.wrapper.BroadcastPresenceRoles;
import com.oozee.xmppchat.ofrestclient.entity.wrapper.MemberGroups;
import com.oozee.xmppchat.ofrestclient.entity.wrapper.Members;
import com.oozee.xmppchat.ofrestclient.entity.wrapper.OutcastGroups;
import com.oozee.xmppchat.ofrestclient.entity.wrapper.Outcasts;
import com.oozee.xmppchat.ofrestclient.entity.wrapper.OwnerGroups;
import com.oozee.xmppchat.ofrestclient.entity.wrapper.Owners;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatRoom implements Serializable {
    private AdminGroups adminGroups;
    private Admins admins;
    private BroadcastPresenceRoles broadcastPresenceRoles;
    private boolean canAnyoneDiscoverJID;
    private boolean canChangeNickname;
    private boolean canOccupantsChangeSubject;
    private boolean canOccupantsInvite;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", shape = Shape.STRING, timezone = "Asia/Kolkata")
    private Date creationDate;
    private String description;
    private boolean logEnabled;
    private boolean loginRestrictedToNickname;
    private int maxUsers;
    private MemberGroups memberGroups;
    private Members members;
    private boolean membersOnly;
    private boolean moderated;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", shape = Shape.STRING, timezone = "Asia/Kolkata")
    private Date modificationDate;
    private String naturalName;
    private OutcastGroups outcastGroups;
    private Outcasts outcasts;
    private OwnerGroups ownerGroups;
    private Owners owners;
    private String password;
    private boolean persistent;
    private boolean publicRoom;
    private boolean registrationEnabled;
    private String roomName;
    private String subject;

    public ChatRoom(String roomName, String naturalName, String description) {
        this.naturalName = naturalName;
        this.roomName = roomName;
        this.description = description;
    }

    public String getNaturalName() {
        return this.naturalName;
    }

    public void setNaturalName(String naturalName) {
        this.naturalName = naturalName;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getMaxUsers() {
        return this.maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return this.modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public boolean isPersistent() {
        return this.persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public boolean isPublicRoom() {
        return this.publicRoom;
    }

    public void setPublicRoom(boolean publicRoom) {
        this.publicRoom = publicRoom;
    }

    public boolean isRegistrationEnabled() {
        return this.registrationEnabled;
    }

    public void setRegistrationEnabled(boolean registrationEnabled) {
        this.registrationEnabled = registrationEnabled;
    }

    public boolean isCanAnyoneDiscoverJID() {
        return this.canAnyoneDiscoverJID;
    }

    public void setCanAnyoneDiscoverJID(boolean canAnyoneDiscoverJID) {
        this.canAnyoneDiscoverJID = canAnyoneDiscoverJID;
    }

    public boolean isCanOccupantsChangeSubject() {
        return this.canOccupantsChangeSubject;
    }

    public void setCanOccupantsChangeSubject(boolean canOccupantsChangeSubject) {
        this.canOccupantsChangeSubject = canOccupantsChangeSubject;
    }

    public boolean isCanOccupantsInvite() {
        return this.canOccupantsInvite;
    }

    public void setCanOccupantsInvite(boolean canOccupantsInvite) {
        this.canOccupantsInvite = canOccupantsInvite;
    }

    public boolean isCanChangeNickname() {
        return this.canChangeNickname;
    }

    public void setCanChangeNickname(boolean canChangeNickname) {
        this.canChangeNickname = canChangeNickname;
    }

    public boolean isLogEnabled() {
        return this.logEnabled;
    }

    public void setLogEnabled(boolean logEnabled) {
        this.logEnabled = logEnabled;
    }

    public boolean isLoginRestrictedToNickname() {
        return this.loginRestrictedToNickname;
    }

    public void setLoginRestrictedToNickname(boolean loginRestrictedToNickname) {
        this.loginRestrictedToNickname = loginRestrictedToNickname;
    }

    public boolean isMembersOnly() {
        return this.membersOnly;
    }

    public void setMembersOnly(boolean membersOnly) {
        this.membersOnly = membersOnly;
    }

    public boolean isModerated() {
        return this.moderated;
    }

    public void setModerated(boolean moderated) {
        this.moderated = moderated;
    }

    public BroadcastPresenceRoles getBroadcastPresenceRoles() {
        return this.broadcastPresenceRoles;
    }

    public void setBroadcastPresenceRoles(BroadcastPresenceRoles broadcastPresenceRoles) {
        this.broadcastPresenceRoles = broadcastPresenceRoles;
    }

    public Owners getOwners() {
        return this.owners;
    }

    public void setOwners(Owners owners) {
        this.owners = owners;
    }

    public Members getMembers() {
        return this.members;
    }

    public void setMembers(Members members) {
        this.members = members;
    }

    public Outcasts getOutcasts() {
        return this.outcasts;
    }

    public void setOutcasts(Outcasts outcasts) {
        this.outcasts = outcasts;
    }

    public Admins getAdmins() {
        return this.admins;
    }

    public void setAdmins(Admins admins) {
        this.admins = admins;
    }

    public OwnerGroups getOwnerGroups() {
        return this.ownerGroups;
    }

    public void setOwnerGroups(OwnerGroups ownerGroups) {
        this.ownerGroups = ownerGroups;
    }

    public AdminGroups getAdminGroups() {
        return this.adminGroups;
    }

    public void setAdminGroups(AdminGroups adminGroups) {
        this.adminGroups = adminGroups;
    }

    public MemberGroups getMemberGroups() {
        return this.memberGroups;
    }

    public void setMemberGroups(MemberGroups memberGroups) {
        this.memberGroups = memberGroups;
    }

    public OutcastGroups getOutcastGroups() {
        return this.outcastGroups;
    }

    public void setOutcastGroups(OutcastGroups outcastGroups) {
        this.outcastGroups = outcastGroups;
    }
}
