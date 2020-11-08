package com.example.notes.ui.reminders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notes.models.Reminders;

import java.util.ArrayList;

public class RemindersViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Reminders>> mAlReminders;

    public RemindersViewModel() {
        ArrayList<String> fechas = new ArrayList<>();
        Reminders reminder = new Reminders(1,"Guardar el carro","Tengo que guardar el carro",fechas,"mañana");
        Reminders reminder2 = new Reminders(1,"Bañar el carro","Tengo que bañar el carro porque ya se me andaba echando a perder compadre",fechas,"mañana");
        Reminders reminder3 = new Reminders(1,"Bañar el carro","A veces Tengo que bañar el carro porque ya se me andaba echando a perder compadre",fechas,"mañana");
        ArrayList <Reminders> reminders = new ArrayList<>();
        reminders.add(reminder);
        reminders.add(reminder2);
        reminders.add(reminder3);
        mAlReminders = new MutableLiveData<>();
        mAlReminders.setValue(reminders);
    }

    public LiveData<ArrayList<Reminders>> getList() {
        return mAlReminders;
    }

}