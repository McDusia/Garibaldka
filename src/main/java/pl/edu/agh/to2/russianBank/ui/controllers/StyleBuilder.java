package pl.edu.agh.to2.russianBank.ui.controllers;

import com.google.common.collect.Lists;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import java.util.List;
import java.util.Map;

import static pl.edu.agh.to2.russianBank.ui.controllers.StyleConstants.*;


public class StyleBuilder {

    public void setPosition(Map<Integer, CardView> foundations, Map<Integer,
            CardView> hands,Map<Integer, CardView> wastes, Map<Integer, List<CardView>> houses) {
        GridPane.setConstraints(hands.get(0), MY_HAND_COL, MY_HAND_ROW);
        GridPane.setConstraints(wastes.get(0), MY_WASTE_COL, MY_WASTE_ROW);
        GridPane.setConstraints(foundations.get(0), FOUNDATION_COL1, FOUNDATION_ROW1);
        GridPane.setConstraints(foundations.get(1), FOUNDATION_COL1, FOUNDATION_ROW2);
        GridPane.setConstraints(foundations.get(2), FOUNDATION_COL1, FOUNDATION_ROW3);
        GridPane.setConstraints(foundations.get(3), FOUNDATION_COL1, FOUNDATION_ROW4);
        GridPane.setConstraints(foundations.get(4), FOUNDATION_COL2, FOUNDATION_ROW1);
        GridPane.setConstraints(foundations.get(5), FOUNDATION_COL2, FOUNDATION_ROW2);
        GridPane.setConstraints(foundations.get(6), FOUNDATION_COL2, FOUNDATION_ROW3);
        GridPane.setConstraints(foundations.get(7), FOUNDATION_COL2, FOUNDATION_ROW4);
        GridPane.setConstraints(wastes.get(1), OPP_WASTE_COL, OPP_WASTE_ROW);
        GridPane.setConstraints(hands.get(1), OPP_HAND_COL, OPP_HAND_ROW);

        addImageViews(FOUNDATION_ROW1, FOUNDATION_COL1_EXPAND, Lists.reverse(houses.get(0)));
        addImageViews(FOUNDATION_ROW2, FOUNDATION_COL1_EXPAND, Lists.reverse(houses.get(1)));
        addImageViews(FOUNDATION_ROW3, FOUNDATION_COL1_EXPAND, Lists.reverse(houses.get(2)));
        addImageViews(FOUNDATION_ROW4, FOUNDATION_COL1_EXPAND, Lists.reverse(houses.get(3)));

        addImageViews(FOUNDATION_ROW1, FOUNDATION_COL2_EXPAND, houses.get(4));
        addImageViews(FOUNDATION_ROW2, FOUNDATION_COL2_EXPAND, houses.get(5));
        addImageViews(FOUNDATION_ROW3, FOUNDATION_COL2_EXPAND, houses.get(6));
        addImageViews(FOUNDATION_ROW4, FOUNDATION_COL2_EXPAND, houses.get(7));
    }

    /**
     * Function set ImageViews in proper position in stage
     *
     * @param images list of CardView e.g. foundations, houses...
     */

    private void addImageViews(int row, int minColumn, List<CardView> images) {
        for (int i = 0; i < images.size(); i++) {
            GridPane.setConstraints(images.get(i), minColumn + i, row);
        }
    }

    public void setLabelsPositions(Label myName, Label opponentName) {
        GridPane.setHalignment(myName, HPos.CENTER);
        GridPane.setValignment(myName, VPos.TOP);
        GridPane.setConstraints(myName, 0, 9, 2, 2);

        GridPane.setHalignment(opponentName, HPos.CENTER);
        GridPane.setValignment(opponentName, VPos.BOTTOM);
        GridPane.setConstraints(opponentName, 25, 3, 2, 2);
    }

}
