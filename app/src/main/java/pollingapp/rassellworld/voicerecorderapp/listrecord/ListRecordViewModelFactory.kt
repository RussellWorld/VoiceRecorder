package pollingapp.rassellworld.voicerecorderapp.listrecord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pollingapp.rassellworld.voicerecorderapp.database.RecordDatabaseDao

class ListRecordViewModelFactory(
    private val databaseDao: RecordDatabaseDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListRecordViewModel::class.java)) {
            return ListRecordViewModel(databaseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}