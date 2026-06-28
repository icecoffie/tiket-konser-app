package com.example.tiketkonser;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TicketRepository {
    private final TicketDao mTicketDao;
    private final LiveData<List<ConcertTicket>> mAllTickets;

    public TicketRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mTicketDao = db.ticketDao();
        mAllTickets = mTicketDao.getAllTickets();
    }

    public LiveData<List<ConcertTicket>> getAllTickets() {
        return mAllTickets;
    }

    public LiveData<List<ConcertTicket>> getTicketsByGroup(String group) {
        return mTicketDao.getTicketsByGroup(group);
    }

    public LiveData<List<ConcertTicket>> searchTickets(String query) {
        return mTicketDao.searchTickets("%" + query + "%");
    }

    public LiveData<Long> getTotalSpending() {
        return mTicketDao.getTotalSpending();
    }

    public LiveData<Integer> getTotalTickets() {
        return mTicketDao.getTotalTickets();
    }

    public LiveData<List<String>> getDistinctGroups() {
        return mTicketDao.getDistinctGroups();
    }

    public void insert(ConcertTicket ticket) {
        AppDatabase.databaseWriteExecutor.execute(() -> mTicketDao.insert(ticket));
    }

    public void update(ConcertTicket ticket) {
        AppDatabase.databaseWriteExecutor.execute(() -> mTicketDao.update(ticket));
    }

    public void delete(ConcertTicket ticket) {
        AppDatabase.databaseWriteExecutor.execute(() -> mTicketDao.delete(ticket));
    }
}
