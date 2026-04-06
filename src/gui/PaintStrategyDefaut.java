package gui;

import java.awt.Color;
import java.awt.Graphics;

import config.GameConfiguration;
import engine.habitant.Habitant;
import engine.habitant.lien.Familial;
import engine.habitant.lien.Liens;
import engine.habitant.lien.Professionnel;
import engine.habitant.etat.EtatHabitant;
import engine.habitant.visitor.CouleurVisitor;
import engine.map.Block;
import engine.map.Map;

/**
 * Université CY Cergy Paris - L2 Informatique
 * Genie Logiciel - Projet SOCIAL
 *
 * @author HANANE Sanaa & PIRABAKARAN Parthipan
 *
 * Implémentation par défaut de PaintStrategy.
 * Calquée sur TriColoredStrategy du prof dans Tree V2 :
 * une classe concrète qui implémente le contrat de dessin.
 * Pour changer le rendu, on crée une nouvelle classe
 * sans toucher au reste du code.
 *
 * [AJOUT - Modèle SIR]
 * Les habitants porteurs d'une information sont distingués visuellement
 * par une auréole dorée dessinée AVANT le cercle principal.
 * Cela permet d'observer la propagation de l'information dans le réseau
 * comme une vague visible se déplaçant de proche en proche.
 */
public class PaintStrategyDefaut implements PaintStrategy {

    private boolean afficherFamille = true;
    private boolean afficherTravail = true;
    private boolean afficherAmis = true;

    // Couleur de l'auréole — dorée, distincte de tous les états psychologiques
    private static final Color COULEUR_INFORME = new Color(255, 215, 0);

    @Override
    public void setFiltres(boolean famille, boolean travail, boolean amis) {
        this.afficherFamille = famille;
        this.afficherTravail = travail;
        this.afficherAmis = amis;
    }

    @Override
    public void paint(Map map, Graphics graphics) {
        int blockSize = GameConfiguration.BLOCK_SIZE;
        Block[][] blocks = map.getBlocks();

        for (int lineIndex = 0; lineIndex < map.getLineCount(); lineIndex++) {
            for (int columnIndex = 0; columnIndex < map.getColumnCount(); columnIndex++) {
                Block block = blocks[lineIndex][columnIndex];
                graphics.setColor(Color.WHITE);
                graphics.fillRect(
                        block.getColumn() * blockSize,
                        block.getLine() * blockSize,
                        blockSize, blockSize);
            }
        }
    }

    @Override
    public void paint(Habitant habitant, Graphics graphics) {
        Block position = habitant.getPosition();
        int blockSize = GameConfiguration.BLOCK_SIZE;

        int x1 = position.getColumn() * blockSize + blockSize / 2;
        int y1 = position.getLine() * blockSize + blockSize / 2;

        // Dessin des liens sociaux
        if (habitant.getRelation() != null) {
            for (Liens lien : habitant.getRelation()) {
                Habitant ami = lien.getPartenaire();

                if (lien instanceof Familial     && !afficherFamille) continue;
                if (lien instanceof Professionnel && !afficherTravail) continue;
                if (!(lien instanceof Familial) && !(lien instanceof Professionnel)
                        && !afficherAmis) continue;

                if (lien instanceof Familial) {
                    graphics.setColor(Color.PINK);
                } else if (lien instanceof Professionnel) {
                    graphics.setColor(Color.BLUE);
                } else {
                    graphics.setColor(Color.GREEN);
                }

                Block posAmi = ami.getPosition();
                int x2 = posAmi.getColumn() * blockSize + blockSize / 2;
                int y2 = posAmi.getLine() * blockSize + blockSize / 2;
                graphics.drawLine(x1, y1, x2, y2);
            }
        }

        int x = position.getColumn();
        int y = position.getLine();

        // ── [AJOUT SIR] Auréole dorée pour les habitants informés ──────────────
        // Dessinée AVANT le cercle de l'habitant pour apparaître en arrière-plan.
        // +4 pixels de rayon et -2 pixels d'offset pour centrer l'auréole.
        // Principe : on ne modifie pas les classes EtatHabitant existantes —
        // c'est la couche graphique seule qui connaît cette règle d'affichage.
        if (habitant.estInforme()) {
            graphics.setColor(COULEUR_INFORME);
            graphics.drawOval(
                    x * blockSize - 2,
                    y * blockSize - 2,
                    blockSize + 4,
                    blockSize + 4
            );
        }

        // ── Dessin principal de l'habitant ──────────────────────────────────────
        int sante = habitant.getBesoins().getSante();

        if (sante <= 0) {
            // Décès — cas spécial, pas d'état psychologique
            graphics.setColor(Color.BLACK);
        } else {
            // On délègue la couleur au CouleurVisitor — double dispatch
            EtatHabitant etat = habitant.getPsychologie().determinerEtat(habitant.getBesoins());
            Color couleur = etat.accept(new CouleurVisitor());
            graphics.setColor(couleur);
        }

        graphics.fillOval(x * blockSize, y * blockSize, blockSize, blockSize);
        graphics.setColor(Color.BLACK);
        graphics.drawOval(x * blockSize, y * blockSize, blockSize, blockSize);
    }
}
