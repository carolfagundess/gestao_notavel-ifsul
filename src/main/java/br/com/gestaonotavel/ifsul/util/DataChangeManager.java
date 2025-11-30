package br.com.gestaonotavel.ifsul.util;

import java.util.ArrayList;
import java.util.List;

public class DataChangeManager {

    private static final DataChangeManager INSTANCE = new DataChangeManager();
    private List<DataChangeListener> listeners = new ArrayList<>();

    private DataChangeManager(){}

    public static DataChangeManager getInstance(){
        return INSTANCE;
    }

    public void addDataChangeListener(DataChangeListener dataChangeListener){
        if(!listeners.contains(dataChangeListener)){
            listeners.add(dataChangeListener);
        }
    }

    public void notificarListeners(String entidade){
        for(DataChangeListener listener : listeners){
            listener.atualizarDados(entidade);
        }
    }
}