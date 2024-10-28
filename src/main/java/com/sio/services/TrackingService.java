package com.sio.services;

import com.sio.apis.MockChrevTzyonApiClient;
import com.sio.models.Target;
import com.sio.repositories.PositionRepository;
import com.sio.repositories.TargetRepository;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import com.sio.models.Position;

public class TrackingService {

    private final MockChrevTzyonApiClient mockChrevTzyonApiClient = new MockChrevTzyonApiClient();
    private final TargetRepository targetRepository;
    private final PositionRepository positionRepository;

    public TrackingService() {
        this.targetRepository = new TargetRepository();
        this.positionRepository = new PositionRepository();
    }

    /**
     * Met à jour les positions de toutes les cibles
     * Si la cible n'existe pas dans la base de données, elle est créée
     * Si la cible existe dans la base de données, sa nouvelle position est ajoutée
     * Affiche le message suivant après que la position a été acquise avec succès :
     * - {nom de code de la cible} : Position acquise avec succès
     * Affiche le message suivant après que la position n'a pas été acquise
     * (l'API n'a pas encore acquis la position) :
     * - {nom de code de la cible} : Position non acquise
     */
    public void updateTargetsPositions() {
        ArrayList<Target> targets = targetRepository.findAll();

        for (Target target : targets) {
            ArrayList<org.json.simple.JSONObject> targetsFromApi = mockChrevTzyonApiClient.getTargets();

            if (targetsFromApi != null) {
                boolean positionAcquise = false;

                for (org.json.simple.JSONObject jsonTarget : targetsFromApi) {
                    if (jsonTarget.get("hash").equals(target.getHash())) {
                        Float latitude = Float.parseFloat(jsonTarget.get("latitude").toString());
                        Float longitude = Float.parseFloat(jsonTarget.get("longitude").toString());

                        Instant instant = Instant.parse(jsonTarget.get("timestamp").toString());
                        Timestamp timestamp = Timestamp.from(instant);

                        Position newPosition = new Position(target, latitude, longitude, timestamp);

                        if (positionRepository.findByTargetHash(target.getHash()).isEmpty()) {
                            // La cible n'existe pas dans la base de données, on la crée
                            targetRepository.create(target);
                            mockChrevTzyonApiClient.addTarget(target);
                        }

                        positionRepository.create(newPosition);
                        System.out.println(target.getCodeName() + " : Position acquise avec succès");
                        positionAcquise = true;
                        break;
                    }
                }

                if (!positionAcquise) {
                    System.out.println(target.getCodeName() + " : Position non acquise");
                }
            } else {
                System.out.println("Échec de la récupération des cibles depuis l'API.");
            }
        }
    }
}