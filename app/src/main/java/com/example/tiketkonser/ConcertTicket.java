package com.example.tiketkonser;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tickets")
public class ConcertTicket {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String groupName;
    private String concertTitle;
    private String venue;
    private String eventDate;
    private String seatSection;
    private long price;
    private int quantity;
    private String status;

    public ConcertTicket(String groupName, String concertTitle, String venue, String eventDate, String seatSection, long price, int quantity, String status) {
        this.groupName = groupName;
        this.concertTitle = concertTitle;
        this.venue = venue;
        this.eventDate = eventDate;
        this.seatSection = seatSection;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getConcertTitle() { return concertTitle; }
    public void setConcertTitle(String concertTitle) { this.concertTitle = concertTitle; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    public String getSeatSection() { return seatSection; }
    public void setSeatSection(String seatSection) { this.seatSection = seatSection; }

    public long getPrice() { return price; }
    public void setPrice(long price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
