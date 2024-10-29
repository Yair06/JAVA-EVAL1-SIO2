package com.sio.services;

import com.sio.apis.MockChrevTzyonApiClient;
import com.sio.models.Target;
import com.sio.models.Position;
import com.sio.repositories.PositionRepository;
import com.sio.repositories.TargetRepository;


import java.util.ArrayList;

public class TargetService {

    private final MockChrevTzyonApiClient mockChrevTzyonApiClient = new MockChrevTzyonApiClient();
    private final TargetRepository tRepository;
    private final PositionRepository pRepository;

    public TargetService() {
        this.tRepository = new TargetRepository();
        this.pRepository = new PositionRepository();
    }

    /**
     * Get all targets stored in database and their respective positions
     * @return targets ArrayList
     */
    public ArrayList<Target> getTargets() {
        ArrayList<Target> targets = tRepository.findAll();

        for (Target target : targets) {
            int position = 0;
        }

        return tRepository.findAll();
    }

    /**
     * Add a target to the API
     * Print the following message after the target is successfully added to the API: Target successfully added, you will now have to wait 60 seconds before the target is available for position acquisition
     * @param codename String
     * @param name String
     */
    public void addTarget(String codename, String name) {

        Target newTarget = new Target();
        newTarget.setCodeName(codename);
        newTarget.setName(name);

        MockChrevTzyonApiClient apiClient = new MockChrevTzyonApiClient();
        boolean apiSuccess = apiClient.addTarget(newTarget);

        if (apiSuccess) {
            tRepository.create(newTarget);

            System.out.println("Target ajoutée.");
        } else {
            System.out.println("Target non ajoutée.");
}

    }

    /**
     * Delete a target from the database and the API
     * @param t Target
     */

    public void deleteTarget(Target t) {
        if (t == null) {
            System.out.println("La cible ne peut pas être nulle.");
            return;
        }

        try {
            boolean apiDeleteSuccess = mockChrevTzyonApiClient.deleteTarget(t);

            if (apiDeleteSuccess) {
                tRepository.delete(t);
                System.out.println("Cible avec le hash " + t.getHash() + " a été supprimée avec succès de la base de données et de l'API.");
            } else {
                System.out.println("Échec de la suppression de la cible de l'API.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression de la cible : " + e.getMessage());
        }
    }



}
