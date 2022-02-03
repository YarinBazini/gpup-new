package components.login;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import okhttp3.HttpUrl;
import okhttp3.Response;
import utils.Constants;
import utils.HttpClientUtil;

public class LoginController {
    @FXML
    private Label login_header;

    @FXML
    private Label user_label;

    @FXML
    private TextField userName;

    @FXML
    private Label threads_label;

    @FXML
    private ComboBox<Integer> amountOfThreads;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private Button loginButton;

    @FXML
    private Label welcome_header;

    private final StringProperty errorMessageProperty = new SimpleStringProperty();

    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
        amountOfThreads.getItems().removeAll(amountOfThreads.getItems());
        amountOfThreads.getItems().addAll(1,2,3,4,5);
        amountOfThreads.getSelectionModel().select(0);
    }

    @FXML
    void onLogin(ActionEvent event) {
        String userName = this.userName.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            this.errorMessageLabel.getStyleClass().remove("successes_message");
            this.errorMessageLabel.getStyleClass().add("failed_message");
            return;
        }
        String finalUrl = HttpUrl.parse(Constants.WORKER_LOGIN).newBuilder().
                addQueryParameter("userName", userName).
                addQueryParameter("numOfThreads", amountOfThreads.getValue().toString())
                .build().
                toString();

        Response response = HttpClientUtil.runSync(finalUrl);
        if (response != null && response.code() == 200) {
            this.errorMessageProperty.set("");
            this.errorMessageLabel.getStyleClass().remove("failed_message");
        } else {
            this.errorMessageLabel.getStyleClass().remove("successes_message");
            this.errorMessageLabel.getStyleClass().add("failed_message");
            if (response != null && response.code() == 401 )
                this.errorMessageProperty.set("User already exists in the system");
            else
                this.errorMessageProperty.set("Something went wrong");
        }
    }
}
