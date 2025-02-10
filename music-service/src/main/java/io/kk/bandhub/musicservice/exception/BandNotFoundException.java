package io.kk.bandhub.musicservice.exception;

public class BandNotFoundException extends IllegalArgumentException{
    public BandNotFoundException(String msg){super(msg);}
}
