package components.generalComponents.tasksTable;

import com.google.gson.Gson;
import dtoObjects.TaskDTO;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class DoneTaskTableRefresher extends TimerTask {
    private Consumer<List<TaskFX>> usersListConsumer;
    private int requestNumber;
    private BooleanProperty shouldUpdate;

    public DoneTaskTableRefresher(BooleanProperty shouldUpdate, Consumer<List<TaskFX>> usersListConsumer) {
        this.shouldUpdate = shouldUpdate;
        this.usersListConsumer = usersListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {

        if (!shouldUpdate.get()) {
            return;
        }

        final int finalRequestNumber = ++requestNumber;
        HttpClientUtil.runAsync(Constants.TASKS_LIST_DONE, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Fail on refresh tasks list.");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfGraphs = response.body().string();
                Gson gson = new Gson();
                TaskDTO[] taskDTOS = gson.fromJson(jsonArrayOfGraphs, TaskDTO[].class);
                List<TaskFX> taskFxs = new ArrayList<>();
                for (TaskDTO taskDTO:taskDTOS){
                    taskFxs.add(new TaskFX(taskDTO));
                }
                usersListConsumer.accept(taskFxs);
            }
        });
    }
}
