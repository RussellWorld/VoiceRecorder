package pollingapp.rassellworld.voicerecorderapp.listrecord

import androidx.lifecycle.ViewModel
import pollingapp.rassellworld.voicerecorderapp.database.RecordDatabaseDao

class ListRecordViewModel(
    dataSource: RecordDatabaseDao
) : ViewModel() {

    val database = dataSource
    val records = database.getAllRecords()
}