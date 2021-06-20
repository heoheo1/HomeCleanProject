package com.hj.homecleanproject;

public class FamilyItem {
    String name;
    String family_group;
    int image;
    String phone_Number;

    public FamilyItem(String name, String family_group, String phone_Number, int image) {
        this.name = name;
        this.family_group = family_group;
        this.phone_Number = phone_Number;
        this.image = image;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily_group() {
        return family_group;
    }

    public void setFamily_group(String family_group) {
        this.family_group = family_group;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getPhone_Number() {
        return phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        this.phone_Number = phone_Number;
    }
}
