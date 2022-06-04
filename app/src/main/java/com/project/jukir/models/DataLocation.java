package com.project.jukir.models;

import java.io.Serializable;
import java.util.List;

public class DataLocation implements Serializable {

    public int id;
    public String gambar;
    public String nama_lokasi;
    public Object created_at;
    public Object updated_at;
    public Object deleted_at;
    public List<LokasiLantai> lokasi_lantai;
}
