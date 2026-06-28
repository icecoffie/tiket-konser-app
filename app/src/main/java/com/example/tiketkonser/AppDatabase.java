package com.example.tiketkonser;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ConcertTicket.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TicketDao ticketDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "kpop_tix_db")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                TicketDao dao = INSTANCE.ticketDao();
                
                // Seeding initial data
                dao.insert(new ConcertTicket("ITZY", "BORN TO BE World Tour", "Indonesia Arena, Jakarta", "15 Jun 2024", "VIP", 3500000, 2, "Paid"));
                dao.insert(new ConcertTicket("aespa", "SYNK: Parallel Line", "Beach City International Stadium", "24 Aug 2024", "CAT 1", 2800000, 1, "Paid"));
                dao.insert(new ConcertTicket("EXO", "EXO-SC Back to Back", "Beach City International Stadium", "04 Feb 2024", "CAT 2", 1500000, 1, "Used"));
                dao.insert(new ConcertTicket("TWICE", "READY TO BE World Tour", "JIS, Jakarta", "23 Dec 2023", "Festival", 2500000, 2, "Used"));
                dao.insert(new ConcertTicket("NewJeans", "Bunnies Camp", "TBA, Jakarta", "10 Oct 2024", "VIP", 3800000, 1, "Pending"));
                dao.insert(new ConcertTicket("BLACKPINK", "BORN PINK World Tour", "GBK, Jakarta", "11 Mar 2023", "VIP", 3400000, 2, "Used"));
                dao.insert(new ConcertTicket("Stray Kids", "dominATE World Tour", "Madya Stadium, Jakarta", "21 Dec 2024", "CAT 1", 3200000, 1, "Paid"));
                dao.insert(new ConcertTicket("SEVENTEEN", "FOLLOW World Tour", "TBA, Jakarta", "15 Nov 2024", "Festival", 2600000, 2, "Pending"));
            });
        }
    };
}
