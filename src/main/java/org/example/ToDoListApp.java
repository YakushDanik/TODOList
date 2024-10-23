package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class ToDoListApp extends Application {

    private ArrayList<String> tasks;
    private ListView<String> taskListView;

    @Override
    public void start(Stage primaryStage) {
        tasks = new ArrayList<>();
        taskListView = new ListView<>();
        TextField taskInput = new TextField();
        taskInput.setPromptText("Введите задачу");

        Button addButton = new Button("Добавить");
        addButton.setOnAction(e -> addTask(taskInput.getText()));

        Button editButton = new Button("Редактировать");
        editButton.setOnAction(e -> editTask());

        Button completeButton = new Button("Выполнено");
        completeButton.setOnAction(e -> completeTask());

        Button deleteButton = new Button("Удалить");
        deleteButton.setOnAction(e -> deleteTask());

        Button saveButton = new Button("Сохранить");
        saveButton.setOnAction(e -> saveTasks());

        Button loadButton = new Button("Загрузить");
        loadButton.setOnAction(e -> loadTasks());

        Button sortButton = new Button("Сортировать");
        sortButton.setOnAction(e -> sortTasks());

        VBox layout = new VBox(10, taskInput, addButton, editButton, completeButton, deleteButton, saveButton, loadButton, sortButton, taskListView);
        Scene scene = new Scene(layout, 300, 500);

        primaryStage.setTitle("To-Do List");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addTask(String task) {
        if (!task.trim().isEmpty()) {
            tasks.add(task + " [Невыполнено]");
            taskListView.getItems().add(task + " [Невыполнено]");
        }
    }

    private void editTask() {
        String selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            TextInputDialog dialog = new TextInputDialog(selectedTask);
            dialog.setTitle("Редактировать задачу");
            dialog.setHeaderText("Измените текст задачи:");
            dialog.setContentText("Задача:");

            String result = dialog.showAndWait().orElse("");
            if (!result.trim().isEmpty()) {
                int index = tasks.indexOf(selectedTask);
                tasks.set(index, result);
                taskListView.getItems().set(index, result);
            }
        }
    }

    private void completeTask() {
        String selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null && !selectedTask.contains("[Выполнено]")) {
            int index = tasks.indexOf(selectedTask);
            tasks.set(index, selectedTask.replace("[Невыполнено]", "[Выполнено]"));
            taskListView.getItems().set(index, selectedTask.replace("[Невыполнено]", "[Выполнено]"));
        }
    }

    private void deleteTask() {
        String selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            tasks.remove(selectedTask);
            taskListView.getItems().remove(selectedTask);
        }
    }

    private void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt"))) {
            for (String task : tasks) {
                writer.write(task);
                writer.newLine();
            }
            showAlert("Сохранение", "Задачи сохранены успешно.");
        } catch (IOException e) {
            showAlert("Ошибка", "Ошибка при сохранении задач.");
        }
    }

    private void loadTasks() {
        tasks.clear();
        taskListView.getItems().clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tasks.add(line);
                taskListView.getItems().add(line);
            }
            showAlert("Загрузка", "Задачи загружены успешно.");
        } catch (IOException e) {
            showAlert("Ошибка", "Ошибка при загрузке задач.");
        }
    }

    private void sortTasks() {
        Collections.sort(tasks);
        taskListView.getItems().setAll(tasks);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
