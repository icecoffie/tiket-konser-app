package com.example.tiketkonser;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TicketDao {
    @Insert
    void insert(ConcertTicket ticket);

    @Update
    void update(ConcertTicket ticket);

    @Delete
    void delete(ConcertTicket ticket);

    @Query("SELECT * FROM tickets ORDER BY id DESC")
    LiveData<List<ConcertTicket>> getAllTickets();

    @Query("SELECT * FROM tickets WHERE groupName = :group ORDER BY id DESC")
    LiveData<List<ConcertTicket>> getTicketsByGroup(String group);

    @Query("SELECT * FROM tickets WHERE groupName LIKE :query OR concertTitle LIKE :query OR venue LIKE :query ORDER BY id DESC")
    LiveData<List<ConcertTicket>> searchTickets(String query);

    @Query("SELECT SUM(price * quantity) FROM tickets WHERE status = 'Paid'")
    LiveData<Long> getTotalSpending();

    @Query("SELECT SUM(quantity) FROM tickets")
    LiveData<Integer> getTotalTickets();

    @Query("SELECT DISTINCT groupName FROM tickets")
    LiveData<List<String>> getDistinctGroups();
}
