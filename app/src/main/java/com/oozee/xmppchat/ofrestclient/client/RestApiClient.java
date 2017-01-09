package com.oozee.xmppchat.ofrestclient.client;

import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.oozee.xmppchat.ofrestclient.entity.Account;
import com.oozee.xmppchat.ofrestclient.entity.AuthenticationToken;
import com.oozee.xmppchat.ofrestclient.entity.ChatRoom;
import com.oozee.xmppchat.ofrestclient.entity.ChatRooms;
import com.oozee.xmppchat.ofrestclient.entity.Group;
import com.oozee.xmppchat.ofrestclient.entity.Groups;
import com.oozee.xmppchat.ofrestclient.entity.Message;
import com.oozee.xmppchat.ofrestclient.entity.Occupants;
import com.oozee.xmppchat.ofrestclient.entity.Participants;
import com.oozee.xmppchat.ofrestclient.entity.Property;
import com.oozee.xmppchat.ofrestclient.entity.Roster;
import com.oozee.xmppchat.ofrestclient.entity.Rosters;
import com.oozee.xmppchat.ofrestclient.entity.Sessions;
import com.oozee.xmppchat.ofrestclient.entity.Statistics;
import com.oozee.xmppchat.ofrestclient.entity.SystemProperties;
import com.oozee.xmppchat.ofrestclient.entity.User;
import com.oozee.xmppchat.ofrestclient.entity.Users;
import com.oozee.xmppchat.ofrestclient.entity.wrapper.UserGroup;

import org.json.JSONObject;

public class RestApiClient {
    private static RestApiClient instance;
    private String baseUrl;
    private Context context;
    private Gson gson;
    private ObjectMapper mapper;
    private OnFailure onFailure;
    private OnSuccess onSuccess;
    private AuthenticationToken token;

    public RestApiClient(Context context, Account account) {
        this.context = context;
        this.token = account.getAuthenticationToken();
        this.baseUrl = "http://" + account.getHost() + ":" + account.getPort() + "/plugins/restapi/v1/";
        this.gson = new Gson();
        this.mapper = getConfiguredMapper();
    }

    private RestApiClient(Context context) {
        this.context = context.getApplicationContext();
    }

    static RestApiClient instance(Context context) {
        if (instance != null) {
            return instance;
        }
        RestApiClient restApiClient = new RestApiClient(context);
        instance = restApiClient;
        return restApiClient;
    }

