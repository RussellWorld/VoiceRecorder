package info.fandroid.voicerecorder.removeDialog

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import pollingapp.rassellworld.voicerecorderapp.R
import pollingapp.rassellworld.voicerecorderapp.database.RecordDatabase
import pollingapp.rassellworld.voicerecorderapp.database.RecordDatabaseDao
import java.io.File

class RemoveViewModel(
    private var databaseDao: RecordDatabaseDao,
    private val application: Application
) : ViewModel() {

    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    fun removeItem(itemId: Long) {
        databaseDao = RecordDatabase.getInstance(application).recordDatabaseDao

        try {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    databaseDao.removeRecord(itemId)
                }
            }
        } catch (e: Exception) {
            Log.e("removeItem", "exception", e)
        }
    }

    fun removeFile(path: String) {
        val file = File(path)
        if (file.exists()) {
            file.delete()
            Toast.makeText(application, R.string.file_deleted_text, Toast.LENGTH_SHORT).show()
        }
    }
}










