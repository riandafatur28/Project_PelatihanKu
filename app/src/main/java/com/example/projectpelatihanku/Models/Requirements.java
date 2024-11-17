package com.example.projectpelatihanku.Models;

public class Requirements {
    private String requirement;

    /**
     * Constructor untuk kelas Requirements
     * @param requirement Requirement programs yang ditampilkan
     */
    public Requirements(String requirement) {
        this.requirement = requirement;
    }

    /**
     * Getter untuk mengambil requirement programs
     * @return requirement programs
     */
    public String getRequirement() {
        return requirement;
    }
}