    private ObjectMapper getConfiguredMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, true);
        return mapper;
    }

    public RestApiClient account(Account account) {

        System.out.println("Auth Token --> " + account.getAuthenticationToken());

        this.token = account.getAuthenticationToken();
        this.baseUrl = "http://" + account.getHost() + ":" + account.getPort() + "/plugins/restapi/v1/";
        this.gson = new Gson();
        this.mapper = getConfiguredMapper();
        return this;
    }

    public RestApiClient success(OnSuccess onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public RestApiClient failure(OnFailure onFailure) {
        this.onFailure = onFailure;
        return this;
    }

    public void getUsers(Listener<Users> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(0, this.baseUrl + "users/", null, listener, error, Users.class, this.token));
    }

    public void getUsers(JSONObject params, Listener<Users> listener, ErrorListener error) {
        String str;
        String str2 = this.baseUrl + "users/";
        if (params == null) {
            str = null;
        } else {
            str = params.toString();
        }
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(0, str2, str, listener, error, Users.class, this.token));
    }

    public void getUser(String username, Listener<User> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(0, this.baseUrl + "users/" + username, null, listener, error, User.class, this.token));
    }

    public void createUser(User user, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(1, this.baseUrl + "users/", this.gson.toJson(user), listener, error, Status.class, this.token));
    }

    public void updateUser(User user, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(2, this.baseUrl + "users/" + user.getUsername(), this.gson.toJson(user), listener, error, Status.class, this.token));
    }

    public void deleteUser(String username, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(3, this.baseUrl + "users/" + username, null, listener, error, Status.class, this.token));
    }

    public void getGroups(Listener<Groups> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(0, this.baseUrl + "groups/", null, listener, error, Groups.class, this.token));
    }

    public void getGroup(String groupName, Listener<Group> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(0, this.baseUrl + "groups/" + groupName, null, listener, error, Group.class, this.token));
    }

    public void createGroup(Group group) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(1, this.baseUrl + "groups/", this.gson.toJson(group), new C04291(), new C04302(), Status.class, this.token));
    }

    public void createGroup(Group group, Listener<Status> listener, ErrorListener errorListener) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(1, this.baseUrl + "groups/", this.gson.toJson(group), listener, errorListener, Status.class, this.token));
    }

    public void updateGroup(Group group, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(2, this.baseUrl + "groups/" + group.getName(), this.gson.toJson(group), listener, error, Status.class, this.token));
    }

    public void deleteGroup(String groupName, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(3, this.baseUrl + "groups/" + groupName, null, listener, error, Status.class, this.token));
    }

    public void getUserGroups(String userName, Listener<UserGroup> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(0, this.baseUrl + "users/" + userName + "/groups", null, listener, error, UserGroup.class, this.token));
    }

    public void addUserToGroups(String userName, UserGroup groups, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(1, this.baseUrl + "users/" + userName + "/groups", this.gson.toJson(groups), listener, error, Status.class, this.token));
    }

    public void addUserToGroup(String userName, String groupName, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(1, this.baseUrl + "users/" + userName + "/groups/" + groupName, null, listener, error, Status.class, this.token));
    }

    public void deleteUserFromGroups(String userName, UserGroup groups, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(3, this.baseUrl + "users/" + userName + "/groups", this.gson.toJson(groups), listener, error, Status.class, this.token));
    }

    public void deleteUserFromGroup(String userName, String groupName, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(3, this.baseUrl + "users/" + userName + "/groups/" + groupName, null, listener, error, Status.class, this.token));
    }

    public void lockoutUser(String userName, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(1, this.baseUrl + "lockouts/" + userName, null, listener, error, Status.class, this.token));
    }

    public void unlockUser(String userName, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(3, this.baseUrl + "lockouts/" + userName, null, listener, error, Status.class, this.token));
    }

    public void getUserRoster(String userName, Listener<Rosters> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(0, this.baseUrl + "users/" + userName + "/roster", null, listener, error, Rosters.class, this.token));
    }

    public void addUserToRoster(String userName, Roster rosterItemEntity, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(1, this.baseUrl + "users/" + userName + "/roster", this.gson.toJson(rosterItemEntity), listener, error, Status.class, this.token));
    }

    public void deleteFromRoster(String userName, String jid, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(3, this.baseUrl + "users/" + userName + "/roster" + jid, null, listener, error, Status.class, this.token));
    }

    public void updateRosterEntry(String userName, Roster rosterItemEntity, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(2, this.baseUrl + "users/" + userName + "/roster/" + rosterItemEntity.getJid(), this.gson.toJson(rosterItemEntity), listener, error, Status.class, this.token));
    }

    public void getChatRooms(JSONObject params, Listener<ChatRooms> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(0, this.baseUrl + "chatrooms/", params == null ? null : params.toString(), listener, error, ChatRooms.class, this.token));
    }

    public void getChatRoom(String roomName, Listener<ChatRoom> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(0, this.baseUrl + "chatrooms/" + roomName, null, listener, error, ChatRoom.class, this.token));
    }

    public void getChatRooms(Listener<ChatRooms> listener, ErrorListener error) {
        getChatRooms(null, listener, error);
    }

    public void getChatRoomParticipants(String roomName, Listener<Participants> listener, ErrorListener error) {
        Listener<Participants> listener2 = listener;
        ErrorListener errorListener = error;
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(0, this.baseUrl + ("chatrooms/" + roomName + "/participants"), null, listener2, errorListener, Participants.class, this.token));
    }

    public void getChatRoomOccupants(String roomName, Listener<Occupants> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(0, this.baseUrl + "chatrooms/" + roomName + "/occupants", null, listener, error, Occupants.class, this.token));
    }

    public void createChatRoom(ChatRoom room, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(1, this.baseUrl + "chatrooms/", this.gson.toJson(room), listener, error, Status.class, this.token));
    }

    public void deleteChatRoom(String chatRoomName, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(3, this.baseUrl + "chatrooms/" + chatRoomName, null, listener, error, Status.class, this.token));
    }

    public void updateChatRoom(ChatRoom room, Listener<Status> listener, ErrorListener error) {
        String params = null;
        try {
            params = this.mapper.writeValueAsString(room);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(2, this.baseUrl + "chatrooms/" + room.getRoomName(), params, listener, error, Status.class, this.token));
    }

    public void addUserToChatRoom(String chatRoomName, String userName, String role, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(1, this.baseUrl + "chatrooms/" + chatRoomName + "/" + role + "/" + userName, null, listener, error, Status.class, this.token));
    }

    public void addGroupToChatRoom(String chatRoomName, String group, String role, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(1, this.baseUrl + "chatrooms/" + chatRoomName + "/" + role + "/group/" + group, null, listener, error, Status.class, this.token));
    }

    public void deleteUserFromChatRoom(String chatRoomName, String groupName, String role, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(3, this.baseUrl + "chatrooms/" + chatRoomName + "/" + role + "/" + groupName, null, listener, error, Status.class, this.token));
    }

    public void deleteGroupFromChatRoom(String chatRoomName, String groupName, String role, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(3, this.baseUrl + "chatrooms/" + chatRoomName + "/" + role + "/group/" + groupName, null, listener, error, Status.class, this.token));
    }

    public void getSessions(Listener<Sessions> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(0, this.baseUrl + "sessions/", null, listener, error, Sessions.class, this.token));
    }

    public void getSessions(String username, Listener<Sessions> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(0, this.baseUrl + "sessions/" + username, null, listener, error, Sessions.class, this.token));
    }

    public void closeSessions(String username, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(3, this.baseUrl + "sessions/" + username, null, listener, error, Status.class, this.token));
    }

    public void broadcast(Message message, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(1, this.baseUrl + "messages/users/", this.gson.toJson(message), listener, error, Status.class, this.token));
    }

    public void getProperties(Listener<SystemProperties> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(0, this.baseUrl + "system/properties/", null, listener, error, SystemProperties.class, this.token));
    }

    public void getProperty(String propertyName, Listener<Property> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(0, this.baseUrl + "system/properties/" + propertyName, null, listener, error, Property.class, this.token));
    }

    public void createProperty(Property property, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(1, this.baseUrl + "system/properties/", this.gson.toJson(property), listener, error, Status.class, this.token));
    }

    public void deleteProperty(String propertyName, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(3, this.baseUrl + "system/properties/" + propertyName, null, listener, error, Status.class, this.token));
    }

    public void updateProperty(Property property, Listener<Status> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(2, this.baseUrl + "system/properties/" + property.getKey(), this.gson.toJson(property), listener, error, Status.class, this.token));
    }

    public void getStatistics(Listener<Statistics> listener, ErrorListener error) {
        Volley.newRequestQueue(this.context).add(new ApiRequest<>(0, this.baseUrl + "system/statistics/sessions", null, listener, error, Statistics.class, this.token));
    }

    /* renamed from: com.oozee.ofrestclient.client.RestApiClient.1 */
    private class C04291 implements Listener<Status> {
        C04291() {
        }

        @Override
        public void onResponse(Object mTag, Status response) {
            if (RestApiClient.this.onSuccess != null) {
                RestApiClient.this.onSuccess.success(response);
            }
        }
    }

    /* renamed from: com.oozee.ofrestclient.client.RestApiClient.2 */
    private class C04302 implements ErrorListener {
        C04302() {
        }

        public void onErrorResponse(VolleyError error) {
            if (RestApiClient.this.onFailure != null) {
                RestApiClient.this.onFailure.failure(new Reason(error));
            }
        }
    }
}
