package pl.edu.agh.to2.russianBank.game.command;

import com.google.common.base.MoreObjects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.edu.agh.to2.russianBank.game.*;
import pl.edu.agh.to2.russianBank.ui.controllers.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MoveController {
    private GameTable gameTable;
    private ObservableList<Command> commandStack = FXCollections
            .observableArrayList();
    private ObservableList<Command> unmadeCommandStack = FXCollections
            .observableArrayList();

    public MoveController(GameTable gameTable) {
        this.gameTable = gameTable;
    }

    public GameTable getGameTable() {
        return gameTable;
    }

    public boolean executeCommand(Command command) {
        if (command.execute(gameTable)) {
            commandStack.add(command);
            unmadeCommandStack.clear();
            return true;
        }
        return false;
    }

    public void redo() {
        if (!unmadeCommandStack.isEmpty()) {
            Command command = unmadeCommandStack.remove(unmadeCommandStack.size() - 1);
            command.redo(gameTable);
            commandStack.add(command);
        }
    }

    public void undo() {
        if (!commandStack.isEmpty()) {
            Command command = commandStack.remove(commandStack.size() - 1);
            command.undo(gameTable);
            unmadeCommandStack.add(command);
        }
    }

    public ObservableList<Command> getCommandStack() {
        return commandStack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveController that = (MoveController) o;
        return Objects.equals(gameTable, that.gameTable) &&
                Objects.equals(commandStack, that.commandStack) &&
                Objects.equals(unmadeCommandStack, that.unmadeCommandStack);
    }

    @Override
    public int hashCode() {

        return Objects.hash(gameTable, commandStack, unmadeCommandStack);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("gameTable", gameTable)
                .add("commandStack", commandStack)
                .add("unmadeCommandStack", unmadeCommandStack)
                .toString();
    }

    public List<Integer> getObligatoryMoves(GameTable gameTable, Move move) {
        ICardSet source = move.getSource(gameTable);
        List<Integer> result = new ArrayList<>();

        List<Foundation> foundations = gameTable.getFoundations()
                .stream()
                .map(cs -> (Foundation) cs)
                .collect(Collectors.toList());
        List<House> houses = gameTable.getHouses()
                .stream()
                .map(cs -> (House) cs)
                .collect(Collectors.toList());
        List<Hand> hands = gameTable.getPlayersCard()
                .stream()
                .map(PlayerDeck::getHand)
                .filter(hand -> hand.getPosition() == source.getPosition())
                .collect(Collectors.toList());
        List<Waste> wastes = gameTable.getPlayersCard().stream()
                .map(PlayerDeck::getWaste)
                .filter(waste -> waste.getPosition() == source.getPosition())
                .collect(Collectors.toList());

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

