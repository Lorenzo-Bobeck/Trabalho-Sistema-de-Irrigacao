package dao;

import java.util.ArrayList;
import java.util.List;
import Model.Plant;

public class PlantDAO {

    private List<Plant> listaPlantas = new ArrayList<>();

    public void salvar(Plant plant) {
        listaPlantas.add(plant);
    }

    public List<Plant> listar() {
        return listaPlantas;
    }
}