package com.project.jukir.models;

import java.io.Serializable;

public class ListLocation implements Serializable {

    public int id;
    public int user_id;
    public int lokasi_id;
    public Object lokasi_lantai_id;
    public Object lokasi_lantai_parkir_id;
    public Object metode_pembayaran_id;
    public String kode_booking;
    public String masuk;
    public String keluar;
    public int total_harga;
    public int status;
    public String created_at;
    public String updated_at;
    public Object deleted_at;
    public LokasiLantai lokasi_lantai;
    public LokasiLantaiParkir lokasi_lantai_parkir;
    public DataLocation lokasi;
    public PaymentMethod metode_pembayaran;
    public User user;
    public Employee employee;
}
