package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Unit test on Neighbour service
 */
@RunWith(JUnit4.class)
public class NeighbourServiceTest {

    private NeighbourApiService service;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
    }

    @Test
    public void getNeighboursWithSuccess() {
        List<Neighbour> neighbours = service.getNeighbours();
        List<Neighbour> expectedNeighbours = DummyNeighbourGenerator.DUMMY_NEIGHBOURS;
        assertThat(neighbours, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedNeighbours.toArray()));
    }

    @Test
    public void deleteNeighbourWithSuccess() {
        Neighbour neighbourToDelete = service.getNeighbours().get(0);
        service.deleteNeighbour(neighbourToDelete);
        assertFalse(service.getNeighbours().contains(neighbourToDelete));
    }

    @Test
    public void getListFavoriteNeighbourNotEmpty(){
        Neighbour neighbour = service.getNeighbours().get(0);
        service.addNeighbourFavorite(neighbour);
        assertFalse(service.getFavoriteNeighbours().isEmpty());
    }

    @Test
    public void addNeighbourToFavoriteWithSuccess(){
        Neighbour neighbourToAddFavorite = service.getNeighbours().get(0);
        service.addNeighbourFavorite(neighbourToAddFavorite);
        assertTrue(service.getFavoriteNeighbours().contains(neighbourToAddFavorite));
    }

    @Test
    public void removeNeighbourToFavoriteWithSuccess(){
        Neighbour neighbourToRemovefavorite = service.getNeighbours().get(1);
        service.addNeighbourFavorite(neighbourToRemovefavorite);
        assertTrue(service.getFavoriteNeighbours().contains(neighbourToRemovefavorite));
        service.deleteNeighbourFavorite(neighbourToRemovefavorite);
        assertFalse(service.getFavoriteNeighbours().contains(neighbourToRemovefavorite));
    }
}
