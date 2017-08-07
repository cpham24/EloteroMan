package edu.calstatela.cpham24.eloteroman.DisplayActivities.data;

import java.util.ArrayList;

/**
 * Created by Ambrosio on 8/1/2017.
 */

public class user {

    String id;
    String username;
    String name;
    String avatar;
    String isPublic;
    ArrayList<String> favoriteFoodCarts;

    public user(String id, String username, String name, String avatar, String isPublic, ArrayList<String> favoriteFoodCarts) {
        this.id=id;
        this.username = username;
        this.name = name;
        this.avatar = avatar;
        this.isPublic = isPublic;
        this.favoriteFoodCarts = favoriteFoodCarts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getFavoriteFoodCarts() {
        return favoriteFoodCarts;
    }

    public void setFavoriteFoodCarts(ArrayList<String> favoriteFoodCarts) {
        this.favoriteFoodCarts = favoriteFoodCarts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }



}
