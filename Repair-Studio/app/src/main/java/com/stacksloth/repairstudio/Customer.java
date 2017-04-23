package com.stacksloth.repairstudio;

/**
 * Created by Daniel Slapelis on 4/15/2017.
 */

public class Customer {
    private int mId;
    private int mUser;
    private String mName;
    private String mEmail;
    private String mPhone;
    private float mSpent;

    public Customer(int id, int user, String name, String email, String phone, float spent)
    {
        mId = id;
        mUser = user;
        mName = name;
        mEmail = email;
        mPhone = phone;
        mSpent = spent;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmUser() {
        return mUser;
    }

    public void setmUser(int mUser) {
        this.mUser = mUser;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public float getmSpent() {
        return mSpent;
    }

    public void setmSpent(float mSpent) {
        this.mSpent = mSpent;
    }
}
