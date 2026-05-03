package com.streetLeague.backend.enums;

/**
 * Step 6 : Statut du paiement d'une réservation de séance.
 * EN_ATTENTE : paiement pas encore effectué
 * PAYE       : paiement reçu
 * REMBOURSE  : paiement remboursé (après annulation)
 */
public enum StatutPaiement {
    EN_ATTENTE,
    PAYE,
    REMBOURSE
}
