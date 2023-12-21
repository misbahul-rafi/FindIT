package com.example.findit;

public class UsersModel {
    private String name;
    private String email;
    private String phone;
    private String nim;
    private String kelas;
    public UsersModel(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public UsersModel(String name, String email, String phone, String nim, String kelas) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.nim = nim;
        this.kelas = kelas;
    }
}
