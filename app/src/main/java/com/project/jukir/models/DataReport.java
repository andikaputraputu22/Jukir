package com.project.jukir.models;

import java.io.Serializable;
import java.util.List;

public class DataReport implements Serializable {

    public String url;
    public int total_mobil;
    public int total_trasaksi;
    public int total_penghasilan;
    public List<ListLocation> list;
}
