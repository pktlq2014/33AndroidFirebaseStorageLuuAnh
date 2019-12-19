package com.example.a33androidfirebasestorageluuanh;

public class HinhAnh
{
    public String tenAnh;
    public String linkAnh;

    public HinhAnh() {
    }

    public HinhAnh(String tenAnh, String linkAnh) {
        this.tenAnh = tenAnh;
        this.linkAnh = linkAnh;
    }

    public String getTenAnh() {
        return tenAnh;
    }

    public void setTenAnh(String tenAnh) {
        this.tenAnh = tenAnh;
    }

    public String getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        this.linkAnh = linkAnh;
    }
}
