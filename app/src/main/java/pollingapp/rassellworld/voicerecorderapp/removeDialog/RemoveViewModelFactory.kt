package info.fandroid.voicerecorder.removeDialog

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pollingapp.rassellworld.voicerecorderapp.database.RecordDatabaseDao

class RemoveViewModelFactory(
    private val databaseDao: RecordDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemoveViewModel::class.java)) {
            return RemoveViewModel(
                databaseDao,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}