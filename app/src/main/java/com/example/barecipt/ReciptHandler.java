package com.example.barecipt;

public class ReciptHandler {
    private int id;
    private String namaResep,lamaMemasak, pilihan, jenis, bahan, langkah;
    private String statusLamaMemasak;

    public ReciptHandler(){
        //
    }

    public ReciptHandler(Integer id, String namaResep, String lamaMemasak, String statusLamaMemasak,
                         String pilihan, String jenis, String bahan, String langkah){
        this.id = id;
        this.namaResep = namaResep;
        this.lamaMemasak = lamaMemasak;
        this.statusLamaMemasak = statusLamaMemasak;
        this.pilihan = pilihan;
        this.jenis = jenis;
        this.bahan = bahan;
        this.langkah = langkah;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaResep() {
        return namaResep;
    }

    public void setNamaResep(String namaResep) {
        this.namaResep = namaResep;
    }

    public String getLamaMemasak() {
        return lamaMemasak;
    }

    public void setLamaMemasak(String lamaMemasak) {
        this.lamaMemasak = lamaMemasak;
    }

    public String getStatusLamaMemasak() {
        return statusLamaMemasak;
    }

    public void setStatusLamaMemasak(String statusLamaMemasak) {
        this.statusLamaMemasak = statusLamaMemasak;
    }

    public String getPilihan() {
        return pilihan;
    }

    public void setPilihan(String pilihan) {
        this.pilihan = pilihan;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getBahan() {
        return bahan;
    }

    public void setBahan(String bahan) {
        this.bahan = bahan;
    }

    public String getLangkah() {
        return langkah;
    }

    public void setLangkah(String langkah) {
        this.langkah = langkah;
    }
}
