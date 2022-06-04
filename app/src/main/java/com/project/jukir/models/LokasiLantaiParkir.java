package com.project.jukir.models;

import java.io.Serializable;
import java.util.List;

public class LokasiLantaiParkir implements Serializable {

    public int id;
    public int lokasi_id;
    public int lokasi_lantai_id;
    public String spot;
    public boolean status;
    public Object created_at;
    public Object updated_at;
}
