package net.davidvoid.thor.lightning.entity;

public interface Entity {
    public boolean has_valid_id();
    public long getId();
    public void setId(long id);
}
