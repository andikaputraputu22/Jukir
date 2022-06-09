package com.project.jukir.models;

import java.io.Serializable;
import java.util.List;

public class DataWallet implements Serializable {

    public int id;
    public String icon;
    public String nama_metode_pembayaran;
    public Object created_at;
    public Object updated_at;
    public int saldo;
}
