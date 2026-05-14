package com.aksharadeepa.tutor.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.aksharadeepa.tutor.utils.NotificationHelper

class StudyReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        NotificationHelper.createReminderChannel(applicationContext)
        NotificationHelper.showReminder(
            applicationContext,
            title = "Time to study",
            message = "Take a 5-question quiz to keep your streak alive."
        )
        return Result.success()
    }
}
