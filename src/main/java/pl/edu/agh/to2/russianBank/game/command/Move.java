package pl.edu.agh.to2.russianBank.game.command;

import com.google.common.base.MoreObjects;
import pl.edu.agh.to2.russianBank.game.*;
import pl.edu.agh.to2.russianBank.ui.controllers.Service;

import java.util.*;
import java.util.stream.Collectors;

public class Move implements Command {
    private ICardSet source;
    private ICardSet target;

    public Move(ICardSet source, ICardSet target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public boolean execute(GameTable gameTable) {

        int sourcePos = source.getPosition();
        int targetPos = target.getPosition();

        boolean result = false;
        List<Integer> obligatoryMoveSources = getObligatoryMoves(gameTable);

        if (obligatoryMoveSources.contains(sourcePos)){
            if (targetPos < 12)
                return false;
        }

        if ((sourcePos == 0 && targetPos == 1) || (sourcePos == 2 && targetPos == 3)) { // MY HAND -> MY WASTE
            result = source.readTopCard().map(c -> target.putCard(c)).orElse(false);
        } else if (targetPos == 1 || targetPos == 3) { // OTHER -> WASTE
            result = source.readTopCard().map(c -> target.enemyPutCard(c)).orElse(false);
        } else { // OTHER -> OTHER
            result = source.readTopCard().map(c -> target.putCard(c)).orElse(false);
        }

        if (result) {
            source.takeTopCard();
        }

        // TODO : can we move this entire if outside of Move? @J
        if ((sourcePos == 0 || sourcePos == 2) && source.getSize() == 0) {
            if (isHandEmpty(gameTable, sourcePos)) {
                Service.getInstance().getClient().endGame(true, "winning condition");
            } else {
                Service.getInstance().getClient().swapHandWaste(sourcePos, sourcePos + 1);
            }
        }
        return result;
    }


    public void redo(GameTable gameTable) {
        target.putCard(source.takeTopCard().get());
    }

    public void undo(GameTable gameTable) {
        source.putCard(target.takeTopCard().get());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return source == move.source &&
                target == move.target;
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("source", source)
                .add("target", target)
                .toString();
    }

    private boolean isHandEmpty(GameTable gameTable, int sourcePos) {
        Waste waste = gameTable.getPlayersCard().stream()
                .filter(pD -> pD.getHand().getPosition() == sourcePos)
                .map(pD -> pD.getWaste()).findFirst().get();

        if (gameTable.getPlayersCard().stream()
                .filter(pD -> pD.getHand().getPosition() == sourcePos)
                .allMatch(pD -> pD.getWaste().getSize() == 0)) {
            return true;
        } else {
            gameTable.swapPiles(source, waste);
            return false;
        }
    }

    private List<Integer> getObligatoryMoves(GameTable gameTable) {
        List<Integer> result = new ArrayList<>();
        List<Foundation> foundations = gameTable.getFoundations().stream().map(cs -> (Foundation) cs).collect(Collectors.toList());
        List<House> houses = gameTable.getHouses().stream().map(cs -> (House) cs).collect(Collectors.toList());
        List<Hand> hands = gameTable.getPlayersCard().stream().map(pd -> pd.getHand()).filter(hand -> hand.getPosition() == source.getPosition()).collect(Collectors.toList());
        List<Waste> wastes = gameTable.getPlayersCard().stream().map(pd -> pd.getWaste()).filter(waste -> waste.getPosition() == source.getPosition()).collect(Collectors.toList());

        houses.stream().filter(house -> house.readTopCard().isPresent())
                .forEach(house -> {foundations.stream()
                        .filter(foundation -> foundation.tryPutCard(house.readTopCard().get()))
                        .forEach(foundation -> result.add(house.getPosition()));
                });

        hands.stream().filter(hand -> hand.readTopCard().isPresent())
                .forEach(hand -> {foundations.stream()
                        .filter(foundation -> foundation.tryPutCard(hand.readTopCard().get()))
                        .forEach(foundation -> result.add(hand.getPosition()));
                });

        wastes.stream().filter(waste -> waste.readTopCard().isPresent())
                .forEach(waste -> {foundations.stream()
                        .filter(foundation -> foundation.tryPutCard(waste.readTopCard().get()))
                        .forEach(foundation -> result.add(waste.getPosition()));
                });

        return result;
    }
}
