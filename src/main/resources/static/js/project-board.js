document.addEventListener('DOMContentLoaded', function () {
    let tasks = document.querySelectorAll('.task');
    for (let task of tasks) {
        task.draggable = true;
    }

    let columns = document.querySelectorAll('.column');
    for (let column of columns) {
        column.addEventListener('dragover', function (event) {
            event.preventDefault();
        });

        column.addEventListener('drop', function (event) {
            let taskId = event.dataTransfer.getData('task-id');
            let task = document.getElementById(taskId);
            column.appendChild(task);

            let newStatus;
            if (column.id === 'todo') {
                newStatus = 'TODO';
            } else if (column.id === 'inprogress') {
                newStatus = 'IN_PROGRESS';
            } else if (column.id === 'done') {
                newStatus = 'DONE';
            }

            // Call API to update task status
            updateTaskStatus(taskId, newStatus);
        });
    }

    for (let task of tasks) {
        task.addEventListener('dragstart', function (event) {
            event.dataTransfer.setData('task-id', event.target.id);
        });
    }
});

function updateTaskStatus(taskId, newStatus) {
    fetch(`/api/tasks/${taskId}/status`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({status: newStatus}),
    }).catch((error) => {
        console.error('Error updating task status:', error);
    });
}
