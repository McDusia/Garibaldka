package pl.edu.agh.to2.russianBank.ui;

import pl.edu.agh.to2.russianBank.game.ICardSet;
import pl.edu.agh.to2.russianBank.game.Player;
import pl.edu.agh.to2.russianBank.game.command.Move;
import pl.edu.agh.to2.russianBank.net.client.ClientCallbacks;
import pl.edu.agh.to2.russianBank.ui.controllers.GameController;

public class ClientCallbacksImpl implements ClientCallbacks {

    private GameController controller;

    /*@Override
    public void startGame(GameTable table) {

        try {
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(RootLayout.class.getResource("Game.fxml"));
            Stage stage = new Stage();

            stage.setTitle("Garibaldka");
            Scene scene = new Scene(root, 1200, 1200);

            scene.widthProperty().addListener(new ChangeListener<Number>() {

                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    //GameController.updateWidthConstaints(newValue.doubleValue());
                }
            });

            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
            controller = loader.getController();
            controller.setTable(table);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

    @Override
    public void movingCard(Player player, ICardSet previousSlot, ICardSet newSlot) {
    }

    @Override
    public void endGame(String message) {

    }

    @Override
    public void move(Move move) {

    }

    @Override
    public void networkError(Throwable ex) {
        // TODO:
        ex.printStackTrace();
    }


}
