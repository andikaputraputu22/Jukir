package com.project.jukir.models;

import java.io.Serializable;
import java.util.List;

public class LokasiLantai implements Serializable {

    public int id;
    public int lokasi_id;
    public String lantai;
    public Object created_at;
    public Object updated_at;
    public int slot_tersedia;
    public List<LokasiLantaiParkir> lokasi_lantai_parkir;
}
