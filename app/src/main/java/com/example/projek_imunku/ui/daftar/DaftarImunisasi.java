package com.example.projek_imunku.ui.daftar;

public class DaftarImunisasi {
    private String name;
    private String age;
    private String type;
    private String desc;
    private String price;

    private Float weight;
    private Float height;
    private Float imt;
    private String date;
    public DaftarImunisasi() {
        // Diperlukan untuk Firebase deserialization
    }

    public DaftarImunisasi(String name, String age, String type, String desc, String price, Float berat, Float tinggi, Float imt, String tanggal) {
        this.name = name;
        this.age = age;
        this.type = type;
        this.desc = desc;
        this.price = price;

        this.weight = weight;
        this.height = height;
        this.imt = imt;
        this.date = date;
    }

    // Getter dan Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public Float getweight() { return weight; }
    public void setweight(Float weight) { this.weight = weight; }

    public Float getheight() { return height; }
    public void setheight(Float height) { this.height = height; }

    public Float getImt() { return imt; }
    public void setImt(Float imt) { this.imt = imt; }

    public String getdate() { return date; }
    public void setdate(String date) { this.date = date; }
}
