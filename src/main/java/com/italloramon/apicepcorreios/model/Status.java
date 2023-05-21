package com.italloramon.apicepcorreios.model;

public enum Status {
    NEED_SETUP, //Need download the csv from Correios
    RUNNING, // Is loading/saving in the database
    READY;
}
