package com.example.rafael.appprototype.DataTypes.DB;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.example.rafael.appprototype.DataTypes.NonDB.GradingNonDB;
import com.example.rafael.appprototype.DataTypes.NonDB.ScoringNonDB;
import com.example.rafael.appprototype.DataTypes.Scales;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael on 15-09-2016.
 */
@Table(name = "FreeTextField")
public class FreeTextField extends Model implements Serializable {


    @Column(name = "guid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    String guid;

    @Column(name = "field")
    String field;

    @Column(name = "addNotesButton")
    String notes;

    @Column(name = "session", onDelete = Column.ForeignKeyAction.CASCADE)
    Session session;


    public FreeTextField(String field) {
        super();
        this.field = field;
    }

    public FreeTextField() {
        super();
    }


    @Override
    public String toString() {
        String ret = "";
        ret += field + " - " + notes;
        return ret;
    }

    /**
     * Get all the text fields associated to a given session.
     *
     * @param session
     * @return
     */
    public static List<FreeTextField> getFieldBySession(Session session) {
        return new Select()
                .from(FreeTextField.class)
                .where("session = ?", session)
                .execute();
    }


    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getGuid() {
        return guid;
    }


    /**
     * Set the addNotesButton for this test,
     *
     * @param notes
     */
    public void setNotes(String notes) {
        //system.out.println("Adding note for this test");
        this.notes = notes;
    }

    public boolean hasNotes() {
        return notes != null;
    }

    public String getNotes() {
        return notes;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public static FreeTextField getFieldByID(String fieldID) {
        return new Select()
                .from(FreeTextField.class)
                .where("guid = ?", fieldID)
                .executeSingle();
    }
}
