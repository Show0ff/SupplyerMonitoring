// Получаем ссылки на кнопки статуса задач пользователя и контейнеры с текстом задач
const todoButton = document.getElementById("todo-button");
const inProgressButton = document.getElementById("inprogress-button");
const doneButton = document.getElementById("done-button");
const todoContainer = document.getElementById("todo-container");
const inProgressContainer = document.getElementById("inprogress-container");
const doneContainer = document.getElementById("done-container");

// Скрываем все контейнеры, кроме контейнера с задачами со статусом "TODO"
inProgressContainer.style.display = "none";
doneContainer.style.display = "none";

// Обработчики событий для кликов на кнопки статуса задач пользователя
todoButton.addEventListener("click", function() {
    todoContainer.style.display = "block";
    inProgressContainer.style.display = "none";
    doneContainer.style.display = "none";
});

inProgressButton.addEventListener("click", function() {
    todoContainer.style.display = "none";
    inProgressContainer.style.display = "block";
    doneContainer.style.display = "none";
});

doneButton.addEventListener("click", function() {
    todoContainer.style.display = "none";
    inProgressContainer.style.display = "none";
    doneContainer.style.display = "block";
});

// Получаем ссылки на кнопки статуса задач, которые пользователь выдал, и контейнеры с текстом задач
const manageTodoButton = document.getElementById("manage-todo-button");
const manageInProgressButton = document.getElementById("manage-inprogress-button");
const manageDoneButton = document.getElementById("manage-done-button");
const manageTodoContainer = document.getElementById("manage-todo-container");
const manageInProgressContainer = document.getElementById("manage-inprogress-container");
const manageDoneContainer = document.getElementById("manage-done-container");

// Скрываем все контейнеры, кроме контейнера с задачами со статусом "TODO"
manageInProgressContainer.style.display = "none";
manageDoneContainer.style.display = "none";

// Обработчики событий для кликов на кнопки статуса задач, которые пользователь выдал
manageTodoButton.addEventListener("click", function() {
    manageTodoContainer.style.display = "block";
    manageInProgressContainer.style.display = "none";
    manageDoneContainer.style.display = "none";
});

manageInProgressButton.addEventListener("click", function() {
    manageTodoContainer.style.display = "none";
    manageInProgressContainer.style.display = "block";
    manageDoneContainer.style.display = "none";
});

manageDoneButton.addEventListener("click", function() {
    manageTodoContainer.style.display = "none";
    manageInProgressContainer.style.display = "none";
    manageDoneContainer.style.display = "block";
});