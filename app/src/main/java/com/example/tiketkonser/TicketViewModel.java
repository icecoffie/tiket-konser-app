package com.example.tiketkonser;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

public class TicketViewModel extends AndroidViewModel {
    private final TicketRepository mRepository;
    private final LiveData<List<ConcertTicket>> mAllTickets;
    
    private final MutableLiveData<String> filterGroup = new MutableLiveData<>("Semua");
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");

    public TicketViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TicketRepository(application);
        mAllTickets = mRepository.getAllTickets();
    }

    public LiveData<List<ConcertTicket>> getAllTickets() {
        return mAllTickets;
    }

    public LiveData<List<ConcertTicket>> getFilteredTickets() {
        // Simple logic: if group is "Semua", return all or search results.
        // For production, we might combine search and filter.
        return Transformations.switchMap(filterGroup, group -> {
            if (group.equals("Semua")) {
                return Transformations.switchMap(searchQuery, query -> {
                    if (query.isEmpty()) return mRepository.getAllTickets();
                    else return mRepository.searchTickets(query);
                });
            } else {
                return mRepository.getTicketsByGroup(group);
            }
        });
    }

    public LiveData<Long> getTotalSpending() { return mRepository.getTotalSpending(); }
    public LiveData<Integer> getTotalTickets() { return mRepository.getTotalTickets(); }
    public LiveData<List<String>> getDistinctGroups() { return mRepository.getDistinctGroups(); }

    public void setFilterGroup(String group) { filterGroup.setValue(group); }
    public void setSearchQuery(String query) { searchQuery.setValue(query); }

    public void insert(ConcertTicket ticket) { mRepository.insert(ticket); }
    public void update(ConcertTicket ticket) { mRepository.update(ticket); }
    public void delete(ConcertTicket ticket) { mRepository.delete(ticket); }
}
