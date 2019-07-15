package com.example.todoapp.data

class TasksRepository(

    val tasksLocalDataSource: TasksDataSource
) : TasksDataSource {

    var cachedTasks: LinkedHashMap<String, Task> = LinkedHashMap()

    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
        // Respond immediately with cache if available and not dirty
        if (cachedTasks.isNotEmpty()) {
            callback.onTasksLoaded(ArrayList(cachedTasks.values))
            return
        }

        tasksLocalDataSource.getTasks(object : TasksDataSource.LoadTasksCallback {
            override fun onTasksLoaded(tasks: List<Task>) {
                refreshCache(tasks)
                callback.onTasksLoaded(ArrayList(cachedTasks.values))
            }


            override fun onDataNotAvailable() {
            }

        })

    }

    override fun saveTask(task: Task) {
        cacheAndPerform(task) {
            tasksLocalDataSource.saveTask(it)
        }
    }

    override fun completeTask(task: Task) {
        cacheAndPerform(task) {
            it.isCompleted = true
            tasksLocalDataSource.completeTask(it)
        }
    }

    override fun completeTask(taskId: String) {
        getTaskWithId(taskId)?.let {
            completeTask(it)
        }
    }

    override fun activateTask(task: Task) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(task) {
            it.isCompleted = false
            tasksLocalDataSource.activateTask(it)
        }
    }

    override fun activateTask(taskId: String) {
        getTaskWithId(taskId)?.let {
            activateTask(it)
        }
    }

    override fun clearCompletedTasks() {
        tasksLocalDataSource.clearCompletedTasks()

        cachedTasks = cachedTasks.filterValues {
            !it.isCompleted
        } as LinkedHashMap<String, Task>
    }


    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        val taskInCache = getTaskWithId(taskId)

        if (taskInCache != null) {
            callback.onTaskLoaded(taskInCache)
            return
        }


        tasksLocalDataSource.getTask(taskId, object : TasksDataSource.GetTaskCallback {
            override fun onTaskLoaded(task: Task) {
                cacheAndPerform(task) {
                    callback.onTaskLoaded(it)
                }
            }

            override fun onDataNotAvailable() {
            }
        })
    }

    override fun deleteAllTasks() {
        tasksLocalDataSource.deleteAllTasks()
        cachedTasks.clear()
    }

    override fun deleteTask(taskId: String) {
        tasksLocalDataSource.deleteTask(taskId)
        cachedTasks.remove(taskId)
    }


    private fun refreshCache(tasks: List<Task>) {
        cachedTasks.clear()
        tasks.forEach {
            cacheAndPerform(it) {}
        }

    }

    private fun refreshLocalDataSource(tasks: List<Task>) {
        tasksLocalDataSource.deleteAllTasks()
        for (task in tasks) {
            tasksLocalDataSource.saveTask(task)
        }
    }

    private fun getTaskWithId(id: String) = cachedTasks[id]

    private inline fun cacheAndPerform(task: Task, perform: (Task) -> Unit) {
        val cachedTask = Task(task.title, task.description, task.id).apply {
            isCompleted = task.isCompleted
        }
        cachedTasks.put(cachedTask.id, cachedTask)
        perform(cachedTask)
    }

    override fun refreshTasks() {
    }


    companion object {

        private var INSTANCE: TasksRepository? = null

        @JvmStatic
        fun getInstance(
            tasksRemoteDataSource: TasksDataSource) =
            INSTANCE ?: synchronized(TasksRepository::class.java) {
                INSTANCE ?: TasksRepository(tasksRemoteDataSource)
                    .also { INSTANCE = it }
            }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }

}